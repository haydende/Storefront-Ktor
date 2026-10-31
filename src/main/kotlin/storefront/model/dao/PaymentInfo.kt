package haydende.storefront.model.dao

import haydende.storefront.model.dto.UpdatePaymentInfoDTO
import haydende.storefront.model.table.PaymentInfoTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class PaymentInfo(id: EntityID<Int>): IntEntity(id) {

    companion object : IntEntityClass<PaymentInfo>(PaymentInfoTable)

    var user by PaymentInfoTable.user
    var method by PaymentInfoTable.method
    var cardNumber by PaymentInfoTable.cardNumber
    var expiryDate by PaymentInfoTable.expiryDate
    var cvv by PaymentInfoTable.cvv
    var accountNumber by PaymentInfoTable.accountNumber
    var isDefault by PaymentInfoTable.isDefault
    var created by PaymentInfoTable.created
    var lastModified by PaymentInfoTable.lastModified

    fun toDTO() = UpdatePaymentInfoDTO(
        id = id.value,
        method = method,
        cardNumber = cardNumber,
        expiryDate = expiryDate,
        cvv = cvv,
        accountNumber = accountNumber,
        isDefault = isDefault,
        created = created,
        lastModified = lastModified,
    )
}
