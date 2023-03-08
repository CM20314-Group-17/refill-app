package uk.ac.bath.cm20314.refill.data.product

import android.content.ContentValues.TAG
import android.util.Log
import androidx.datastore.preferences.protobuf.Value
import com.google.firebase.FirebaseException
import com.google.firebase.database.*
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

private lateinit var database: DatabaseReference

object ProductRepositoryImpl : ProductRepository {

    private var ValueDataListener: ValueEventListener? = null
    private var productRead: Product? = null

    private fun getDatabaseRef(): DatabaseReference? {
        return FirebaseDatabase.getInstance().reference.child("Categories")
    }

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
        //database =FirebaseDatabase.getInstance().getReference("Categories")
        //database.child(categoryId).child(productId).child("0").get().addOnSuccessListener {
        //    val name = it.child("name").value
        //    val portionSize = it.child("portionSize").value
        //    val pricePerKg = it.child("pricePerKg").value
        //    val updated = it.child("updated").value
        //   Log.i("firebase","Got value $pricePerKg")//Test to see if database can read
        //}
        //return Product(id = "test", name = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)

        //database = FirebaseDatabase.getInstance().getReference("Categories")
        //val product = database.child(categoryId).child(productId).awaitsSingle()
        //Log.i("Firebase", "Got value $product")
        //return Product(id = "test", name = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)



        ValueDataListener = object: ValueEventListener {
            override fun onDataChange(productSnapshot: DataSnapshot) {
                try {
                    Log.i("Firebase", "Change to database detected")
                    if (productSnapshot != null) {
                        try {
                            val data: HashMap<String, Any> = productSnapshot.value as HashMap<String, Any>
                            val id = data["id"] as String
                            val name = data["name"] as String
                            val pricePerKg = data["pricePerKg"] as Int
                            val portionSize = data["portionSize"] as Float
                            val isUpdated = data["isUpdated"] as Boolean
                            productRead = Product(id, name, pricePerKg, portionSize, isUpdated)
                            Log.i("Firebase", "Successfully read ${id}, ${name}, ${pricePerKg}, ${portionSize}, $isUpdated")
                        }
                        catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                    }
                }
                catch (exception: Exception) {
                    Log.i("Firebase", "Error reading")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if(error != null) {
                    Log.i("Firebase", "Data update cancelled on read: ${error.message}")
                }
            }
        }
        getDatabaseRef()?.addValueEventListener(ValueDataListener as ValueEventListener)
        return productRead
    }


    suspend fun DatabaseReference.awaitsSingle(): DataSnapshot? =
        suspendCancellableCoroutine { continuation ->
            val productListener = object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    val exception = when (error.toException()) {
                        is FirebaseException -> error.toException()
                        else -> Exception("Firebase call for reference $this was cancelled")
                    }
                    continuation.resumeWithException(exception)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        continuation.resume(snapshot)
                    } catch (exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                }
            }
            continuation.invokeOnCancellation { this.removeEventListener(productListener)
                this.addListenerForSingleValueEvent(productListener)
            }
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
                Log.e(TAG, "Error", databaseError.toException())
            }
        })
    }
}

private fun <T> CancellableContinuation<T>.resume(value: T) {

}
