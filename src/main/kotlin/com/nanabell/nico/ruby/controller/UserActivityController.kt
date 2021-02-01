package com.nanabell.nico.ruby.controller

import com.nanabell.nico.ruby.domain.UserActivity
import com.nanabell.nico.ruby.domain.UserActivityDeleteRequest
import com.nanabell.nico.ruby.domain.UserActivityRequest
import com.nanabell.nico.ruby.service.UserActivityService
import io.micronaut.data.model.Sort
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import java.util.*

@Tag(name = "Activity", description = "Activity Controller")
@Controller("/activity")
@Secured(SecurityRule.IS_AUTHENTICATED)
class UserActivityController(private val service: UserActivityService) {

    private val logger = LoggerFactory.getLogger(UserActivityController::class.java)

    @Get("/{?limit,sort}", produces = [MediaType.APPLICATION_JSON])
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
    fun set(id: Long, @Body request: UserActivityRequest): HttpResponse<UserActivity> {
        return HttpResponse.ok(service.setScore(id, request))
    }

    @Put("/{id}/add", processes = [MediaType.APPLICATION_JSON])
    fun add(id: Long, @Body request: UserActivityRequest): HttpResponse<UserActivity> {
        return HttpResponse.ok(service.add(id, request))
    }

    @Put("/{id}/remove", processes = [MediaType.APPLICATION_JSON])
    fun remove(id: Long, @Body request: UserActivityRequest): HttpResponse<UserActivity> {
        return HttpResponse.ok(service.remove(id, request))
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

}
