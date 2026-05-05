package haydende.storefront.model

import kotlin.time.Clock
import kotlin.time.Instant

class User(
    id: Long,
    lastModifiedAt: Instant? = null,
    createdAt: Instant = Clock.System.now(),
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String,
    val profilePicture: String? = null,
): BaseEntity(id, lastModifiedAt, createdAt) {}
