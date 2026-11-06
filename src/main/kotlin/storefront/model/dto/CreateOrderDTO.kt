package haydende.storefront.model.dto

import kotlinx.serialization.Serializable

@Serializable
class CreateOrderDTO(
    val userId: Int,
    val addressId: Int,
    val basketId: Int,
    val paymentInfoId: Int,
)
