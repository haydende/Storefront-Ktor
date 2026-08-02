package haydende.storefront.service

import haydende.storefront.model.Users
import haydende.storefront.model.dao.User
import haydende.storefront.model.dto.CreateUserDTO
import haydende.storefront.model.dto.UpdateUserDTO
import haydende.storefront.util.DatabaseUtils
import haydende.storefront.util.EncryptionUtils
import io.ktor.server.application.ApplicationEnvironment
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserService(environment: ApplicationEnvironment) {

    var databaseUtils: DatabaseUtils = DatabaseUtils.getInstance(environment)
    var encryptionUtils: EncryptionUtils = EncryptionUtils.getInstance(environment)

    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    fun getUserById(id: Int): User? = transaction {
        User.findById(id)
    }

    fun saveNewUser(createUserDto: CreateUserDTO) = transaction {
        User.new {
            firstName = createUserDto.firstName
            lastName = createUserDto.lastName
            email = createUserDto.email
            password = encryptPassword(createUserDto.password!!)
            phone = createUserDto.phone
            profilePicB64 = createUserDto.profilePicB64
        }
    }

    fun updateUser(userDto: UpdateUserDTO) = transaction {
        User.findByIdAndUpdate(userDto.id) {
            userDto.firstName?.let { firstName -> it.firstName = firstName }
            userDto.lastName?.let { lastName -> it.lastName = lastName }
            userDto.email?.let { email -> it.email = email }
            userDto.phone?.let { phone -> it.phone = phone }
            userDto.profilePicB64?.let { profilePicB64 -> it.profilePicB64 = profilePicB64 }
        }
    }

    fun deleteUser(userId: Int) = transaction {
        User[userId].delete()
    }

    fun updateUserPassword(userId: Int, password: String) = transaction {
        User.findByIdAndUpdate(userId) {
            it.password = encryptPassword(password)
        }
    }

    private fun encryptPassword(password: String): String = encryptionUtils
        .encrypt(password)
        .contentToString()

    companion object {

        @Volatile
        private var instance: UserService? = null

        fun getInstance(environment: ApplicationEnvironment): UserService =
            instance ?: synchronized(this) {
                instance ?: UserService(environment).also { instance = it }
            }
    }
}