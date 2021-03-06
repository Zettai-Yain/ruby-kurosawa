package com.nanabell.nico.kurosawa.repository

import com.nanabell.nico.kurosawa.entitiy.ActivityConfigEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ActivityConfigRepository : JpaRepository<ActivityConfigEntity, Long>
