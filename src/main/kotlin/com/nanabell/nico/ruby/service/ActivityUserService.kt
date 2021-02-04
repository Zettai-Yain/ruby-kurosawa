package com.nanabell.nico.ruby.service

import com.nanabell.nico.ruby.domain.UserActivity
import com.nanabell.nico.ruby.domain.ActivityUserEntity
import com.nanabell.nico.ruby.domain.ActivityUserLog
import com.nanabell.nico.ruby.domain.ActivityUserRequest
import com.nanabell.nico.ruby.repository.ActivityUserLogRepository
import com.nanabell.nico.ruby.repository.ActivityUserRepository
import io.micrometer.core.instrument.MeterRegistry
import io.micronaut.data.model.Sort
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ActivityUserService(
    private val repository: ActivityUserRepository,
    private val logRepository: ActivityUserLogRepository,
    private val registry: MeterRegistry
) {

    private val logger = LoggerFactory.getLogger(ActivityUserService::class.java)

    fun find(id: Long): UserActivity? {
        val entity = repository.findAllById(id)
        return if (entity.isNotEmpty()) UserActivity(entity) else null
    }

    fun findAll(limit: Int, direction: Sort.Order.Direction?): List<UserActivity> {
        return repository.findAll(limit.coerceAtLeast(0), direction).groupBy { it.id }.map { UserActivity(it.value) }
    }

    fun setScore(id: Long, request: ActivityUserRequest): UserActivity {
        val activity = getOrCreate(id, request.source)

        val change = getChange(activity, request.score, ChangeType.SET)
        logger.info("Setting ${request.source} Score of User $id to ${request.score}")
        return persist(activity, change, request.source).let { find(id)!! }
    }

    fun add(id: Long, request: ActivityUserRequest): UserActivity {
        val activity = getOrCreate(id, request.source)

        val change = getChange(activity, request.score, ChangeType.ADD)
        logger.info("Adding ${request.score} to ${request.source} Score of User $id")
        return persist(activity, change, request.source).let { find(id)!! }
    }

    fun remove(id: Long, request: ActivityUserRequest): UserActivity {
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

    private fun getChange(activityUserEntity: ActivityUserEntity, change: Long, changeType: ChangeType): Long {
        val original = activityUserEntity.score

        return when (changeType) {
            ChangeType.SET -> {
                activityUserEntity.score = change.coerceAtLeast(0)
                activityUserEntity.score - original
            }

            ChangeType.ADD -> {
                activityUserEntity.score += change
                activityUserEntity.score - original
            }

            ChangeType.REMOVE -> {
                activityUserEntity.score -= change
                activityUserEntity.score = activityUserEntity.score.coerceAtLeast(0)

                activityUserEntity.score - original
            }
        }
    }


    private fun persist(activityUserEntity: ActivityUserEntity, change: Long, source: String): ActivityUserEntity {
        registry.summary("user.activity", "source", source, "id", "${activityUserEntity.id}").record(change.toDouble())

        logRepository.save(ActivityUserLog(activityUserEntity, change))
        return if (activityUserEntity.new) repository.save(activityUserEntity) else repository.update(activityUserEntity)
    }

    private fun getOrCreate(id: Long, source: String): ActivityUserEntity {
        val activity = repository.findByIdAndSource(id, source)
        if (activity != null)
            return activity

        return ActivityUserEntity(id, source, 0, true)
    }

    private enum class ChangeType {
        SET,
        ADD,
        REMOVE
    }

}
