package uk.ac.bath.cm20314.refill.data.category

import com.google.firebase.database.Exclude

data class Category(
    @Exclude @JvmField var categoryName: String = "",
    // TODO - Cannot save thumbnail as it would be confused with a product.
    @Exclude @JvmField var thumbnail: Int = 0,
    @Exclude @JvmField var itemCount: Int = 0,
    @Exclude @JvmField var isUpdated: Boolean = true
)