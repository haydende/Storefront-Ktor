package haydende.storefront.route

import haydende.storefront.exception.ProductNotFoundException
import haydende.storefront.model.dto.CreateProductDTO
import haydende.storefront.model.dto.UpdateProductDTO
import haydende.storefront.service.ProductService
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

fun Application.productModule(environment: ApplicationEnvironment) {

    val LOG = LoggerFactory.getLogger("ProductRoute")
    val productService = ProductService.getInstance(environment)

    routing {
        route("/product") {
            get {
                val id = call.queryParameters["id"]?.toInt() ?: throw IllegalArgumentException("id is required")
                val returned = id.let {
                    LOG.info("Getting product by id: $id")
                    productService.getProductById(id)
                }

                if (returned != null) {
                    call.respond(returned.toDTO())
                } else {
                    call.respond(HttpStatusCode.NotFound, "No product found with id: $id")
                }
            }

            post("/new") {
                call.receive<CreateProductDTO>().let { productDto ->
                    LOG.info("Received new product: $productDto")

                    val product = productService.saveNewProduct(productDto)
                    call.respond(HttpStatusCode.Created, product.toDTO())
                }
            }

            put("/edit") {
                call.receive<UpdateProductDTO>().let { productDto ->
                    LOG.info("Received updated product details for id: ${productDto.id}")

                    val updated = productService.updateProduct(productDto) ?: throw ProductNotFoundException("Product with ID ${productDto.id} not found")
                    call.respond(HttpStatusCode.Accepted, updated.toDTO())
                }
            }

            delete("/delete") {
                val productId = call.queryParameters["id"]!!.toInt()

                productService.deleteProduct(productId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}