package uk.ac.bath.cm20314.refill.data.product

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.*

private lateinit var database: DatabaseReference

object ProductRepositoryImpl : ProductRepository {



    override suspend fun getProducts(categoryId: String): List<Product> {
        // TODO: Replace with products retrieved from the database.
        return listOf(
            Product(categoryName = "Pasta",productName = "Spaghetti", pricePerKg = 9, portionSize = 100f),
            Product(categoryName = "Pasta",productName = "Pennette (White)", pricePerKg = 8, portionSize = 100f),
            Product(categoryName = "Pasta",productName = "Pennette (Wholewheat)", pricePerKg = 9, portionSize = 100f),
            Product(categoryName = "Pasta",productName = "Tagliatelle", pricePerKg = 5, portionSize = 100f),
            Product(categoryName = "Pasta",productName = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)
        )
    }


    override suspend fun getProduct(productName: String, categoryName: String): Product? {//need category id too
        database =FirebaseDatabase.getInstance().getReference("Categories")
        database.child(categoryName).child(productName).child("0").get().addOnSuccessListener {
            val name = it.child("name").value
            val portionSize = it.child("portionSize").value
            val pricePerKg = it.child("pricePerKg").value
            val updated = it.child("updated").value
            Log.i("firebase","Got value $pricePerKg")//Test to see if database can read
        }
        return Product(categoryName = "Pasta",productName = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)
    }

    override suspend fun updateProduct(product: Product) {
        deleteProduct(product.productName,product.categoryName)
        createProduct(product.categoryName,product.productName,product.pricePerKg,product.portionSize,product.isUpdated)
    }

    override suspend fun createProduct(categoryName:String,productName: String, pricePerKg: Int, portionSize: Float, isUpdated: Boolean):Product? {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Categories")
        val query = ref.orderByKey().equalTo(categoryName)
        val product = Product(categoryName,productName, pricePerKg, portionSize, isUpdated)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (categorySnapshot in dataSnapshot.children) {
                    val newRef = categorySnapshot.child(productName).ref
                    val productValues = product.toMap().filterKeys { key -> key != "categoryName" }
                    newRef.setValue(productValues)
                    Log.d(TAG, "Item added")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error", databaseError.toException())
            }
        })
        return product
    }


    override suspend fun deleteProduct(productName: String, categoryName: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Categories")
        val query = ref.orderByKey().equalTo(categoryName)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (categorySnapshot in dataSnapshot.children) {
                    val itemQuery = categorySnapshot.child(productName).ref
                    itemQuery.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error", databaseError.toException())
            }
        })
    }
}