package haydende.storefront.model.dto

import kotlinx.serialization.*

@Serializable
data class UserDTO(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val isCustomer: Boolean,
    val password: String? = null,
    val phone: String,
    val profilePicB64: String? = null
)
