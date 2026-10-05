package haydende.storefront.model.table

import org.jetbrains.exposed.v1.core.dao.id.CompositeIdTable

object BasketProductsTable : CompositeIdTable("basketproducts") {
    val basket = reference("basket_id", BasketTable)
    val product = reference("product_id", ProductTable)
    val quantity = integer("quantity")

    init {
        addIdColumn(basket)
        addIdColumn(product)
    }

    override val primaryKey = PrimaryKey(basket, product)

}