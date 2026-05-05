package haydende.storefront.model

import kotlin.time.Clock
import kotlin.time.Instant

class Order(
    id: Long,
    createdAt: Instant = Clock.System.now(),
    lastModifiedAt: Instant?,
    val basket: Basket,
    val user: User,
): BaseEntity(id, lastModifiedAt, createdAt)