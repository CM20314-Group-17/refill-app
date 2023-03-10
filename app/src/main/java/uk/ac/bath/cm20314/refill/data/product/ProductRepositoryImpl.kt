package uk.ac.bath.cm20314.refill.data.product

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.*


object ProductRepositoryImpl : ProductRepository {

    // add all products at read to list, use list to getProduct
    private var ValueDataListener: ValueEventListener? = null
    private var productList: ArrayList<DataSnapshot>? = ArrayList()

    private fun getDatabaseRef(): DatabaseReference? {
        return FirebaseDatabase.getInstance().reference.child("Categories")
    }

    // Initialise Listener
    init {
        if (ValueDataListener != null) {
            getDatabaseRef()?.removeEventListener(ValueDataListener!!)
        }
        ValueDataListener = null
        Log.i("Firebase", "Read data initialised")

        ValueDataListener = object: ValueEventListener {
            override fun onDataChange(productSnapshot: DataSnapshot) {
                try {
                    Log.i("Firebase", "Change to database detected")
                    val data: ArrayList<DataSnapshot> = ArrayList()
                    if (productSnapshot != null) {
                        for (snapshot: DataSnapshot in productSnapshot.children) {
                            for (snapshot2: DataSnapshot in snapshot.children){
                                try {
                                    data.add(snapshot2)
                                }
                                catch (exception: Exception) {
                                    exception.printStackTrace()
                                }
                            }
                        }
                        productList = data
                        Log.i("Firebase", "New data read, ${productList!!.size} entries")
                    }
                } catch (exception: Exception) {
                    Log.i("Firebase", "Error reading")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (error != null) {
                    Log.i("Firebase", "Data update cancelled, error: ${error.message}")
                }
            }
        }
        getDatabaseRef()?.addValueEventListener(ValueDataListener as ValueEventListener)
    }

    override suspend fun getProducts(categoryId: String): List<Product> {
        // TODO: Replace with products retrieved from the database.
        return listOf(
            Product(categoryName = "test", productName = "Spaghetti", pricePerKg = 9, portionSize = 100f),
            Product(categoryName = "test", productName = "Pennette (White)", pricePerKg = 8, portionSize = 100f),
            Product(categoryName = "test", productName = "Pennette (Wholewheat)", pricePerKg = 9, portionSize = 100f),
            Product(categoryName = "test", productName = "Tagliatelle", pricePerKg = 5, portionSize = 100f),
            Product(categoryName = "test", productName = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)
        )
    }


    override suspend fun getProduct(categoryId: String, productId: String): Product? {
        // Find the product, if it exists return it
        for (item: DataSnapshot in productList!!) {
            val product: HashMap<Any, Any> = item.value as HashMap<Any, Any>
            if (product["productName"] == productId) {
                val gotProduct = item.getValue(Product::class.java)
                Log.i("Firebase", "Product found $item")
                return gotProduct
            }
        }
        Log.i("Firebase", "getProduct called, item not found")
        return Product("2", "2", 2, 2f, true)
    }

    override suspend fun updateProduct(product: Product) {
        TODO()
    }

    override suspend fun createProduct(
        categoryId: String,
        productId: String,
        pricePerKg: Int,
        portionSize: Float,
        isUpdated: Boolean
    ): Product? {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Categories")
        val query = myRef.orderByKey().equalTo(categoryId)
        val product = Product(categoryId, productId, pricePerKg, portionSize, isUpdated)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (categorySnapshot in dataSnapshot.children) {
                    val newItemRef = categorySnapshot.child(productId).ref
                    newItemRef.setValue(product)
                    Log.d(TAG, "Item added")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error", databaseError.toException())
            }
        })
        return product
    }

    override suspend fun deleteProduct(productId: String, categoryId: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Categories")
        val query = myRef.orderByKey().equalTo(categoryId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (categorySnapshot in dataSnapshot.children) {
                    val itemQuery = categorySnapshot.child(productId).ref
                    itemQuery.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error", databaseError.toException())
            }
        })
    }
}