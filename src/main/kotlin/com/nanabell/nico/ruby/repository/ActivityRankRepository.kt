package com.nanabell.nico.ruby.repository

import com.nanabell.nico.ruby.entitiy.ActivityRankEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import org.hibernate.jpa.QueryHints
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Repository
@Transactional
abstract class ActivityRankRepository(private val entityManager: EntityManager) :
    JpaRepository<ActivityRankEntity, Int> {


    fun findAllByGuild(): Map<Long, List<ActivityRankEntity>> {
        val query = entityManager.createQuery("""
            SELECT c.id, r
            FROM ActivityRankEntity r
            JOIN ActivityConfigEntity c ON r.rankId = c.rankId
        """.trimIndent()
        )

        query.setHint(QueryHints.HINT_READONLY, true)

        @Suppress("UNCHECKED_CAST")
        val rows = query.resultList as List<Array<Any>>

        val list = arrayListOf<Pair<Long, ActivityRankEntity>>()
        for (row in rows) {
            list.add(row[0] as Long to row[1] as ActivityRankEntity)
        }

        return list.groupBy({ it.first }, { it.second })
    }

    fun findByGuild(guild: Long): List<ActivityRankEntity> {
        val query = entityManager.createQuery("""
            SELECT r
            FROM ActivityRankEntity r
            JOIN ActivityConfigEntity c ON r.rankId = c.rankId
            WHERE c.id = :guild
        """.trimIndent(), ActivityRankEntity::class.java
        )

        query.setParameter("guild", guild)
        query.setHint(QueryHints.HINT_READONLY, true)

        return query.resultList
    }

}