package haydende.storefront.model.dao

import haydende.storefront.model.Users
import haydende.storefront.model.dto.UserDTO
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class User(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<User>(Users)

    var isCustomer by Users.isCustomer
    var firstName by Users.firstName
    var lastName by Users.lastName
    var email by Users.email
    var password by Users.password
    var phone by Users.phone
    var profilePicB64 by Users.profilePicB64

    fun toDTO() = UserDTO(
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