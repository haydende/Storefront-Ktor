package haydende.storefront.model

object Users : BaseTable("Users") {
    val isCustomer = bool("is_customer").default(true)
    val firstName = text("first_name")
    val lastName = text("last_name")
    val email = text("email")
    val password = text("password")
    val phone = varchar("phone", 20)
    val profilePicB64 = text("profile_pic_base64")
}
