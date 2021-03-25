package com.nanabell.nico.kurosawa.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.nanabell.nico.kurosawa.entitiy.ActivityMultiplierEntity

data class ActivityMultiplier(@JsonIgnore private val entity: ActivityMultiplierEntity) {

    @JsonProperty("id")
    val id: Long = entity.id

    @JsonProperty("type")
    val type: ActivityMultiplierType = entity.type

    @JsonProperty("multiplier")
    var multiplier: Float = entity.multiplier

    fun entity(): ActivityMultiplierEntity {
        entity.multiplier = multiplier

        return entity
    }
}
