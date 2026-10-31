package haydende.storefront.model.dto

import kotlinx.serialization.Serializable

@Serializable
class CreatePaymentInfoDTO(
    val userId: Int,
    val method: String,
    val cardNumber: String,
    val expiryDate: String,
    val cvv: String,
    val accountNumber: String,
    val isDefault: Boolean? = false,
)
