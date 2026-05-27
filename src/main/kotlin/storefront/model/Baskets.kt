package haydende.storefront.model

object Baskets : BaseTable("basket") {
    val products = reference(name = "products", foreign = Products, fkName = "basket_id")
}
