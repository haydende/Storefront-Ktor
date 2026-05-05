package haydende.storefront.model

import kotlin.time.Clock
import kotlin.time.Instant

class Product(
    id: Long,
    lastModifiedAt: Instant,
    createdAt: Instant = Clock.System.now(),
    name: String,
    brand: String,
    description: String,
    imageB64: String,
    price: Double,
    quantity: Int,
): BaseEntity(id, lastModifiedAt, createdAt)