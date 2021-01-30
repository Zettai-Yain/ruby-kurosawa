package com.nanabell.nico.ruby.controller

import com.nanabell.nico.ruby.domain.UserActivity
import com.nanabell.nico.ruby.domain.UserActivityDeleteRequest
import com.nanabell.nico.ruby.domain.UserActivityEntity
import com.nanabell.nico.ruby.domain.UserActivityRequest
import com.nanabell.nico.ruby.service.UserActivityService
import io.micronaut.data.model.Sort
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.slf4j.LoggerFactory
import java.util.*
import javax.annotation.security.PermitAll

@Controller("/activity")
@Secured(SecurityRule.IS_AUTHENTICATED)
class UserActivityController(private val service: UserActivityService) {

    private val logger = LoggerFactory.getLogger(UserActivityController::class.java)

    @Get("/{?limit,sort}")
    fun getAll(
        @QueryValue limit: Optional<Int>,
        @QueryValue sort: Optional<Sort.Order.Direction>
    ): HttpResponse<List<UserActivity>> {
        return HttpResponse.ok(service.findAll(limit.orElse(0), sort.orElse(null)))
    }

    @Get("/{id}", processes = [MediaType.APPLICATION_JSON])
    fun get(id: Long): HttpResponse<UserActivity> {
        val activity = service.find(id)
        if (activity == null) {
            logger.warn("Attempted to retrieve Activity for non existing User: $id")
            return HttpResponse.notFound()
        }

        return HttpResponse.ok(activity)
    }

    @Put("/{id}/set", processes = [MediaType.APPLICATION_JSON])
    fun set(id: Long, @Body request: UserActivityRequest): HttpResponse<UserActivityEntity> {
        val activity = service.setScore(id, request)

        return getActivityResponse(activity)
    }

    @Put("/{id}/add", processes = [MediaType.APPLICATION_JSON])
    fun add(id: Long, @Body request: UserActivityRequest): HttpResponse<UserActivityEntity> {
        val activity = service.add(id, request)

        return getActivityResponse(activity)
    }

    @Put("/{id}/remove", processes = [MediaType.APPLICATION_JSON])
    fun remove(id: Long, @Body request: UserActivityRequest): HttpResponse<UserActivityEntity> {
        val activity = service.remove(id, request)

        return getActivityResponse(activity)
    }

    @Delete("/{id}", consumes = [MediaType.APPLICATION_JSON])
    fun delete(id: Long, @Body request: UserActivityDeleteRequest?): HttpResponse<Any> {
        val activity = service.find(id)
        if (activity == null) {
            logger.warn("Attempted to delete Activity for non existing User: $id")
            return HttpResponse.notFound()
        }

        service.delete(id, request?.source)
        return HttpResponse.ok()
    }

    private fun getActivityResponse(activityEntity: UserActivityEntity): HttpResponse<UserActivityEntity> =
        if (activityEntity.new) HttpResponse.created(activityEntity) else HttpResponse.ok(activityEntity)

}
