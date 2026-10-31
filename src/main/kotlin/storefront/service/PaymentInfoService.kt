package haydende.storefront.service

import haydende.storefront.exception.RecordNotFoundException
import haydende.storefront.model.dao.PaymentInfo
import haydende.storefront.model.dto.CreatePaymentInfoDTO
import haydende.storefront.model.dto.UpdatePaymentInfoDTO
import haydende.storefront.model.table.PaymentInfoTable
import haydende.storefront.model.table.UserTable
import haydende.storefront.util.DatabaseUtils
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.Clock

class PaymentInfoService(environment: ApplicationEnvironment) {

    val databaseUtils = DatabaseUtils.getInstance(environment)

    init {
        transaction {
            SchemaUtils.create(PaymentInfoTable)
        }
    }
    fun getPaymentInfoById(id: Int) = transaction {
        PaymentInfo.findById(id)
    }

    fun getAllPaymentInfoForUserId(id: Int) = transaction {
        PaymentInfo.find { PaymentInfoTable.user eq id }.toList()
    }

    fun saveNewPaymentInfo(dto: CreatePaymentInfoDTO) = transaction {
        PaymentInfo.new {
            user = EntityID(dto.userId, UserTable)
            method = dto.method
            cardNumber = dto.cardNumber
            expiryDate = dto.expiryDate
            cvv = dto.cvv
            accountNumber = dto.accountNumber
            isDefault = dto.isDefault ?: false
        }
    }

    fun updatePaymentInfo(dto: UpdatePaymentInfoDTO) = transaction {
        PaymentInfo.findById(dto.id)?.apply {
            dto.method?.let { method -> this.method = method }
            dto.cardNumber?.let { cardNumber -> this.cardNumber = cardNumber }
            dto.expiryDate?.let { expiryDate -> this.expiryDate = expiryDate }
            dto.cvv?.let { cvv -> this.cvv = cvv }
            dto.accountNumber?.let { accountNumber -> this.accountNumber = accountNumber }
            dto.isDefault?.let { isDefault -> this.isDefault = isDefault }

            this.lastModified = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        }
    }

    fun deletePaymentInfo(id: Int) = transaction {
        try {
            PaymentInfo[id].delete()
        } catch (enf: EntityNotFoundException) {
            LOG.error("PaymentInfo with ID $id not found. Rethrowing as PaymentInfoNotFoundException")
            throw RecordNotFoundException("PaymentInfo with ID $id not found", enf)
        }
    }

    companion object {
        @Volatile
        private var instance: PaymentInfoService? = null
        private val LOG = org.slf4j.LoggerFactory.getLogger(PaymentInfoService::class.java)

        fun getInstance(environment: ApplicationEnvironment): PaymentInfoService =
            instance ?: synchronized(this) {
                instance ?: PaymentInfoService(environment).also { instance = it }
            }
    }
}