package haydende.storefront.model

object Addresses : BaseTable("addresses") {
    val user = reference(name = "user_id", foreign = Users, fkName = "addresses_user_id_fkey")
    val line1 = text("line_1")
    val line2 = text("line_2").nullable()
    val line3 = text("line_3").nullable()
    val cityOrTown = text("city_or_town")
    val stateOrProvince = text("state_or_province")
    val postalCode = text("postal_code")
    val country = text("country")
    val isDefault = bool("is_default").default(false)
}