package haydende.storefront.model.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class UpdateBasketDTO(
    val id: Int,
    val userId: Int? = null,
    val products: List<BasketProductsDTO> = listOf(),
    var status: String? = null,
    var created: LocalDateTime? = null,
    var lastModified: LocalDateTime? = null,
)