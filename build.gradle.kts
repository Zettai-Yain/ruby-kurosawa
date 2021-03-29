plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    id("org.jetbrains.kotlin.kapt") version "1.4.31"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.31"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.4.31"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("io.micronaut.application") version "1.3.4"
}

version = "1.2.0"
group = "com.nanabell.nico.kurosawa"

val kotlinVersion = project.properties["kotlinVersion"]
repositories {
    mavenCentral()
    jcenter()
}

micronaut {
    runtime("netty")
    testRuntime("kotest")

    processing {
        incremental(true)
        annotations("com.nanabell.nico.kurosawa.*")
    }
}

kapt {
    arguments {
        arg("micronaut.openapi.views.spec", "swagger-ui.enabled=true,swagger-ui.theme=flattop")
    }
}

noArg {
    annotation("com.nanabell.nico.ruby.annotation.NoArgConstructor")
}

dependencies {
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut.openapi:micronaut-openapi")
    kapt("io.micronaut.data:micronaut-data-processor")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")

    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")

    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("javax.annotation:javax.annotation-api")

    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-influx")

    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.sql:micronaut-hibernate-jpa")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")

    implementation("io.swagger.core.v3:swagger-annotations")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
}


application {
    mainClass.set("com.nanabell.nico.kurosawa.Application")
}

java {
    sourceCompatibility = JavaVersion.toVersion("14")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "14"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "14"
        }
    }

    dockerBuild {
        images.empty()
        images.add("nico_yazawa/${project.name}:${project.version}")
        images.add("nico_yazawa/${project.name}")
    }

}
