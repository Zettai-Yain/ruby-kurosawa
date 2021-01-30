package com.nanabell.nico.ruby.config

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("micronaut.security.basic-auth.credentials")
class BasicAuthCredentials {

    var username: String? = null

    var password: String? = null
}