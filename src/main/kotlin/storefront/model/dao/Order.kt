package haydende.storefront.model.dao

import haydende.storefront.model.dto.UpdateOrderDTO
import haydende.storefront.model.table.OrderTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class Order(id: EntityID<Int>): IntEntity(id) {

    companion object : IntEntityClass<Order>(OrderTable)

    var basket by OrderTable.basket
    var user by OrderTable.user
    var address by OrderTable.address
    var paymentInfo by OrderTable.paymentInfo
    var status by OrderTable.status
    var created by OrderTable.created
    var lastModified by OrderTable.lastModified

    fun toDTO() = UpdateOrderDTO(
        id = id.value,
        userId = user.value,
        addressId = address.value,
        paymentInfoId = paymentInfo.value,
        basketId = basket.value,
        status = status,
        created = created,
        lastModified = lastModified,
    )

}