package haydende.storefront.service

import haydende.storefront.exception.ProductNotFoundException
import haydende.storefront.model.table.ProductTable
import haydende.storefront.model.dao.Product
import haydende.storefront.model.dto.CreateProductDTO
import haydende.storefront.model.dto.UpdateProductDTO
import haydende.storefront.util.DatabaseUtils
import io.ktor.server.application.ApplicationEnvironment
import org.jetbrains.exposed.v1.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class ProductService(environment: ApplicationEnvironment) {

    var databaseUtils = DatabaseUtils.getInstance(environment)

    init {
        transaction {
            SchemaUtils.create(ProductTable)
        }
    }

    fun getProductById(id: Int): Product? = transaction {
        Product.findById(id)
    }

    fun saveNewProduct(createProductDto: CreateProductDTO) = transaction {
        Product.new {
            name = createProductDto.name
            brand = createProductDto.brand
            description = createProductDto.description
            imageB64 = createProductDto.imageBase64
            price = createProductDto.price
            quantity = createProductDto.quantity
        }
    }

    fun updateProduct(productDto: UpdateProductDTO) = transaction {
        Product.findByIdAndUpdate(productDto.id) {
            productDto.name?.let { name -> it.name = name }
            productDto.brand?.let { brand -> it.brand = brand }
            productDto.description?.let { description -> it.description = description }
            productDto.imageBase64?.let { imageBase64 -> it.imageB64 = imageBase64 }
            productDto.price?.let { price -> it.price = price }
            productDto.quantity?.let { quantity -> it.quantity = quantity }
        }
    }

    fun deleteProduct(productId: Int) = transaction {
        try {
            Product[productId].delete()
        } catch (enf: EntityNotFoundException) {
            LOG.error("Product with ID $productId not found. Rethrowing as ProductNotFoundException:", enf)
            throw ProductNotFoundException("Product with ID $productId not found", enf)
        }
    }

    companion object {
        @Volatile
        private var instance: ProductService? = null
        private val LOG = org.slf4j.LoggerFactory.getLogger(ProductService::class.java)

        fun getInstance(environment: ApplicationEnvironment): ProductService =
            instance ?: synchronized(this) {
                instance ?: ProductService(environment).also { instance = it }
            }
    }
}