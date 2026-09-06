package haydende.storefront.route

import haydende.storefront.model.dto.CreateAddressDTO
import haydende.storefront.model.dto.UpdateAddressDTO
import haydende.storefront.service.AddressService
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
import org.slf4j.LoggerFactory


fun Application.addressModule(environment: ApplicationEnvironment) {

    val LOG = LoggerFactory.getLogger("AddressRoute")
    val addressService = AddressService.getInstance(environment)
    val userService = UserService.getInstance(environment)

    routing {

        route("/address") {

            get {
                val userId = call.queryParameters["userId"]?.toInt() ?: throw IllegalArgumentException("id is required")
                val returned = userId.let {
                    LOG.info("Getting addresses for User ID: $userId")
                    addressService.getAddressesForUserId(userId)
                }

                if (returned.isNotEmpty()) {
                    call.respond(returned)
                } else {
                    call.respond(HttpStatusCode.NotFound, "No addresses found for user ID: $userId")
                }
            }

            post("/new") {
                call.receive<CreateAddressDTO>().let { addressDto ->
                    LOG.info("Received new address for UserID: ${addressDto.userId}")
                    if (userService.getUserById(addressDto.userId) != null) {
                        val address = addressService.saveNewAddress(addressDto)
                        call.respond(HttpStatusCode.Created, address.toDTO())
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid User ID provided")
                    }

                }
            }

            put("/edit") {
                call.receive<UpdateAddressDTO>().let { addressDto ->
                    LOG.info("Received updated details for address with ID [${addressDto.id}]")

                    val updated = addressService.updateAddress(addressDto)
                    call.respond(HttpStatusCode.Accepted, updated.toDTO())
                }
            }

            delete("/delete") {
                val addressId = call.queryParameters["id"]!!.toInt()
                addressService.deleteAddress(addressId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}