package com.nanabell.nico.ruby

import io.micronaut.runtime.Micronaut.build
import java.lang.management.ManagementFactory

object Application  {

    @JvmStatic
    fun main(args: Array<String>) {
        val builder = build()
            .args(*args)
            .packages("com.nanabell.nico.db")
            .defaultEnvironments("prod")

        if (isDevEnv())
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


