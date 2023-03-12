package uk.ac.bath.cm20314.refill.data.product

import com.google.firebase.database.Exclude

data class Product(
    val categoryName: String = "",
    var productName: String = "",
    var pricePerKg: Int = 0,
    var portionSize: Float = 0f,
    var isUpdated: Boolean = true
){

    @Exclude
    fun toMap(): Map<String, Any?> {
    return mapOf(
        "categoryName" to categoryName,
        "productName" to productName,
        "pricePerKg" to pricePerKg,
        "portionSize" to portionSize,
        "isUpdated" to isUpdated
    )
}}