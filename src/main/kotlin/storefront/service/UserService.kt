package haydende.storefront.service

import haydende.storefront.model.Users
import haydende.storefront.model.dao.User
import haydende.storefront.util.DatabaseUtils
import io.ktor.server.application.ApplicationEnvironment
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserService(environment: ApplicationEnvironment) {

    var databaseUtils: DatabaseUtils = DatabaseUtils.getInstance(environment)

    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    fun getUserById(id: Int): User? = transaction {
        User.findById(id)
    }

    companion object {

        @Volatile
        private var instance: UserService? = null

        fun getInstance(environment: ApplicationEnvironment): UserService =
            instance ?: synchronized(this) {
                instance ?: UserService(environment).also { instance = it }
            }
    }
}