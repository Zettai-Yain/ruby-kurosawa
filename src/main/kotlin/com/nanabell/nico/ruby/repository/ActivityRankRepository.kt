package com.nanabell.nico.ruby.repository

import com.nanabell.nico.ruby.entitiy.ActivityRankEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ActivityRankRepository : JpaRepository<ActivityRankEntity, Int>