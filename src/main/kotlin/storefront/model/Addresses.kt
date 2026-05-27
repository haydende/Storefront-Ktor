package haydende.storefront.model

object Addresses : BaseTable("addresses") {
    val user = reference(name = "address_user", foreign = Users, fkName = "user_id")
    val line1 = text("line1")
    val line2 = text("line2")
    val line3 = text("line3")
    val cityOrTown = text("city_or_town")
    val stateOrProvince = text("state_or_province")
    val postalCode = text("postal_code")
    val country = text("country")
    val isDefault = bool("is_default").default(false)
}