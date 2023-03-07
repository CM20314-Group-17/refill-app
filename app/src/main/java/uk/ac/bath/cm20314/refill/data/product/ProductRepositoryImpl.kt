package uk.ac.bath.cm20314.refill.data.product

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.*

private lateinit var database: DatabaseReference

object ProductRepositoryImpl : ProductRepository {



    override suspend fun getProducts(categoryId: String): List<Product> {
        // TODO: Replace with products retrieved from the database.
        return listOf(
            Product(id = "test", name = "Spaghetti", pricePerKg = 9, portionSize = 100f),
            Product(id = "test", name = "Pennette (White)", pricePerKg = 8, portionSize = 100f),
            Product(id = "test", name = "Pennette (Wholewheat)", pricePerKg = 9, portionSize = 100f),
            Product(id = "test", name = "Tagliatelle", pricePerKg = 5, portionSize = 100f),
            Product(id = "test", name = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)
        )
    }


    override suspend fun getProduct(productId: String, categoryId: String): Product? {//need category id too
        database =FirebaseDatabase.getInstance().getReference("Categories")
        database.child(categoryId).child(productId).child("0").get().addOnSuccessListener {
            val name = it.child("name").value
            val portionSize = it.child("portionSize").value
            val pricePerKg = it.child("pricePerKg").value
            val updated = it.child("updated").value
            Log.i("firebase","Got value $pricePerKg")//Test to see if database can read
        }
        return Product(id = "test", name = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)
    }

    override suspend fun updateProduct(product: Product) {
        TODO()
    }

    override suspend fun createProduct(categoryId:String,productId: String,name: String, pricePerKg: Int, portionSize: Float, isUpdated: Boolean) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Categories")
        val query = myRef.orderByKey().equalTo(categoryId)
        val product = Product(productId, name, pricePerKg, portionSize, isUpdated)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (categorySnapshot in dataSnapshot.children) {
                    val newItemRef = categorySnapshot.child(name).ref
                    newItemRef.setValue(product)
                    Log.d(TAG, "Item added")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error", databaseError.toException())
            }
        })

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
                Log.e(TAG, "ERROR", databaseError.toException())
            }
        })
    }
}