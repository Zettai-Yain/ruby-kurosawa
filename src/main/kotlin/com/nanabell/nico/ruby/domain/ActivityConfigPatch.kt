package com.nanabell.nico.ruby.domain

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import javax.annotation.Nullable
import javax.validation.constraints.Min

data class ActivityConfigPatch(

    @JsonProperty("min_gain")
    var minGain: Int? = null,

    @JsonProperty("max_gain")
    var maxGain: Int? = null

) {
    fun isEmpty() = minGain == null && maxGain == null
}
