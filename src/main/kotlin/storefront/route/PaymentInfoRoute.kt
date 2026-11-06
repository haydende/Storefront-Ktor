package haydende.storefront.route

import haydende.storefront.exception.RecordNotFoundException
import haydende.storefront.model.dto.CreatePaymentInfoDTO
import haydende.storefront.model.dto.UpdatePaymentInfoDTO
import haydende.storefront.service.PaymentInfoService
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
import org.slf4j.LoggerFactory
import kotlin.collections.emptyList

fun Application.paymentInfoModule(environment: ApplicationEnvironment) {

    val LOG = LoggerFactory.getLogger("PaymentInfoRoute")
    val paymentInfoService = PaymentInfoService.getInstance(environment)

    routing {
        route("/paymentinfo") {

            get {
                val id = call.queryParameters["id"]?.toInt() ?: throw IllegalArgumentException("id is required")
                val returned = id.let {
                    LOG.info("Getting payment info by id: $id")
                    paymentInfoService.getPaymentInfoById(id)
                }

                if (returned != null) {
                    call.respond(returned.toDTO())
                } else {
                    call.respond(HttpStatusCode.NotFound, "No PaymentInfo found with id: $id")
                }
            }

            get("/user") {
                val userId = call.queryParameters["userId"]?.toInt() ?: throw BadRequestException("Query param userId is required")
                val returned = userId.let {
                    LOG.info("Getting payment info for User ID: $userId")
                    paymentInfoService.getAllPaymentInfoForUserId(userId)
                }

                if (returned.isNotEmpty()) {
                    call.respond(returned.map { it.toDTO() })
                } else {
                    call.respond(HttpStatusCode.NotFound, "No PaymentInfo found for User with ID: $userId")
                }
            }

            post("/new") {
                call.receive<CreatePaymentInfoDTO>().let { paymentInfoDto ->
                    LOG.info("Received new payment info: $paymentInfoDto")

                    val paymentInfo = paymentInfoService.saveNewPaymentInfo(paymentInfoDto)
                    call.respond(HttpStatusCode.Created, paymentInfo.toDTO())
                }
            }

            put("/edit") {
                call.receive<UpdatePaymentInfoDTO>().let { paymentInfoDto ->
                    LOG.info("Received updated payment info details for id: ${paymentInfoDto.id}")

                    val updated = paymentInfoService.updatePaymentInfo(paymentInfoDto) ?: throw RecordNotFoundException("PaymentInfo with ID ${paymentInfoDto.id} not found")
                    call.respond(HttpStatusCode.Accepted, updated.toDTO())
                }
            }

            delete("/delete") {
                val paymentInfoId = call.queryParameters["id"]!!.toInt()
                LOG.info("Request received to delete PaymentInfo with id: $paymentInfoId")

                paymentInfoService.deletePaymentInfo(paymentInfoId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}