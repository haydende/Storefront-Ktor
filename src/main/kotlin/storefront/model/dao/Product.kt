package haydende.storefront.model.dao

import haydende.storefront.model.table.ProductTable
import haydende.storefront.model.dto.CreateProductDTO
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class Product(id: EntityID<Int>): IntEntity(id) {

    companion object : IntEntityClass<Product>(ProductTable)

    var name by ProductTable.name
    var brand by ProductTable.brand
    var description by ProductTable.description
    var imageB64 by ProductTable.imageB64
    var price by ProductTable.price
    var quantity by ProductTable.quantity

    fun toDTO() = CreateProductDTO(
        id = id.value,
        name = name,
        brand = brand,
        description = description,
        imageBase64 = imageB64,
        price = price,
        quantity = quantity
    )
}