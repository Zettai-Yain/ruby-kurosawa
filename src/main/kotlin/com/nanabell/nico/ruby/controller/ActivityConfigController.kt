package com.nanabell.nico.ruby.controller

import com.nanabell.nico.ruby.domain.ActivityConfig
import com.nanabell.nico.ruby.domain.ActivityConfigPatch
import com.nanabell.nico.ruby.service.ActivityConfigService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.hateoas.JsonError
import org.slf4j.LoggerFactory
import javax.validation.Valid
import javax.validation.ValidationException

@Controller("activity/config")
class ActivityConfigController(private val service: ActivityConfigService) {

    private val logger = LoggerFactory.getLogger(ActivityConfigController::class.java)

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun getAll(): HttpResponse<List<ActivityConfig>> {
        return HttpResponse.ok(service.findAll())
    }

    @Get("{id}", produces = [MediaType.APPLICATION_JSON])
    fun get(id: Long): HttpResponse<ActivityConfig> {
        val config = service.find(id)
        if (config == null) {
            logger.warn("Received ActivityConfig GET Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        return HttpResponse.ok(config)
    }

    @Post(processes = [MediaType.APPLICATION_JSON])
    fun create(@Body config: ActivityConfig): HttpResponse<ActivityConfig> {
        if (service.exists(config.id)) {
            logger.warn("Received ActivityConfig POST Request for already existing id: ${config.id}")
            return HttpResponse.status(HttpStatus.CONFLICT, "Entity already exists!")
        }

        return HttpResponse.created(service.create(config))
    }

    @Patch("{id}", processes = [MediaType.APPLICATION_JSON])
    fun update(id: Long, @Body patch: ActivityConfigPatch): HttpResponse<ActivityConfig> {
        val config = service.find(id)
        if (config == null) {
            logger.warn("Received ActivityConfig PATCH Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        if (patch.isEmpty())
            return HttpResponse.ok(config)

        return HttpResponse.ok(service.patch(config, patch))
    }

    @Delete("{id}", produces = [MediaType.APPLICATION_JSON])
    fun delete(id: Long): HttpResponse<Void> {
        val config = service.find(id)
        if (config == null) {
            logger.warn("Received ActivityConfig DELETE Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        service.delete(config)
        return HttpResponse.ok()
    }

}
