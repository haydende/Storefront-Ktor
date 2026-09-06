package haydende.storefront.route

import haydende.storefront.model.dto.CreateUserDTO
import haydende.storefront.model.dto.UpdateUserDTO
import haydende.storefront.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory


fun Application.userModule(environment: ApplicationEnvironment) {

    val LOG = LoggerFactory.getLogger("UserRoute")
    val userService = UserService.getInstance(environment)

    routing {
        route("/user") {
            get {
                val id = call.queryParameters["id"]?.toInt() ?: throw IllegalArgumentException("id is required")
                val returned = id.let {
                    LOG.info("Getting user by id: $id")
                    userService.getUserById(id)

                }

                if (returned != null) {
                    call.respond(returned.toDTO())
                } else {
                    call.respond(HttpStatusCode.NotFound, "No user found with id: $id")
                }
            }

            post("/new") {
                call.receive<CreateUserDTO>().let { userDto ->
                    LOG.info("Received user: $userDto")

                    require(
                        userDto.firstName.isNotBlank() &&
                        userDto.lastName.isNotBlank() &&
                        userDto.email.isNotBlank() &&
                        !userDto.password.isNullOrBlank()
                    )

                    val user = transaction { userService.saveNewUser(userDto) }
                    call.respond(HttpStatusCode.Created, user.toDTO())
                }
            }

            post("/update-password") {
                call.receive<Map<String, String>>().let { resetRequest ->
                    LOG.info("Received password reset request for user: ${resetRequest["userId"]}")
                    userService.updateUserPassword(resetRequest["userId"]!!.toInt(), resetRequest["password"]!!)
                    call.respond(HttpStatusCode.Accepted)
                }
            }

            put("/edit") {
                call.receive<UpdateUserDTO>().let { user ->
                    LOG.info("Received updated user details for id: ${user.id}")

                    val updated = userService.updateUser(user) ?: throw IllegalStateException("User not found")
                    call.respond(HttpStatusCode.Accepted, updated.toDTO())
                }
            }

            delete("/delete") {
                val userId = call.queryParameters["id"]!!.toInt()

                userService.deleteUser(userId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
