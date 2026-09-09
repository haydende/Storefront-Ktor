package haydende.storefront.model.table

object BasketTable : BaseTable("basket") {
    val products = reference(name = "products", foreign = ProductTable, fkName = "basket_id")
}
