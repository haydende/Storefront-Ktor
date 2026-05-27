package haydende.storefront.model

object PaymentInfos : BaseTable("PaymentInfo") {
    val user = reference(name = "user", foreign = Users, fkName = "user_id")
    val method = text("method")
    val cardNumber = text("card_number")
    val expiryDate = text("expiry_date")
    val cvv = text("cvv")
    val accountNumber = text("account_number")
    val isDefault = bool("is_default")
}
