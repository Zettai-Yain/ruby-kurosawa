package com.nanabell.nico.ruby.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.nanabell.nico.ruby.entitiy.ActivityConfigEntity
import org.hibernate.Hibernate

data class ActivityConfig(@JsonIgnore private val self: ActivityConfigEntity) {

    @JsonProperty("id", required = true)
    val id: Long = self.id

    @JsonProperty("min_gain", required = false, defaultValue = "3")
    var minGain: Int = self.minGain

    @JsonProperty("max_gain", required = false, defaultValue = "5")
    var maxGain: Int = self.maxGain

    val rankId: Int = self.rankId

    fun entity(): ActivityConfigEntity {
        self.minGain = minGain
        self.maxGain = maxGain

        return self
    }

}
