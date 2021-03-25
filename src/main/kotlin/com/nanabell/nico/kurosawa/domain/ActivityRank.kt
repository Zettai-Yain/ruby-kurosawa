package com.nanabell.nico.kurosawa.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.nanabell.nico.kurosawa.entitiy.ActivityRankEntity

data class ActivityRank(@JsonIgnore private val self: ActivityRankEntity) {

    @JsonIgnore
    val id: Int = self.id

    @JsonProperty("role_id")
    var roleId: Long = self.roleId

    @JsonProperty("score")
    var score: Long = self.score

    fun entity(): ActivityRankEntity {
        self.roleId = roleId
        self.score = score

        return self
    }
}
