package uk.ac.bath.cm20314.refill.data.category

data class Category(
    var categoryName: String = "",
    var thumbnail: Int = 0,
    var itemCount: Int = 0,
    var isUpdated: Boolean = true
)