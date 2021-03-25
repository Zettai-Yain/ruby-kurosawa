package com.nanabell.nico.kurosawa.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class ActivityMultiplierPatch(

    @JsonProperty("type")
    val type: ActivityMultiplierType,

    @JsonProperty("multiplier")
    val multiplier: Float

)
