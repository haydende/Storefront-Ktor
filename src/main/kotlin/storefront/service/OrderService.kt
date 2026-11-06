package haydende.storefront.service

import haydende.storefront.model.dao.Order
import haydende.storefront.model.dto.CreateOrderDTO
import haydende.storefront.model.dto.UpdateOrderDTO
import haydende.storefront.model.table.AddressTable
import haydende.storefront.model.table.BasketTable
import haydende.storefront.model.table.OrderTable
import haydende.storefront.model.table.PaymentInfoTable
import haydende.storefront.model.table.UserTable
import haydende.storefront.util.DatabaseUtils
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import kotlin.time.Clock

class OrderService(environment: ApplicationEnvironment) {

    val databaseUtils = DatabaseUtils.getInstance(environment)

    init {
        transaction {
            SchemaUtils.create(OrderTable)
        }
    }

    fun getOrderById(id: Int) = transaction {
        Order.findById(id)
    }

    fun getOrdersForUserId(id: Int) = transaction {
        Order.find { OrderTable.user eq id }.toList()
    }

    fun saveNewOrder(orderDto: CreateOrderDTO) = transaction {
        Order.new {
            user = EntityID(orderDto.userId, UserTable)
            basket = EntityID(orderDto.basketId, BasketTable)
            address = EntityID(orderDto.addressId, AddressTable)
            paymentInfo = EntityID(orderDto.paymentInfoId, PaymentInfoTable)
        }
    }

    fun updateOrder(orderDTO: UpdateOrderDTO) = transaction {
        Order.findById(orderDTO.id)?.apply {
            status = orderDTO.status
            lastModified = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        }
    }

    fun deleteOrder(orderId: Int) = transaction {
        Order.findById(orderId)?.delete()
    }

    companion object {
        @Volatile
        private var instance: OrderService? = null
        private val LOG = LoggerFactory.getLogger(OrderService::class.java)

        fun getInstance(environment: ApplicationEnvironment): OrderService =
            instance ?: synchronized(this) {
                instance ?: OrderService(environment).also { instance = it }
            }
    }

}