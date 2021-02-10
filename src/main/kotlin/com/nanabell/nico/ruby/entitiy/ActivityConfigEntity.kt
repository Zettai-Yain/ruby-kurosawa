package com.nanabell.nico.ruby.entitiy

import com.nanabell.nico.ruby.domain.ActivityConfig
import javax.persistence.*

@Entity
@Table(name = "activity_config")
data class ActivityConfigEntity(

    @Id
    @Column(name = "id")
    val id: Long,

    @Column(name = "min_gain")
    var minGain: Int = 3,

    @Column(name = "max_gain")
    var maxGain: Int = 5,

    @Column(name = "activity_rank_id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    val rankId: Int

) {
    fun domain() = ActivityConfig(this)
}
