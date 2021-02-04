package com.nanabell.nico.ruby.repository

import com.nanabell.nico.ruby.domain.ActivityUserLog
import io.micronaut.context.annotation.Requires
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import javax.transaction.Transactional

@Transactional
@Requires(notEnv = ["test"])
@JdbcRepository(dialect = Dialect.POSTGRES)
interface ActivityUserLogRepository : CrudRepository<ActivityUserLog, Long>