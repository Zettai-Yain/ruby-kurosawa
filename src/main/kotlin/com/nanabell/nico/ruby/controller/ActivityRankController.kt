package com.nanabell.nico.ruby.controller

import com.nanabell.nico.ruby.domain.ActivityRank
import com.nanabell.nico.ruby.domain.ActivityRankAdd
import com.nanabell.nico.ruby.domain.ActivityRankPatch
import com.nanabell.nico.ruby.service.ActivityConfigService
import com.nanabell.nico.ruby.service.ActivityRankService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import org.slf4j.LoggerFactory

@Controller("activity/rank")
class ActivityRankController(
    private val configService: ActivityConfigService,
    private val rankService: ActivityRankService
    ) {

    private val logger = LoggerFactory.getLogger(ActivityRankController::class.java)

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun getAll(): HttpResponse<List<ActivityRank>> {
        return HttpResponse.ok(rankService.findAll())
    }

    @Get("{id}", produces = [MediaType.APPLICATION_JSON])
    fun get(id: Int): HttpResponse<ActivityRank> {
        val rank = rankService.find(id)
        if (rank == null) {
            logger.warn("Received ActivityRank GET Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        return HttpResponse.ok(rank)
    }

    @Get("guild", produces = [MediaType.APPLICATION_JSON])
    fun getAllGuild(): HttpResponse<Map<Long, List<ActivityRank>>> {
        return HttpResponse.ok(rankService.findAllGuild())
    }


    @Get("guild/{guild}", produces = [MediaType.APPLICATION_JSON])
    fun getGuild(guild: Long): HttpResponse<List<ActivityRank>> {
        return HttpResponse.ok(rankService.findGuild(guild))
    }



    @Post(processes = [MediaType.APPLICATION_JSON])
    fun add(request: ActivityRankAdd): HttpResponse<ActivityRank> {
        val config = configService.find(request.guild)
        if (config == null) {
            logger.warn("Received ActivityRank GET Request for unknown guild: ${request.guild}")
            return HttpResponse.notFound()
        }

        return HttpResponse.created(rankService.add(config, request))
    }

    @Patch("{id}", processes = [MediaType.APPLICATION_JSON])
    fun update(id: Int, @Body patch: ActivityRankPatch): HttpResponse<ActivityRank> {
        val rank = rankService.find(id)
        if (rank == null) {
            logger.warn("Received ActivityRank GET Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        return HttpResponse.ok(rankService.patch(rank, patch))
    }

    @Delete("{guild}/{id}")
    fun delete(guild: Long, id: Int): HttpResponse<Void> {
        val rank = rankService.find(id)
        if (rank == null) {
            logger.warn("Received ActivityRank GET Request for unknown id: $id")
            return HttpResponse.notFound()
        }

        rankService.delete(rank)
        return HttpResponse.ok()
    }

}
