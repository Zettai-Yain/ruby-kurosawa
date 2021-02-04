package com.nanabell.nico.ruby.repository

import com.nanabell.nico.ruby.domain.ActivityScoreLog
import io.micronaut.context.annotation.Requires
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import javax.transaction.Transactional

@Repository
@Requires(notEnv = ["test"])
interface ActivityScoreLogRepository : CrudRepository<ActivityScoreLog, Long>