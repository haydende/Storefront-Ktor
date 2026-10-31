package haydende.storefront.route

import haydende.storefront.exception.RecordNotFoundException
import haydende.storefront.model.dto.CreateBasketDTO
import haydende.storefront.model.dto.UpdateBasketDTO
import haydende.storefront.service.BasketService
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

fun Application.basketModule(environment: ApplicationEnvironment) {

    val LOG = LoggerFactory.getLogger("BasketRoute")
    val basketService = BasketService.getInstance(environment)

    routing {
        route("/basket") {

            get {
                val id = call.queryParameters["id"]?.toInt() ?: throw IllegalArgumentException("id is required")
                val returned = id.let {
                    LOG.info("Getting basket by id: $id")
                    basketService.getBasketById(id)
                }

                if (returned != null) {
                    call.respond(returned.toDTO())
                } else {
                    call.respond(HttpStatusCode.NotFound, "No Basket found with id: $id")
                }
            }

            post("/new") {
                call.receive<CreateBasketDTO>().let { basketDto ->
                    LOG.info("Received new basket: $basketDto")

                    val basket = basketService.saveNewBasket(basketDto)
                    call.respond(HttpStatusCode.Created, basket.toDTO())
                }
            }

            put("/edit") {
                call.receive<UpdateBasketDTO>().let { basketDto ->
                    LOG.info("Received updated basket details for id: ${basketDto.id}")

                    val updated = basketService.updateBasket(basketDto) ?: throw RecordNotFoundException("Basket with ID ${basketDto.id} not found")
                    call.respond(HttpStatusCode.Accepted, updated.toDTO())
                }
            }

            delete("/delete") {
                val basketId = call.queryParameters["id"]!!.toInt()

                basketService.deleteBasket(basketId)
                call.respond(HttpStatusCode.OK)
            }

        }
    }

}