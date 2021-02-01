package com.nanabell.nico.ruby.service

import com.nanabell.nico.ruby.domain.UserActivity
import com.nanabell.nico.ruby.domain.UserActivityEntity
import com.nanabell.nico.ruby.domain.UserActivityLog
import com.nanabell.nico.ruby.domain.UserActivityRequest
import com.nanabell.nico.ruby.repository.UserActivityLogRepository
import com.nanabell.nico.ruby.repository.UserActivityRepository
import io.micrometer.core.instrument.MeterRegistry
import io.micronaut.data.model.Sort
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class UserActivityService(
    private val repository: UserActivityRepository,
    private val logRepository: UserActivityLogRepository,
    private val registry: MeterRegistry
) {

    private val logger = LoggerFactory.getLogger(UserActivityService::class.java)

    fun find(id: Long): UserActivity? {
        val entity = repository.findAllById(id)
        return if (entity.isNotEmpty()) UserActivity(entity) else null
    }

    fun findAll(limit: Int, direction: Sort.Order.Direction?): List<UserActivity> {
        return repository.findAll(limit.coerceAtLeast(0), direction).groupBy { it.id }.map { UserActivity(it.value) }
    }

    fun setScore(id: Long, request: UserActivityRequest): UserActivity {
        val activity = getOrCreate(id, request.source)

        val change = getChange(activity, request.score, ChangeType.SET)
        logger.info("Setting ${request.source} Score of User $id to ${request.score}")
        return persist(activity, change, request.source).let { find(id)!! }
    }

    fun add(id: Long, request: UserActivityRequest): UserActivity {
        val activity = getOrCreate(id, request.source)

        val change = getChange(activity, request.score, ChangeType.ADD)
        logger.info("Adding ${request.score} to ${request.source} Score of User $id")
        return persist(activity, change, request.source).let { find(id)!! }
    }

    fun remove(id: Long, request: UserActivityRequest): UserActivity {
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

    private fun getChange(activityEntity: UserActivityEntity, change: Long, changeType: ChangeType): Long {
        val original = activityEntity.score

        return when (changeType) {
            ChangeType.SET -> {
                activityEntity.score = change.coerceAtLeast(0)
                activityEntity.score - original
            }

            ChangeType.ADD -> {
                activityEntity.score += change
                activityEntity.score - original
            }

            ChangeType.REMOVE -> {
                activityEntity.score -= change
                activityEntity.score = activityEntity.score.coerceAtLeast(0)

                activityEntity.score - original
            }
        }
    }


    private fun persist(activityEntity: UserActivityEntity, change: Long, source: String): UserActivityEntity {
        registry.summary("user.activity", "source", source, "id", "${activityEntity.id}").record(change.toDouble())

        logRepository.save(UserActivityLog(activityEntity, change))
        return if (activityEntity.new) repository.save(activityEntity) else repository.update(activityEntity)
    }

    private fun getOrCreate(id: Long, source: String): UserActivityEntity {
        val activity = repository.findByIdAndSource(id, source)
        if (activity != null)
            return activity

        return UserActivityEntity(id, source, 0, true)
    }

    private enum class ChangeType {
        SET,
        ADD,
        REMOVE
    }

}
