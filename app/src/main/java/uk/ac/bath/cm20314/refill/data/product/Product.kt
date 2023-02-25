package uk.ac.bath.cm20314.refill.data.product

data class Product(
    val id: String,
    var name: String,
    var pricePerKg: Int,
    var portionSize: Float,
    var isUpdated: Boolean = true
)