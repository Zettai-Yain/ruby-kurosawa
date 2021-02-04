package com.nanabell.nico.ruby.entitiy

import javax.persistence.*

@Entity
@Table(name = "activity_rank")
data class ActivityRankEntity(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    val id: Int,

    @JoinColumn(name = "activity_rank_id")
    @ManyToOne(targetEntity = ActivityConfigEntity::class)
    val config: ActivityConfigEntity,

    @Column(name = "role_id")
    var roleId: Long,

    @Column(name = "score")
    var score: Long

)
