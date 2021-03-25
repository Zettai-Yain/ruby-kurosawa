package com.nanabell.nico.kurosawa.repository

import com.nanabell.nico.kurosawa.entitiy.ActivityScoreLogEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface ActivityScoreLogRepository : CrudRepository<ActivityScoreLogEntity, Long>
