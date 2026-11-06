package haydende.storefront.service

import haydende.storefront.exception.RecordNotFoundException
import haydende.storefront.model.dao.Basket
import haydende.storefront.model.dao.BasketProduct
import haydende.storefront.model.dao.Product
import haydende.storefront.model.dto.CreateBasketDTO
import haydende.storefront.model.dto.UpdateBasketDTO
import haydende.storefront.model.table.BasketProductsTable
import haydende.storefront.model.table.BasketTable
import haydende.storefront.model.table.UserTable
import haydende.storefront.util.DatabaseUtils
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.Clock

class BasketService(environment: ApplicationEnvironment) {

    var databaseUtils = DatabaseUtils.getInstance(environment)

    init {
        transaction {
            SchemaUtils.create(BasketTable, BasketProductsTable)
        }
    }

    fun getBasketById(id: Int) = transaction {
        Basket.findById(id)
    }

    fun getBasketsByUserId(userId: Int) = transaction {
        Basket.find { BasketTable.user eq userId }.toList()
    }

    fun saveNewBasket(createBasketDto: CreateBasketDTO) = transaction {
        val created = Basket.new {
            user = EntityID(createBasketDto.userId, UserTable)
            status = "open"
        }

        createBasketDto.products.forEach { p ->
            BasketProduct.new {
                basket = created
                product = Product[p.id]
                quantity = p.quantity
            }
        }

        return@transaction Basket[created.id.value]
    }

    fun updateBasket(updateBasketDto: UpdateBasketDTO) = transaction {
        updateBasketDto.products.forEach { p ->
            val bpId = CompositeID { compId ->
                compId[BasketProductsTable.basket] = p.basketId
                compId[BasketProductsTable.product] = p.productId
            }

            val found = BasketProduct.findById(bpId)
            if (found != null) {
                when (p.quantity) {
                    0 -> found.delete()
                    else -> found.quantity = p.quantity
                }
            } else {
                BasketProduct.new {
                    basket = Basket[p.basketId]
                    product = Product[p.productId]
                    quantity = p.quantity
                }
            }
        }

        val basket = Basket[updateBasketDto.id]
        basket.lastModified = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        updateBasketDto.status?.let { basket.status = it }

        return@transaction Basket[updateBasketDto.id]
    }

    fun deleteBasket(basketId: Int) = transaction {
        try {
            BasketProduct.find { BasketProductsTable.basket eq basketId }.forEach { it.delete() }
            Basket[basketId].delete()
        } catch (enf: EntityNotFoundException) {
            LOG.error("Basket with ID $basketId not found. Rethrowing as RecordNotFoundException")
            throw RecordNotFoundException("Basket with ID $basketId not found", enf)
        }
    }

    companion object {
        @Volatile
        private var instance: BasketService? = null
        private val LOG = org.slf4j.LoggerFactory.getLogger(BasketService::class.java)

        fun getInstance(environment: ApplicationEnvironment): BasketService =
            instance ?: synchronized(this) {
                instance ?: BasketService(environment).also { instance = it }
            }
    }

}