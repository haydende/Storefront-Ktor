package haydende.storefront.service

import haydende.storefront.model.Users
import haydende.storefront.model.dao.User
import haydende.storefront.model.dto.UserDTO
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

    fun saveNewUser(userDto: UserDTO) = transaction {
        User.new {
            firstName = userDto.firstName
            lastName = userDto.lastName
            email = userDto.email
            password = encryptPassword(userDto.password!!)
            phone = userDto.phone
            profilePicB64 = userDto.profilePicB64
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