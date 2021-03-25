package com.nanabell.nico.kurosawa

import io.micronaut.runtime.Micronaut.build
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import java.lang.management.ManagementFactory

@OpenAPIDefinition(
    info = Info(
        title = "Nico Yazawa Ruby",
        version = "1.1.0",
        description = "Ruby backend for Nico Yazawa"
    )
)
object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        val builder = build()
            .args(*args)
            .packages("com.nanabell.nico.db")
            .defaultEnvironments("prod")

        if (com.nanabell.nico.kurosawa.Application.isDevEnv())
            builder.environments("dev")

        builder.start()
    }

    private fun isDevEnv(): Boolean {
        for (inputArgument in ManagementFactory.getRuntimeMXBean().inputArguments) {
            if (inputArgument.startsWith("-javaagent")
                || inputArgument.startsWith("-agentpath")
                || inputArgument.startsWith("-agentlib")
            )
                return true
        }

        return false
    }
}
