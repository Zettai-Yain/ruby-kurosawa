package com.nanabell.nico.ruby.controller

import com.nanabell.nico.ruby.domain.ActivityRank
import com.nanabell.nico.ruby.domain.ActivityRankAdd
import com.nanabell.nico.ruby.domain.ActivityRankPatch
import com.nanabell.nico.ruby.service.ActivityRankService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory

@Tag(name = "Activity Rank")
@Controller("activity/rank")
class ActivityRankController(private val service: ActivityRankService) {

    private val logger = LoggerFactory.getLogger(ActivityRankController::class.java)

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun getAll(): HttpResponse<List<ActivityRank>> {
        return HttpResponse.ok(service.findAll())
    }

    @Get("{id}", produces = [MediaType.APPLICATION_JSON])
    fun get(id: Int): HttpResponse<ActivityRank> {
        val rank = service.find(id)
        if (rank == null) {
            logger.warn("Received ActivityRank GET Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        return HttpResponse.ok(rank)
    }

    @Post(processes = [MediaType.APPLICATION_JSON])
    fun add(@Body request: ActivityRankAdd): HttpResponse<ActivityRank> {
        return HttpResponse.created(service.add(request))
    }

    @Patch("{id}", processes = [MediaType.APPLICATION_JSON])
    fun update(id: Int, @Body patch: ActivityRankPatch): HttpResponse<ActivityRank> {
        val rank = service.find(id)
        if (rank == null) {
            logger.warn("Received ActivityRank PATCH Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        return if (patch.isEmpty()) HttpResponse.ok(rank) else HttpResponse.ok(service.patch(rank, patch))
    }

    @Delete("{id}")
    fun delete(id: Int): HttpResponse<Void> {
        val rank = service.find(id)
        if (rank == null) {
            logger.warn("Received ActivityRank DELETE Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        service.delete(rank)
        return HttpResponse.ok()
    }

}
