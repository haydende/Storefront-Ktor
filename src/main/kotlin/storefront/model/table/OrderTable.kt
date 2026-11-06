package haydende.storefront.model.table

object OrderTable : BaseTable("order") {
    val user = reference(name = "user", foreign = UserTable, fkName = "orders_user_id_fkey")
    val basket = reference(name = "basket", foreign = BasketTable, fkName = "orders_basket_id_fkey")
    val address = reference(name = "address", foreign = AddressTable, fkName = "orders_address_id_fkey")
    val paymentInfo = reference(name = "payment_info", foreign = PaymentInfoTable, fkName = "orders_payment_info_id_fkey")
    val status = text("status").default("received")
}