package haydende.storefront.model.dto

import kotlinx.serialization.Serializable

@Serializable
class UpdateAddressDTO(
    val id: Int,
    val line1: String? = null,
    val line2: String? = null,
    val line3: String? = null,
    val city: String? = null,
    val country: String? = null,
    val province: String? = null,
    val postalCode: String? = null,
    val isDefault: Boolean? = null,
) {
}