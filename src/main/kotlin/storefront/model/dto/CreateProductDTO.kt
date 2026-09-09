package haydende.storefront.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateProductDTO(
    val id: Int? = null,
    val name: String,
    val brand: String,
    val description: String,
    val price: Double,
    val imageBase64: String,
    val quantity: Int
)