package haydende.storefront.model

object Order : BaseEntity("order") {
    val basket = reference(name = "basket", foreign = Basket, fkName = "basket_id")
    val user = reference(name = "user", foreign = Users, fkName = "user_id")
}