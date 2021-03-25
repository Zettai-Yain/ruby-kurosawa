package com.nanabell.nico.kurosawa.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class ActivityRankPatch(

    @JsonProperty("role_id")
    var roleId: Long? = null,

    @JsonProperty("score")
    var score: Long? = null

) {
    @JsonIgnore
    fun isEmpty() = roleId == null && score == null
}
