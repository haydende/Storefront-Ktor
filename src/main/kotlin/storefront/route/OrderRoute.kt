package haydende.storefront.route

import haydende.storefront.exception.RecordNotFoundException
import haydende.storefront.model.dto.CreateOrderDTO
import haydende.storefront.model.dto.UpdateOrderDTO
import haydende.storefront.service.AddressService
import haydende.storefront.service.BasketService
import haydende.storefront.service.OrderService
import haydende.storefront.service.PaymentInfoService
import haydende.storefront.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.slf4j.LoggerFactory

fun Application.orderModule(environment: ApplicationEnvironment) {

    val LOG = LoggerFactory.getLogger("OrderRoute")
    val orderService = OrderService.getInstance(environment)
    val userService = UserService.getInstance(environment)
    val addressService = AddressService.getInstance(environment)
    val paymentInfoService = PaymentInfoService.getInstance(environment)
    val basketService = BasketService.getInstance(environment)

    routing {

        route("/order") {

            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("ID is required")
                val returned = id.let {
                    LOG.info("Getting order by ID: $id")
                    orderService.getOrderById(it)
                }

                if (returned != null) {
                    call.respond(returned.toDTO())
                } else {
                    call.respondText(text = "No Order found with ID: $id", status = HttpStatusCode.NotFound)
                }
            }

            get("/user") {
                val id = call.parameters["userId"]?.toInt() ?: throw IllegalArgumentException("User ID is required")
                val returned = id.let {
                    LOG.info("Getting orders for User with ID: $id")
                    orderService.getOrdersForUserId(id)
                }

                if (returned.isNotEmpty()) {
                    call.respond(returned.map { it.toDTO() })
                } else {
                    call.respondText(text = "No Orders found for User with ID: $id", status = HttpStatusCode.NotFound)
                }
            }

            post("/new") {
                call.receive<CreateOrderDTO>().let { dto ->
                    LOG.info("Received new Order for User: ${dto.userId}")

                    userService.getUserById(dto.userId) ?: throw RecordNotFoundException("Referenced User with ID ${dto.userId} not found")
                    addressService.getAddressById(dto.addressId) ?: throw RecordNotFoundException("Referenced Address with ID ${dto.addressId} not found for User with ID ${dto.userId}")
                    paymentInfoService.getPaymentInfoById(dto.paymentInfoId) ?: throw RecordNotFoundException("Referenced Payment Information with ID ${dto.paymentInfoId} not found for User with ID ${dto.userId}")
                    basketService.getBasketById(dto.basketId) ?: throw RecordNotFoundException("Referenced Basket with ID ${dto.basketId} not found for User with ID ${dto.userId}")

                    val returned = orderService.saveNewOrder(dto)
                    call.respond(HttpStatusCode.Created, returned.toDTO())
                }
            }

            put("/edit") {
                call.receive<UpdateOrderDTO>().let { dto ->
                    LOG.info("Received update for Order with ID: ${dto.id}")

                    val returned = orderService.updateOrder(dto) ?: throw RecordNotFoundException("Order with ID ${dto.id} not found")
                    call.respond(HttpStatusCode.Accepted, returned.toDTO())
                }
            }

            delete("/delete") {
                val orderId = call.queryParameters["id"]?.toInt() ?: throw IllegalArgumentException("ID is required")

                orderService.deleteOrder(orderId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}