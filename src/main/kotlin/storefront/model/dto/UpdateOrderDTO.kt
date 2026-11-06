package haydende.storefront.model.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class UpdateOrderDTO(
    val id: Int,
    val userId: Int? = null,
    val addressId: Int? = null,
    val paymentInfoId: Int? = null,
    val basketId: Int? = null,
    val status: String,
    val created: LocalDateTime? = null,
    val lastModified: LocalDateTime? = null,
)