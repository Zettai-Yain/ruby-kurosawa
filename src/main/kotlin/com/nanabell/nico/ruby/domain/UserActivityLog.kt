package com.nanabell.nico.ruby.domain

import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.persistence.*

@Entity
@Table(name = "user_activity_log")

data class UserActivityLog(

    @Id
    @Column(name = "seq")
    @GeneratedValue(strategy = GenerationType.TABLE)
    var seq: Long = 0,

    @Column(name = "time")
    var time: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),

    @Column(name = "id")
    var id: Long,

    @Column(name = "source")
    var source: String,

    @Column(name = "score")
    var score: Long

) {
    constructor(activity: UserActivityEntity, change: Long) : this(0, OffsetDateTime.now(ZoneOffset.UTC), activity.id, activity.source, change)
}
