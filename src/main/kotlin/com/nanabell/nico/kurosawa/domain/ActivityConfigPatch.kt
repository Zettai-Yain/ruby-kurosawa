package com.nanabell.nico.kurosawa.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class ActivityConfigPatch(

    @JsonProperty("min_gain")
    var minGain: Int? = null,

    @JsonProperty("max_gain")
    var maxGain: Int? = null

) {
    @JsonIgnore
    fun isEmpty() = minGain == null && maxGain == null
}
