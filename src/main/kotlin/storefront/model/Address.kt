package haydende.storefront.model

import kotlin.time.Clock
import kotlin.time.Instant

class Address(
    id: Long,
    createdAt: Instant = Clock.System.now(),
    lastModifiedAt: Instant?,
    val user: User,
    val line1: String,
    val line2: String,
    val line3: String,
    val cityOrTown: String,
    val stateOrProvince: String,
    val postalCode: String,
    val country: String,
    val isDefault: Boolean,
) : BaseEntity(id, lastModifiedAt, createdAt)