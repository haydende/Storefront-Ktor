package haydende.storefront.model.table

object OrderTable : BaseTable("order") {
    val basket = reference(name = "basket", foreign = BasketTable, fkName = "basket_id")
    val user = reference(name = "user", foreign = UserTable, fkName = "user_id")
}