package haydende.storefront.route

import haydende.storefront.model.dto.CreateUserDTO
import haydende.storefront.model.dto.UpdateUserDTO
import haydende.storefront.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory

val LOG = LoggerFactory.getLogger("UserRoute")

@OptIn(ExperimentalSerializationApi::class)
fun Application.userModule(environment: ApplicationEnvironment) {

    val userService = UserService.getInstance(environment)

    routing {
        route("/user") {
            get {
                val id = call.queryParameters["id"]?.toInt() ?: throw IllegalArgumentException("id is required")
                val returned = try {
                    LOG.info("Getting user by id: $id")
                    userService.getUserById(id)

                } catch (e: Exception) {
                    LOG.error("Error getting user by id", e)
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        "An error occurred while processing this request. Please review the server logs."
                    )
                    null // ensures result is null or UserDTO
                }

                if (returned != null) {
                    call.respond(returned.toDTO())
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            post("/new") {
                try {
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
                } catch (e: BadRequestException) {
                    val cause = e.cause?.cause as MissingFieldException?
                    if (cause != null) {
                        val mfE = cause.cause as MissingFieldException
                        val missingFields = mfE.missingFields.joinToString(", ")
                        LOG.error("Required fields missing from request: [$missingFields]")
                        call.respond(HttpStatusCode.BadRequest, "Missing required fields: [$missingFields]")

                    } else {
                        LOG.error("Bad request received", e)
                        call.respond(HttpStatusCode.BadRequest)
                    }

                } catch (e: Exception) {
                    LOG.error("Error creating user", e)
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        "An error occurred while processing this request. Please review the server logs."
                    )
                }
            }

            put("/edit") {
                try {

                    call.receive<UpdateUserDTO>().let { user ->
                        LOG.info("Received updated user details for id: ${user.id}")

                        val updated = userService.updateUser(user) ?: throw IllegalStateException("User not found")
                        call.respond(HttpStatusCode.Accepted, updated.toDTO())
                    }

                } catch (e: BadRequestException) {
                    val cause = e.cause?.cause as MissingFieldException?
                    if (cause != null) {
                        val mfE = cause.cause as MissingFieldException
                        val missingFields = mfE.missingFields.joinToString(", ")
                        LOG.error("Required fields missing from request: [$missingFields]")
                        call.respond(HttpStatusCode.BadRequest, "Missing required fields: [$missingFields]")

                    } else {
                        LOG.error("Bad request received", e)
                        call.respond(HttpStatusCode.BadRequest)
                    }

                } catch (e: Exception) {
                    LOG.error("Error creating user", e)
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        "An error occurred while processing this request. Please review the server logs."
                    )
                }
            }

            delete("/delete") {
                try {
                    val userId = call.queryParameters["id"]!!.toInt()

                    userService.deleteUser(userId)
                    call.respond(HttpStatusCode.OK)

                } catch(e: Exception) {
                    LOG.error("Error deleting user", e)
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        "An error occurred while processing this request. Please review the server logs."
                    )
                }
            }

        }
    }
}
