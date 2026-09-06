package haydende.storefront.service

import haydende.storefront.exception.AddressNotFoundException
import haydende.storefront.model.Addresses
import haydende.storefront.model.Users
import haydende.storefront.model.dao.Address
import haydende.storefront.model.dto.CreateAddressDTO
import haydende.storefront.model.dto.UpdateAddressDTO
import haydende.storefront.util.DatabaseUtils
import io.ktor.server.application.ApplicationEnvironment
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class AddressService(environment: ApplicationEnvironment) {

    val databaseUtils = DatabaseUtils.getInstance(environment)

    init {
        transaction {
            SchemaUtils.create(Addresses)
        }
    }

    fun getAddressesForUserId(id: Int) = transaction {
        Address.find { Addresses.user eq id }.toList()
    }

    fun saveNewAddress(addressDto: CreateAddressDTO) = transaction {
        Address.new {
            user = EntityID(addressDto.userId, Users)
            line1 = addressDto.line1
            addressDto.line2?.let { line2 = it }
            addressDto.line3?.let { line3 = it }
            city = addressDto.city
            country = addressDto.country
            province = addressDto.province
            postalCode = addressDto.postCode
            isDefault = addressDto.isDefault
        }
    }

    fun updateAddress(addressDto: UpdateAddressDTO) = transaction {
        Address.findByIdAndUpdate(addressDto.id) { address ->
            addressDto.line1?.let { address.line1 = it }
            addressDto.line2?.let { address.line2 = it }
            addressDto.line3?.let { address.line3 = it }
            addressDto.city?.let { address.city = it }
            addressDto.country?.let { address.country = it }
            addressDto.province?.let { address.province = it }
            addressDto.postalCode?.let { address.postalCode = it }
            addressDto.isDefault?.let { address.isDefault = it }

        } ?: throw AddressNotFoundException("Address with ID ${addressDto.id} not found")
    }

    fun deleteAddress(addressId: Int) = transaction {
        try {
            Address[addressId].delete()
        } catch (enf: EntityNotFoundException) {
            LOG.error("Address with ID $addressId not found. Rethrowing as AddressNotFoundException:", enf)
            throw AddressNotFoundException("Address with ID $addressId not found")
        }
    }

    companion object {
        @Volatile
        private var instance: AddressService? = null
        private val LOG = org.slf4j.LoggerFactory.getLogger(AddressService::class.java)

        fun getInstance(environment: ApplicationEnvironment): AddressService =
            instance ?: synchronized(this) {
                instance ?: AddressService(environment).also { instance = it }
            }
    }
}