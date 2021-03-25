package com.nanabell.nico.kurosawa.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class ActivityMultiplierAdd(

    @JsonProperty("id")
    val id: Long,

    @JsonProperty("type")
    val type: ActivityMultiplierType,

    @JsonProperty("multiplier")
    var multiplier: Float
)
