package haydende.storefront.model.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class UpdatePaymentInfoDTO(
    val id: Int,
    val method: String? = null,
    val cardNumber: String? = null,
    val expiryDate: String? = null,
    val cvv: String? = null,
    val accountNumber: String? = null,
    val isDefault: Boolean? = null,
    val created: LocalDateTime? = null,
    val lastModified: LocalDateTime? = null,
)