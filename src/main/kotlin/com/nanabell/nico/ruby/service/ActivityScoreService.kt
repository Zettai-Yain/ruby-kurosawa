package com.nanabell.nico.ruby.service

import com.nanabell.nico.ruby.domain.ActivityScore
import com.nanabell.nico.ruby.entitiy.ActivityScoreEntity
import com.nanabell.nico.ruby.entitiy.ActivityScoreLogEntity
import com.nanabell.nico.ruby.domain.ActivityScoreRequest
import com.nanabell.nico.ruby.repository.ActivityScoreLogRepository
import com.nanabell.nico.ruby.repository.ActivityScoreRepository
import io.micrometer.core.instrument.MeterRegistry
import io.micronaut.data.model.Sort
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ActivityScoreService(
    private val repository: ActivityScoreRepository,
    private val logRepository: ActivityScoreLogRepository,
    private val registry: MeterRegistry
) {

    private val logger = LoggerFactory.getLogger(ActivityScoreService::class.java)

    fun find(id: Long): ActivityScore? {
        val entity = repository.findAllById(id)
        return if (entity.isNotEmpty()) ActivityScore(entity) else null
    }

    fun findAll(limit: Int, direction: Sort.Order.Direction?): List<ActivityScore> {
        return repository.findAll(limit.coerceAtLeast(0), direction).groupBy { it.id }.map { ActivityScore(it.value) }
    }

    fun setScore(id: Long, request: ActivityScoreRequest): ActivityScore {
        val activity = getOrCreate(id, request.source)

        val change = getChange(activity, request.score, ChangeType.SET)
        logger.info("Setting ${request.source} Score of User $id to ${request.score}")
        return persist(activity, change, request.source).let { find(id)!! }
    }

    fun add(id: Long, request: ActivityScoreRequest): ActivityScore {
        val activity = getOrCreate(id, request.source)

        val change = getChange(activity, request.score, ChangeType.ADD)
        logger.info("Adding ${request.score} to ${request.source} Score of User $id")
        return persist(activity, change, request.source).let { find(id)!! }
    }

    fun remove(id: Long, request: ActivityScoreRequest): ActivityScore {
        val activity = getOrCreate(id, request.source)

        val change = getChange(activity, request.score, ChangeType.REMOVE)
        logger.info("Removing ${request.score} to ${request.source} Score of User $id")
        return persist(activity, change, request.source).let { find(id)!! }
    }

    fun delete(id: Long, source: String?) {
        if (source != null) {
            logger.info("Deleting $source Score for User $id")
            repository.deleteByIdAndSource(id, source)
        } else {
            logger.info("Deleting all Score entries for User $id")
            repository.deleteById(id)
        }
    }

    private fun getChange(activityScoreEntity: ActivityScoreEntity, change: Long, changeType: ChangeType): Long {
        val original = activityScoreEntity.score

        return when (changeType) {
            ChangeType.SET -> {
                activityScoreEntity.score = change.coerceAtLeast(0)
                activityScoreEntity.score - original
            }

            ChangeType.ADD -> {
                activityScoreEntity.score += change
                activityScoreEntity.score - original
            }

            ChangeType.REMOVE -> {
                activityScoreEntity.score -= change
                activityScoreEntity.score = activityScoreEntity.score.coerceAtLeast(0)

                activityScoreEntity.score - original
            }
        }
    }


    private fun persist(activityScoreEntity: ActivityScoreEntity, change: Long, source: String): ActivityScoreEntity {
        registry.summary("user.activity", "source", source, "id", "${activityScoreEntity.id}").record(change.toDouble())

        logRepository.save(ActivityScoreLogEntity(activityScoreEntity, change))
        return if (activityScoreEntity.new) repository.save(activityScoreEntity) else repository.update(activityScoreEntity)
    }

    private fun getOrCreate(id: Long, source: String): ActivityScoreEntity {
        val activity = repository.findByIdAndSource(id, source)
        if (activity != null)
            return activity

        return ActivityScoreEntity(id, source, 0, true)
    }

    private enum class ChangeType {
        SET,
        ADD,
        REMOVE
    }

}
