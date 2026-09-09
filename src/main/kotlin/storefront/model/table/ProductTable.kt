package haydende.storefront.model.table

import haydende.storefront.model.column.money


object ProductTable : BaseTable("products") {
    val name = text("name")
    val brand = text("brand")
    val description = text("description")
    val imageB64 = text("image_base64")
    val price = money("price")
    val quantity = integer("quantity")
}