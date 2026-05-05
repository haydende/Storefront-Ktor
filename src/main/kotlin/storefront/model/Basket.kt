package haydende.storefront.model

import kotlin.time.Clock
import kotlin.time.Instant

class Basket(
    id: Long,
    lastModifiedAt: Instant,
    createdAt: Instant = Clock.System.now(),
    val products: Map<Product, Int>
): BaseEntity(id, lastModifiedAt, createdAt)