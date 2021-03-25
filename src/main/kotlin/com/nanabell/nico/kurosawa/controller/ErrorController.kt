package com.nanabell.nico.kurosawa.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.hateoas.JsonError
import javax.validation.ValidationException

@Controller("error")
class ErrorController {

    @Error(ValidationException::class, global = true)
    fun validationHandler(e: ValidationException): HttpResponse<JsonError> {
        return HttpResponse.status<JsonError>(HttpStatus.CONFLICT).body(JsonError(e.message))
    }

}
