package haydende.storefront

import haydende.storefront.service.UserService
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.userModule(environment: ApplicationEnvironment) {

    val userService = UserService.getInstance(environment)

    routing {
        userRoutes(userService)
    }
}

fun Route.userRoutes(service: UserService) {

    get("/user") {
        call.respond(
            service.getUserById(
                call.queryParameters["id"]?.toInt() ?: throw IllegalArgumentException("id is required")
            )
        )
    }



}