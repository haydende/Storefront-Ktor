package haydende.storefront.model.table

import haydende.storefront.model.column.expiryDate

object PaymentInfoTable : BaseTable("PaymentInfo") {
    val user = reference(name = "user_id", foreign = UserTable, fkName = "paymentinfo_user_id_fkey")
    val method = text("method")
    val cardNumber = text("card_number")
    val expiryDate = expiryDate("expiry_date").nullable()
    val cvv = text("cvv")
    val accountNumber = text("account_number")
    val isDefault = bool("is_default")
}
