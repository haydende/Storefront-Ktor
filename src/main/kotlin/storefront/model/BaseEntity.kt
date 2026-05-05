package haydende.storefront.model

import java.util.Date
import kotlin.time.Clock
import kotlin.time.Instant

abstract class BaseEntity(
    val id: Long,
    val lastModifiedAt: Instant? = null,
    val createdAt: Instant = Clock.System.now(),
)
