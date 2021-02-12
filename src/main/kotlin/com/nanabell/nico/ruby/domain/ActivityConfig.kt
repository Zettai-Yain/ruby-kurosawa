package com.nanabell.nico.ruby.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.nanabell.nico.ruby.entitiy.ActivityConfigEntity
import org.hibernate.Hibernate

data class ActivityConfig(@JsonIgnore private val self: ActivityConfigEntity) {

    @JsonIgnore
    val id: Int = self.id

    @JsonProperty("min_gain")
    var minGain: Int = self.minGain

    @JsonProperty("max_gain")
    var maxGain: Int = self.maxGain

    fun entity(): ActivityConfigEntity {
        self.minGain = minGain
        self.maxGain = maxGain

        return self
    }

}
