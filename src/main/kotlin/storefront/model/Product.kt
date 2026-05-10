package haydende.storefront.model

object Product : BaseEntity("products") {
    val name = text("name")
    val brand = text("brand")
    val description = text("description")
    val imageB64 = text("image_base64")
    val price = double("price")
    val quantity = double("quantity")
}