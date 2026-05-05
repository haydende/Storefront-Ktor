package haydende.storefront.model

import kotlin.time.Clock
import kotlin.time.Instant

class PaymentInfo(
    id: Long,
    createdAt: Instant = Clock.System.now(),
    lastModifiedAt: Instant?,
    val user: User,
    val method: String,
    val cardNumber: String,
    val expiryDate: String,
    val cvv: String,
    val accountNumber: String,
    val isDefault: Boolean,
): BaseEntity(id, lastModifiedAt, createdAt)
