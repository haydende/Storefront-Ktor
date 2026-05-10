package haydende.storefront.model

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.time.Clock

abstract class BaseEntity(tableName: String) : IntIdTable(tableName) {
    val created = datetime("created").default(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    val lastModified = datetime("last_modified").nullable()
}

