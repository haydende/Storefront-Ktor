package haydende.storefront.model.dao

import haydende.storefront.model.table.UserTable
import haydende.storefront.model.dto.CreateUserDTO
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class User(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<User>(UserTable)

    var isCustomer by UserTable.isCustomer
    var firstName by UserTable.firstName
    var lastName by UserTable.lastName
    var email by UserTable.email
    var password by UserTable.password
    var phone by UserTable.phone
    var profilePicB64 by UserTable.profilePicB64

    fun toDTO() = CreateUserDTO(
        id = id.value,
        firstName = firstName,
        lastName = lastName,
        email = email,
        isCustomer = isCustomer,
        phone = phone,
        profilePicB64 = profilePicB64,
        password = null
    )
}