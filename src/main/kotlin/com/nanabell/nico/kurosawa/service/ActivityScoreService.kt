package com.nanabell.nico.kurosawa.service

import com.nanabell.nico.kurosawa.domain.ActivityScore
import com.nanabell.nico.kurosawa.domain.ActivityScoreRequest
import com.nanabell.nico.kurosawa.entitiy.ActivityScoreEntity
import com.nanabell.nico.kurosawa.repository.ActivityScoreRepository
import io.micrometer.core.instrument.MeterRegistry
import io.micronaut.data.model.Sort
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ActivityScoreService(
    private val repository: ActivityScoreRepository,
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
        activity.score = request.score

        logger.info("Setting ${request.source} Score of User $id to ${request.score}")
        return persist(activity).let { find(id)!! }
    }

    fun add(id: Long, request: ActivityScoreRequest): ActivityScore {
        val activity = getOrCreate(id, request.source)
        val change = getChange(activity, request.score, ChangeType.ADD)

        logger.info("Adding ${request.score} to ${request.source} Score of User $id")
        registry.summary("user.activity.gain", "source", request.source, "id", "$id").record(change.toDouble())
        return persist(activity).let { find(id)!! }
    }

    fun remove(id: Long, request: ActivityScoreRequest): ActivityScore {
        val activity = getOrCreate(id, request.source)
        val change = getChange(activity, request.score, ChangeType.REMOVE)

        logger.info("Removing ${request.score} to ${request.source} Score of User $id")
        registry.summary("user.activity.loss", "source", request.source, "id", "$id").record(change.toDouble())
        return persist(activity).let { find(id)!! }
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

    private fun getChange(entity: ActivityScoreEntity, change: Long, changeType: ChangeType): Long {
        val original = entity.score

        return when (changeType) {
            ChangeType.ADD -> {
                entity.score += change

                entity.score - original // return
            }

            ChangeType.REMOVE -> {
                entity.score -= change
                entity.score = entity.score.coerceAtLeast(0)

                entity.score - original // return
            }
        }
    }


    private fun persist(activityScoreEntity: ActivityScoreEntity): ActivityScoreEntity {
        return if (activityScoreEntity.new) repository.save(activityScoreEntity) else repository.update(activityScoreEntity)
        // TODO: Atomic Database Updates!
    }

    private fun getOrCreate(id: Long, source: String): ActivityScoreEntity {
        val activity = repository.findByIdAndSource(id, source)
        if (activity != null)
            return activity

        return ActivityScoreEntity(id, source, 0, true)
    }

    private enum class ChangeType {
        ADD,
        REMOVE
    }

}
