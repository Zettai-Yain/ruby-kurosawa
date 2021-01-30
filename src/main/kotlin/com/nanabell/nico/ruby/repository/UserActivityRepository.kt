package com.nanabell.nico.ruby.repository

import com.nanabell.nico.ruby.domain.UserActivityEntity
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
abstract class UserActivityRepository(private val entityManager: EntityManager) :
    JpaRepository<UserActivityEntity, UserActivityEntity.UserActivityId> {

    @Query("SELECT entity FROM UserActivityEntity entity WHERE entity.id = :id", readOnly = true)
    abstract fun findAllById(id: Long): List<UserActivityEntity>

    @Query("SELECT entity FROM UserActivityEntity entity WHERE entity.id = :id AND entity.source = :source", readOnly = true)
    abstract fun findByIdAndSource(id: Long, source: String): UserActivityEntity?

    fun findAll(limit: Int = 0, direction: Sort.Order.Direction? = null): List<UserActivityEntity> {
        var query = "SELECT entity.id, entity.source, entity.score FROM user_activity entity"

        if (limit > 0) {
            query += " WHERE entity.id IN (SELECT id FROM user_activity GROUP BY id"

            if (direction != null)
                query += " ORDER BY SUM(score) $direction"

            query += " LIMIT $limit)"
        }

        query += if (direction != null) {
            " ORDER BY (SELECT sum(score) FROM user_activity WHERE id = entity.id GROUP BY id) $direction"
        } else {
            " ORDER BY entity.id"
        }

        query += ";"

        @Suppress("UNCHECKED_CAST")
        val rows: List<Array<Any>> = entityManager.createNativeQuery(query)
            .setHint(QueryHints.HINT_READONLY, true)
            .resultList as List<Array<Any>>

        val list = arrayListOf<UserActivityEntity>()
        for (row in rows) {
            list.add(
                UserActivityEntity(
                    (row[0] as BigInteger).toLong(),
                    row[1] as String,
                    (row[2] as BigInteger).toLong()
                )
            )
        }

        return list
    }

    @Query("DELETE FROM UserActivityEntity WHERE id = :id")
    abstract fun deleteById(id: Long)

    @Query("DELETE FROM UserActivityEntity WHERE id = :id AND source = :source")
    abstract fun deleteByIdAndSource(id: Long, source: String)
}