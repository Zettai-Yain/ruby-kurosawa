package com.nanabell.nico.ruby.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.nanabell.nico.ruby.entitiy.ActivityConfigEntity
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.Validated
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class ActivityConfig(

    @JsonProperty("id", required = true)
    val id: Long,

    @JsonProperty("min_gain", required = false, defaultValue = "3")
    var minGain: Int,

    @JsonProperty("max_gain", required = false, defaultValue = "5")
    var maxGain: Int
) {
    constructor(entity: ActivityConfigEntity) : this(entity.id, entity.minGain, entity.maxGain)
}
