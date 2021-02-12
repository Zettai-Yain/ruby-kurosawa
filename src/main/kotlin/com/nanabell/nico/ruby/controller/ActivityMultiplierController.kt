package com.nanabell.nico.ruby.controller

import com.nanabell.nico.ruby.domain.ActivityMultiplier
import com.nanabell.nico.ruby.domain.ActivityMultiplierAdd
import com.nanabell.nico.ruby.domain.ActivityMultiplierPatch
import com.nanabell.nico.ruby.domain.ActivityMultiplierType
import com.nanabell.nico.ruby.service.ActivityMultiplierService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory

@Tag(name = "Activity Multiplier")
@Controller("activity/multiplier")
class ActivityMultiplierController(private val service: ActivityMultiplierService) {

    private val logger = LoggerFactory.getLogger(ActivityMultiplierController::class.java)

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun getAll(): HttpResponse<List<ActivityMultiplier>> {
        return HttpResponse.ok(service.findAll())
    }

    @Get("{id}", produces = [MediaType.APPLICATION_JSON])
    fun get(id: Long): HttpResponse<List<ActivityMultiplier>> {
        return HttpResponse.ok(service.find(id))
    }

    @Get("channel/{id}", produces = [MediaType.APPLICATION_JSON])
    fun getChannel(id: Long): HttpResponse<ActivityMultiplier> {
        val multiplier = service.find(id, ActivityMultiplierType.CHANNEL)
        if (multiplier == null) {
            logger.warn("Received ActivityMultiplier GET Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        return HttpResponse.ok(multiplier)
    }

    @Get("role/{id}", produces = [MediaType.APPLICATION_JSON])
    fun getRole(id: Long): HttpResponse<ActivityMultiplier> {
        val multiplier = service.find(id, ActivityMultiplierType.ROLE)
        if (multiplier == null) {
            logger.warn("Received ActivityMultiplier GET Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        return HttpResponse.ok(multiplier)
    }

    @Get("user/{id}", produces = [MediaType.APPLICATION_JSON])
    fun getUser(id: Long): HttpResponse<ActivityMultiplier> {
        val multiplier = service.find(id, ActivityMultiplierType.USER)
        if (multiplier == null) {
            logger.warn("Received ActivityMultiplier GET Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        return HttpResponse.ok(multiplier)
    }

    @Post(processes = [MediaType.APPLICATION_JSON])
    fun add(@Body request: ActivityMultiplierAdd): HttpResponse<ActivityMultiplier> {
        return HttpResponse.ok(service.add(request))
    }

    @Patch("{id}", processes = [MediaType.APPLICATION_JSON])
    fun update(id: Long, @Body patch: ActivityMultiplierPatch): HttpResponse<ActivityMultiplier> {
        val multiplier = service.find(id, patch.type)
        if (multiplier == null) {
            logger.warn("Received ActivityMultiplier PATCH Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        return HttpResponse.ok(service.patch(multiplier, patch.multiplier))
    }

    @Delete("{id}", produces = [MediaType.APPLICATION_JSON])
    fun delete(id: Long): HttpResponse<List<ActivityMultiplier>> {
        val multipliers = service.find(id)
        for (multiplier in multipliers) {
            service.delete(multiplier)
        }

        return HttpResponse.ok(multipliers)
    }

    @Delete("channel/{id}", produces = [MediaType.APPLICATION_JSON])
    fun deleteChannel(id: Long): HttpResponse<ActivityMultiplier> {
        val multiplier = service.find(id, ActivityMultiplierType.CHANNEL)
        if (multiplier == null) {
            logger.warn("Received ActivityMultiplier DELETE Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        service.delete(multiplier)
        return HttpResponse.ok(multiplier)
    }

    @Delete("role/{id}", produces = [MediaType.APPLICATION_JSON])
    fun deleteRole(id: Long): HttpResponse<ActivityMultiplier> {
        val multiplier = service.find(id, ActivityMultiplierType.ROLE)
        if (multiplier == null) {
            logger.warn("Received ActivityMultiplier DELETE Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        service.delete(multiplier)
        return HttpResponse.ok(multiplier)
    }

    @Delete("user/{id}", produces = [MediaType.APPLICATION_JSON])
    fun deleteUser(id: Long): HttpResponse<ActivityMultiplier> {
        val multiplier = service.find(id, ActivityMultiplierType.USER)
        if (multiplier == null) {
            logger.warn("Received ActivityMultiplier DELETE Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        service.delete(multiplier)
        return HttpResponse.ok(multiplier)
    }
}