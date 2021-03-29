package com.nanabell.nico.kurosawa

import io.micronaut.context.annotation.Context
import io.micronaut.runtime.Micronaut.build
import io.micronaut.runtime.event.ApplicationStartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import org.slf4j.LoggerFactory
import java.lang.management.ManagementFactory

@Context
@OpenAPIDefinition(
    info = Info(
        title = "Ruby Kurosawa",
        version = "1.0.0",
        description = "User Activity Store/Configuration Service",
        contact = Contact(name = "Nanabell"),
        license = License(name = "MIT")
    )
)
class Application {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    @EventListener
    fun init(event: ApplicationStartupEvent) {
        val environment = event.source.environment

        environment.getProperty("micronaut.metrics.enabled", Boolean::class.java, false)!!.let {
            when (it) {
                true -> logger.info("Metrics Reporting is enabled, Please ensure you have configured a valid Influx token!")
                false -> logger.warn("Metrics Reporting is disabled! No Metrics will be collected!")
            }
        }
    }

    companion object {

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


}
