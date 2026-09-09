package haydende.storefront.model.dao

import haydende.storefront.model.table.AddressTable
import haydende.storefront.model.dto.CreateAddressDTO
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class Address(id: EntityID<Int>): IntEntity(id) {

    companion object : IntEntityClass<Address>(AddressTable)

    var user by AddressTable.user
    var line1 by AddressTable.line1
    var line2 by AddressTable.line2
    var line3 by AddressTable.line3
    var city by AddressTable.cityOrTown
    var country by AddressTable.country
    var province by AddressTable.stateOrProvince
    var postalCode by AddressTable.postalCode
    var isDefault by AddressTable.isDefault
    
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