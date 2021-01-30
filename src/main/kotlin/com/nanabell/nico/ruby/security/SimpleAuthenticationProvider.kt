package com.nanabell.nico.ruby.security

import com.nanabell.nico.ruby.config.BasicAuthCredentials
import io.micronaut.context.annotation.Parallel
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.reactivestreams.Publisher
import javax.inject.Singleton

@Singleton
@Parallel
class SimpleAuthenticationProvider(private val credentials: BasicAuthCredentials) : AuthenticationProvider {

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        request: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        if (request.identity != credentials.username)
            return Flowable.just(AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND))

        if (request.secret != credentials.password)
            return Flowable.just(AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH))

        return Flowable.just(UserDetails(credentials.username, listOf()))
    }

}
