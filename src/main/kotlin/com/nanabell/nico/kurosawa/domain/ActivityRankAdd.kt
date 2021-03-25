package com.nanabell.nico.kurosawa.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class ActivityRankAdd(

    @JsonProperty("role_id", required = true)
    val roleId: Long,

    @JsonProperty("score", required = true)
    val score: Long

)
