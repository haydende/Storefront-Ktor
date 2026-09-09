package haydende.storefront.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProductDTO(
    val id: Int,
    val name: String? = null,
    val brand: String? = null,
    val price: Double? = null, // preserve 2 decimal places
    val description: String? = null,
    val imageBase64: String? = null,
    val quantity: Int? = null
)