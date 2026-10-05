package haydende.storefront.model.dao

import haydende.storefront.model.dto.UpdateBasketDTO
import haydende.storefront.model.table.BasketProductsTable
import haydende.storefront.model.table.BasketTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class Basket(id: EntityID<Int>): IntEntity(id) {

    companion object : IntEntityClass<Basket>(BasketTable)

    var user by BasketTable.user
    val products by BasketProduct referrersOn BasketProductsTable.basket
    var status by BasketTable.status
    var created by BasketTable.created
    var lastModified by BasketTable.lastModified

    fun toDTO() =
        UpdateBasketDTO(
            id = id.value,
            userId = user.value,
            products = transaction { products.map { it.toDTO() } },
            status = status,
            created = created,
            lastModified = lastModified,
        )

}