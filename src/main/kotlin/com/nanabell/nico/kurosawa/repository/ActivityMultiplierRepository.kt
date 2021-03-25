package com.nanabell.nico.kurosawa.repository

import com.nanabell.nico.kurosawa.domain.ActivityMultiplierType
import com.nanabell.nico.kurosawa.entitiy.ActivityMultiplierEntity
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ActivityMultiplierRepository : JpaRepository<ActivityMultiplierEntity, ActivityMultiplierEntity.ActivityMultiplierId> {

    fun findById(id: Long): List<ActivityMultiplierEntity>

    @Query("SELECT e FROM ActivityMultiplierEntity e WHERE e.id = :id AND e.type = :type")
    fun findByIdAndType(id: Long, type: ActivityMultiplierType): ActivityMultiplierEntity?

}
