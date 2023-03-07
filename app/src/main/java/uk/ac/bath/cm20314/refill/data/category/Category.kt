package uk.ac.bath.cm20314.refill.data.category

data class Category(
    val id: String,
    var categoryName: String,
    val itemCount: Int,
    var isUpdated: Boolean = true
)