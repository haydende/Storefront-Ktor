package haydende.storefront.model.column

import org.jetbrains.exposed.v1.core.ColumnType
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.statements.api.PreparedStatementApi
import org.postgresql.util.PGmoney
import org.postgresql.util.PGobject

/**
 * Custom `ColumnType` implementation for Postres `money` type.
 */
class MoneyColumnType : ColumnType<Double>() {

    override fun sqlType() = "money"

    override fun valueFromDB(value: Any) = value.toString().toDouble()

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val parameterValue: PGobject? = value?.let { PGmoney("£$value") } // PGmoney constructor removes first char
        super.setParameter(stmt, index, parameterValue)
    }

}

/**
 * Registers a `money` column type for the table.
 */
fun Table.money(name: String) = registerColumn(name, MoneyColumnType())
