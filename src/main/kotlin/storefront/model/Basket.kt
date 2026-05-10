package haydende.storefront.model

object Basket : BaseEntity("basket") {
    val products = reference(name = "products", foreign = Product, fkName = "basket_id")
}
