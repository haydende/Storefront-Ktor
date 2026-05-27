package haydende.storefront.util

import org.jetbrains.exposed.v1.jdbc.Database
import io.ktor.server.application.*

class DatabaseUtils(environment: ApplicationEnvironment) {

    val connection: Database

    init {
        connection = Database.connect(
            environment.config.property("database.connection-string").getString(),
//            environment.config.property("database.driver-class-name").getString(),
        )
    }

    companion object {

        @Volatile
        private var instance: DatabaseUtils? = null

        fun getInstance(environment: ApplicationEnvironment): DatabaseUtils =
            instance ?: synchronized(this) {
                instance ?: DatabaseUtils(environment).also { instance = it }
            }

    }

}