package haydende.storefront.model.dto

import kotlinx.serialization.*

@Serializable
data class UpdateUserDTO(
    val id: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val isCustomer: Boolean? = null,
    val password: String? = null,
    val phone: String? = null,
    val profilePicB64: String? = null
)
