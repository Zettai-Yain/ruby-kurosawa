package com.nanabell.nico.kurosawa.entitiy

import com.nanabell.nico.kurosawa.domain.ActivityConfig
import javax.persistence.*

@Entity
@Table(name = "activity_config")
data class ActivityConfigEntity(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(name = "min_gain")
    var minGain: Int = 3,

    @Column(name = "max_gain")
    var maxGain: Int = 5,

) {
    fun domain() = ActivityConfig(this)
}
