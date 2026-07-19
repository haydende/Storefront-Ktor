package haydende.storefront.util

import io.ktor.server.application.ApplicationEnvironment
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

class EncryptionUtils(environment: ApplicationEnvironment) {

    val cipher: Cipher

    init {
        val secretKey = KeyGenerator.getInstance("AES").let {
            it.init(256)
            it.generateKey()
        }
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivParameterSpec = IvParameterSpec(ByteArray(16))
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
    }

    fun encrypt(data: String): ByteArray = cipher.doFinal(data.toByteArray())

    companion object {

        @Volatile
        private var instance: EncryptionUtils? = null

        fun getInstance(environment: ApplicationEnvironment): EncryptionUtils =
            instance ?: synchronized(this) {
                instance ?: EncryptionUtils(environment).also { instance = it }
            }
    }


}