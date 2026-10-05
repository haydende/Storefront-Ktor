package haydende.storefront.model.table

object BasketTable : BaseTable("baskets") {
    val user = reference(name = "user_id", foreign = UserTable, fkName = "baskets_user_id_fkey")
    val status = text("status")
}
