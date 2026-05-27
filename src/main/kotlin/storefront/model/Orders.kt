package haydende.storefront.model

object Orders : BaseTable("order") {
    val basket = reference(name = "basket", foreign = Baskets, fkName = "basket_id")
    val user = reference(name = "user", foreign = Users, fkName = "user_id")
}