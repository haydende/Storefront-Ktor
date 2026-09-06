package haydende.storefront

import haydende.storefront.exception.AddressNotFoundException
import haydende.storefront.exception.UserNotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.util.reflect.instanceOf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

@OptIn(ExperimentalSerializationApi::class)
fun Application.main() {

    val LOG = LoggerFactory.getLogger("Application")

    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        )
    }

    install(StatusPages) {

        exception<BadRequestException> { call, e ->
            val cause = e.cause?.cause
            if (cause !== null && cause.instanceOf(MissingFieldException::class)) {
                val mfE = cause as MissingFieldException
                val missingFields = mfE.missingFields.joinToString(", ")
                LOG.error("Required fields missing from request: [$missingFields]")
                call.respond(status = HttpStatusCode.BadRequest, message = "Missing required fields: [$missingFields]")

            } else if (cause !== null) {
                LOG.error("Bad request received", e)
                call.respondText(status = HttpStatusCode.BadRequest, text = cause.message ?: "Bad request received. Check server logs for details.")

            } else {
                LOG.error("Bad request received", e)
                call.respondText(status = HttpStatusCode.BadRequest, text = e.message ?: "Bad request received. Check server logs for details.")
            }
        }

        exception<AddressNotFoundException> { call, e ->
            LOG.error("Address not found", e)
            call.respond(status = HttpStatusCode.NotFound, message = e.message ?: "Address not found")
        }

        exception<UserNotFoundException> { call, e ->
            LOG.error("User not found", e)
            call.respond(status = HttpStatusCode.NotFound, message = e.message ?: "User not found")
        }

        exception<Exception> { call, cause ->
            LOG.error("Exception caught by global handler. Responding with HTTP 500.", cause)

            call.respondText(
                status = HttpStatusCode.InternalServerError,
                text = "An error occurred while processing this request. Please review the server logs."
            )
        }
    }
}
