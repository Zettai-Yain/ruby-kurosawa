package com.nanabell.nico.ruby.domain

data class ActivityScore(
    val id: Long,
    val score: Long,
    val distribution: Map<String, Long>
) {
    constructor(entities: List<ActivityScoreEntity>) : this(
        entities.first().id,
        entities.sumOf { it.score },
        entities.sortedBy { it.source }.associate { it.source to it.score }
    )
}

data class ActivityScoreRequest(
    val score: Long,
    val source: String
)

data class ActivityScoreDeleteRequest(
    val source: String
)

