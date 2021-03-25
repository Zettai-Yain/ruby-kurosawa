package com.nanabell.nico.kurosawa.entitiy

import com.nanabell.nico.kurosawa.domain.ActivityRank
import javax.persistence.*

@Entity
@Table(name = "activity_rank")
data class ActivityRankEntity(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(name = "role_id")
    var roleId: Long,

    @Column(name = "score")
    var score: Long

) {
    fun domain() = ActivityRank(this)
}
