package haydende.storefront.model.dto

import kotlinx.serialization.Serializable

@Serializable
class CreateBasketDTO(
    val id: Int? = null,
    val userId: Int,
    val products: List<UpdateProductDTO>,
)