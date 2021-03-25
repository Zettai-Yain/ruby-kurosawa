package com.nanabell.nico.kurosawa.repository

import com.nanabell.nico.kurosawa.entitiy.ActivityRankEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ActivityRankRepository : JpaRepository<ActivityRankEntity, Int>
