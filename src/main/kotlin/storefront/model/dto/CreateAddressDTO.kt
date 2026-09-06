package haydende.storefront.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateAddressDTO (
    val id: Int? = null,
    val userId: Int,
    val line1: String,
    val line2: String? = null,
    val line3: String? = null,
    val city: String,
    val province: String,
    val country: String,
    val postCode: String,
    val isDefault: Boolean,

)