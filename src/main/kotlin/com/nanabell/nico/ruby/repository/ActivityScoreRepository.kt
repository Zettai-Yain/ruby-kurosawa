package com.nanabell.nico.ruby.repository

import com.nanabell.nico.ruby.entitiy.ActivityScoreEntity
import io.micronaut.context.annotation.Requires
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.data.model.Sort
import org.hibernate.jpa.QueryHints
import java.math.BigInteger
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Repository
@Transactional
@Requires(notEnv = ["test"])
abstract class ActivityScoreRepository(private val entityManager: EntityManager) : JpaRepository<ActivityScoreEntity, ActivityScoreEntity.ActivityScoreId> {

    @Query("SELECT entity FROM ActivityScoreEntity entity WHERE entity.id = :id", readOnly = true)
    abstract fun findAllById(id: Long): List<ActivityScoreEntity>

    @Query("SELECT entity FROM ActivityScoreEntity entity WHERE entity.id = :id AND entity.source = :source", readOnly = true)
    abstract fun findByIdAndSource(id: Long, source: String): ActivityScoreEntity?

    fun findAll(limit: Int = 0, direction: Sort.Order.Direction? = null): List<ActivityScoreEntity> {
        var query = "SELECT entity.id, entity.source, entity.score FROM activity_score entity"

        if (limit > 0) {
            query += " WHERE entity.id IN (SELECT id FROM activity_score GROUP BY id"

            if (direction != null)
                query += " ORDER BY SUM(score) $direction"

            query += " LIMIT $limit)"
        }

        query += if (direction != null) {
            " ORDER BY (SELECT sum(score) FROM activity_score WHERE id = entity.id GROUP BY id) $direction"
        } else {
            " ORDER BY entity.id"
        }

        query += ";"

        @Suppress("UNCHECKED_CAST")
        val rows: List<Array<Any>> = entityManager.createNativeQuery(query)
            .setHint(QueryHints.HINT_READONLY, true)
            .resultList as List<Array<Any>>

        val list = arrayListOf<ActivityScoreEntity>()
        for (row in rows) {
            list.add(
                ActivityScoreEntity(
                    (row[0] as BigInteger).toLong(),
                    row[1] as String,
                    (row[2] as BigInteger).toLong()
                )
            )
        }

        return list
    }

    @Query("DELETE FROM ActivityScoreEntity WHERE id = :id")
    abstract fun deleteById(id: Long)

    @Query("DELETE FROM ActivityScoreEntity WHERE id = :id AND source = :source")
    abstract fun deleteByIdAndSource(id: Long, source: String)
}