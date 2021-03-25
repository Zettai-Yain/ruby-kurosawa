package com.nanabell.nico.kurosawa.controller

import com.nanabell.nico.kurosawa.domain.ActivityScore
import com.nanabell.nico.kurosawa.domain.ActivityScoreDeleteRequest
import com.nanabell.nico.kurosawa.domain.ActivityScoreRequest
import com.nanabell.nico.kurosawa.service.ActivityScoreService
import io.micronaut.data.model.Sort
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import java.util.*

@Tag(name = "Activity Score")
@Controller("/activity/score")
class ActivityScoreController(private val service: ActivityScoreService) {

    private val logger = LoggerFactory.getLogger(ActivityScoreController::class.java)

    @Get("/{?limit,sort}", produces = [MediaType.APPLICATION_JSON])
    fun getAll(
        @QueryValue limit: Optional<Int>,
        @QueryValue sort: Optional<Sort.Order.Direction>
    ): HttpResponse<List<ActivityScore>> {
        return HttpResponse.ok(service.findAll(limit.orElse(0), sort.orElse(null)))
    }

    @Get("/{id}", processes = [MediaType.APPLICATION_JSON])
    fun get(id: Long): HttpResponse<ActivityScore> {
        val activity = service.find(id)
        if (activity == null) {
            logger.warn("Attempted to retrieve Activity for non existing User: $id")
            return HttpResponse.notFound()
        }

        return HttpResponse.ok(activity)
    }

    @Put("/{id}/set", processes = [MediaType.APPLICATION_JSON])
    fun set(id: Long, @Body request: ActivityScoreRequest): HttpResponse<ActivityScore> {
        return HttpResponse.ok(service.setScore(id, request))
    }

    @Put("/{id}/add", processes = [MediaType.APPLICATION_JSON])
    fun add(id: Long, @Body request: ActivityScoreRequest): HttpResponse<ActivityScore> {
        return HttpResponse.ok(service.add(id, request))
    }

    @Put("/{id}/remove", processes = [MediaType.APPLICATION_JSON])
    fun remove(id: Long, @Body request: ActivityScoreRequest): HttpResponse<ActivityScore> {
        return HttpResponse.ok(service.remove(id, request))
    }

    @Delete("/{id}", consumes = [MediaType.APPLICATION_JSON])
    fun delete(id: Long, @Body request: ActivityScoreDeleteRequest?): HttpResponse<Any> {
        val activity = service.find(id)
        if (activity == null) {
            logger.warn("Attempted to delete Activity for non existing User: $id")
            return HttpResponse.notFound()
        }

        service.delete(id, request?.source)
        return HttpResponse.ok()
    }

}
