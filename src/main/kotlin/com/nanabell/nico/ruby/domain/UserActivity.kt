package com.nanabell.nico.ruby.domain

data class UserActivity(
    val id: Long,
    val score: Long,
    val distribution: Map<String, Long>
) {
    constructor(entities: List<UserActivityEntity>) : this(
        entities.first().id,
        entities.sumOf { it.score },
        entities.sortedBy { it.source }.associate { it.source to it.score }
    )
}

data class UserActivityRequest(
    val score: Long,
    val source: String
)

data class UserActivityDeleteRequest(
    val source: String
)

