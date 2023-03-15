package uk.ac.bath.cm20314.refill.data.product

import com.google.firebase.database.Exclude

data class Product(
    @Exclude @JvmField var categoryId: String = "",
    @Exclude @JvmField var productId: String = "",
    var productName: String = "",
    var thumbnail: Int = 0,
    var pricePerKg: Int = 0,
    var portionSize: Float = 0f,
    var isUpdated: Boolean = false
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "categoryName" to categoryId,
            "productName" to productName,
            "pricePerKg" to pricePerKg,
            "portionSize" to portionSize,
            "isUpdated" to isUpdated
        )
    }
}