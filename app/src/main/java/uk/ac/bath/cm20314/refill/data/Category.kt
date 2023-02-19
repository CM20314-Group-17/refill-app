package uk.ac.bath.cm20314.refill.data

// TODO: temporary - update when the database is added.
data class Category(
    val id: String,
    val name: String,
    val itemCount: Int,
    val isUpdated: Boolean = true
)