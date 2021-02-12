package com.nanabell.nico.ruby.repository

import com.nanabell.nico.ruby.entitiy.ActivityScoreLogEntity
import io.micronaut.context.annotation.Requires
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface ActivityScoreLogRepository : CrudRepository<ActivityScoreLogEntity, Long>