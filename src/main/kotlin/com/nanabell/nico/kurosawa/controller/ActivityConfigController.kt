package com.nanabell.nico.kurosawa.controller

import com.nanabell.nico.kurosawa.domain.ActivityConfig
import com.nanabell.nico.kurosawa.domain.ActivityConfigPatch
import com.nanabell.nico.kurosawa.service.ActivityConfigService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Patch
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Activity Config")
@Controller("activity/config")
class ActivityConfigController(private val service: ActivityConfigService) {

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun get(): HttpResponse<ActivityConfig> {
        return HttpResponse.ok(service.get())
    }

    @Patch(processes = [MediaType.APPLICATION_JSON])
    fun update(@Body patch: ActivityConfigPatch): HttpResponse<ActivityConfig> {
        val config = service.get()

        return if (patch.isEmpty()) HttpResponse.ok(config) else HttpResponse.ok(service.patch(config, patch))
    }
}
