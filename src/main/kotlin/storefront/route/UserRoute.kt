package haydende.storefront.route

import com.typesafe.config.ConfigException
import haydende.storefront.model.dao.User
import haydende.storefront.model.dto.UserDTO
import haydende.storefront.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.reflect.instanceOf
import io.ktor.util.rootCause
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

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
                }

                if (returned != null) {
                    call.respond(returned)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            post("/new") {
                try {
                    call.receive<UserDTO>().let { userDto ->
                        LOG.info("Received user: $userDto")

                        require(
                            userDto.firstName.isNotBlank() &&
                            userDto.lastName.isNotBlank() &&
                            userDto.email.isNotBlank() &&
                            !userDto.password.isNullOrBlank()
                        )

                        val user = transaction {
                            User.new {
                                firstName = userDto.firstName
                                lastName = userDto.lastName
                                email = userDto.email
                                password = userDto.password
                                phone = userDto.phone
                                profilePicB64 = userDto.profilePicB64
                            }
                        }

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
        }
    }
}
