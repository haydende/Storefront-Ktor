package haydende.storefront.model.dao

import haydende.storefront.model.dto.BasketProductsDTO
import haydende.storefront.model.table.BasketProductsTable
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.CompositeEntity
import org.jetbrains.exposed.v1.dao.CompositeEntityClass

class BasketProduct(id: EntityID<CompositeID>): CompositeEntity(id) {

    companion object : CompositeEntityClass<BasketProduct>(BasketProductsTable)

    var basket by Basket referencedOn BasketProductsTable.basket
    var product by Product referencedOn BasketProductsTable.product
    var quantity by BasketProductsTable.quantity

    fun toDTO() = BasketProductsDTO(
        basketId = basket.id.value,
        productId = product.id.value,
        quantity = quantity
    )

}