package uk.ac.bath.cm20314.refill.data.product

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private lateinit var database: DatabaseReference

object ProductRepositoryImpl : ProductRepository {



    override suspend fun getProducts(categoryId: String): List<Product> {
        // TODO: Replace with products retrieved from the database.
        return listOf(
            //Product(id = "test", name = "Spaghetti", pricePerKg = 9, portionSize = 100f),
            //Product(id = "test", name = "Pennette (White)", pricePerKg = 8, portionSize = 100f),
            //Product(id = "test", name = "Pennette (Wholewheat)", pricePerKg = 9, portionSize = 100f),
            //Product(id = "test", name = "Tagliatelle", pricePerKg = 5, portionSize = 100f),
            //Product(id = "test", name = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)
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
        return Product(id = "3", name = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)
    }

    override suspend fun updateProduct(product: Product) {
        TODO()
    }

    override suspend fun createProduct(category: String, id: String, name: String, pricePerKg: Int, portionSize: Float): Product {
        database = FirebaseDatabase.getInstance().getReference("Categories").child(category).child(id)
        val product = Product(id="3",name, pricePerKg, portionSize)
        database.setValue(product)
        return product
    }

    override suspend fun deleteProduct(productId: String) {
        TODO()
    }
}