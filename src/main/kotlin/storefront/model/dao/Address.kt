package haydende.storefront.model.dao

import haydende.storefront.model.Addresses
import haydende.storefront.model.dto.CreateAddressDTO
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class Address(id: EntityID<Int>): IntEntity(id) {

    companion object : IntEntityClass<Address>(Addresses)

    var user by Addresses.user
    var line1 by Addresses.line1
    var line2 by Addresses.line2
    var line3 by Addresses.line3
    var city by Addresses.cityOrTown
    var country by Addresses.country
    var province by Addresses.stateOrProvince
    var postalCode by Addresses.postalCode
    var isDefault by Addresses.isDefault
    
    fun toDTO() = CreateAddressDTO(
        id = id.value,
        userId = user.value,
        line1 = line1,
        line2 = line2,
        line3 = line3,
        city = city,
        province = province,
        country = country,
        postCode = postalCode,
        isDefault = isDefault,
    )
}