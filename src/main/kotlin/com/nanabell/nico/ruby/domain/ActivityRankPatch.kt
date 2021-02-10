package com.nanabell.nico.ruby.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class ActivityRankPatch(

    @JsonProperty("role_id")
    var roleId: Long? = null,

    @JsonProperty("score")
    var score: Long? = null

) {
    fun isEmpty() = roleId == null && score == null
}
