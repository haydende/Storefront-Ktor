package haydende.storefront.model.dto

import kotlinx.serialization.Serializable

@Serializable
class BasketProductsDTO(
    val basketId: Int,
    val productId: Int,
    val quantity: Int,
)