package com.nanabell.nico.ruby.domain

data class UserActivity(
    val id: Long,
    val score: Long,
    val distribution: Map<String, Long>
) {
    constructor(entities: List<ActivityUserEntity>) : this(
        entities.first().id,
        entities.sumOf { it.score },
        entities.sortedBy { it.source }.associate { it.source to it.score }
    )
}

data class ActivityUserRequest(
    val score: Long,
    val source: String
)

data class ActivityUserDeleteRequest(
    val source: String
)

