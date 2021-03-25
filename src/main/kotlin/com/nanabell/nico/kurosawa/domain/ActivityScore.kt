package com.nanabell.nico.kurosawa.domain

import com.nanabell.nico.kurosawa.entitiy.ActivityScoreEntity

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
