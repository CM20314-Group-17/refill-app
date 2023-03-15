package uk.ac.bath.cm20314.refill.data.category

import com.google.firebase.database.Exclude

data class Category(
    @Exclude @JvmField var categoryId: String = "",
    var categoryName: String = "",
    var thumbnail: Int = 0,
    @Exclude @JvmField var itemCount: Int = 0,
    @Exclude @JvmField var isUpdated: Boolean = true
)