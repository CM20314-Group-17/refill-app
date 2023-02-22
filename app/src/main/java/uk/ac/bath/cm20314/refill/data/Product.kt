package uk.ac.bath.cm20314.refill.data

// TODO: temporary - update when the database is added.
data class Product(
    val id: String,
    val name: String,
    val price_per_kg: Int,
    val portion_size: Float,
    val isUpdated: Boolean = true
)