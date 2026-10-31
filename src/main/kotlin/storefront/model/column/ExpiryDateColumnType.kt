package haydende.storefront.model.column

import org.jetbrains.exposed.v1.core.ColumnType
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.statements.api.PreparedStatementApi
import org.postgresql.util.PGobject

/**
 * Custom `ColumnType` implementation for Postres `expiry_date` type.
 */
class ExpiryDateColumnType : ColumnType<String>() {

    override fun sqlType() = "expiry_date"

    override fun valueFromDB(value: Any) = value.toString()
        .replace(",", "/")
        .filterNot { arrayOf('(', ')').contains(it) }

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val parameterValue = PGobject().apply {
            this.value = if (value is String) {
                "(${value.replace("/", ",")})"
            } else null

            this.type = "expiry_date"
        }

        super.setParameter(stmt, index, parameterValue)
    }

}

/**
 * Registers a `expiry_date` column type for the table.
 */
fun Table.expiryDate(name: String) = registerColumn(name, ExpiryDateColumnType())
