package uk.ac.bath.cm20314.refill.data.category

data class Category(
    var categoryName: String,
    val itemCount: Int,
    var isUpdated: Boolean = true
)