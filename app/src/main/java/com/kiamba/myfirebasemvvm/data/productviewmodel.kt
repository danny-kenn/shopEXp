package com.kiamba.myfirebasemvvm.data

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.kiamba.myfirebasemvvm.model.Product
import com.kiamba.myfirebasemvvm.model.Upload
import com.kiamba.myfirebasemvvm.navigation.ROUTE_LOGIN
import java.io.File
import java.io.FileOutputStream


class productviewmodel(var navController: NavHostController, var context: Context) {
    var authRepository: AuthViewModel
    var progress: ProgressDialog

    init {
        authRepository = AuthViewModel(navController, context)
        if (!authRepository.isloggedin()) {
            navController.navigate(ROUTE_LOGIN)
        }
        progress = ProgressDialog(context)
        progress.setTitle("Loading")
        progress.setMessage("Please wait...")
    }


    fun saveProduct(productName: String, productQuantity: String, productPrice: String) {
        var id = System.currentTimeMillis().toString()
        var productData = Product(productName, productQuantity, productPrice, id)
        var productRef = FirebaseDatabase.getInstance().getReference()
            .child("Products/$id")
        progress.show()
        productRef.setValue(productData).addOnCompleteListener {
            progress.dismiss()
            if (it.isSuccessful) {
                Toast.makeText(context, "Saving successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "ERROR: ${it.exception!!.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun viewProducts(
        product: MutableState<Product>,
        products: SnapshotStateList<Product>
    ): SnapshotStateList<Product> {
        var ref = FirebaseDatabase.getInstance().getReference().child("Products")

        progress.show()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progress.dismiss()
                products.clear()
                for (snap in snapshot.children) {
                    val value = snap.getValue(Product::class.java)
                    product.value = value!!
                    products.add(value)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        return products
    }

    // Function to delete a product and associated image (if present)
    fun deleteProduct(id: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        println("Attempting to delete product with id: $id")  // Debugging print
        getProductById(id) { product ->
            if (product != null) {
                println("Product found: $id")  // Debugging print
                // First, delete the product details from the database
                deleteProductFromDatabase(id) {
                    println("Product details deleted successfully. Proceeding to delete image if present.")  // Debugging print

                    // After product details are deleted, check if an image exists and delete it
                    if (product.imageUrl.isNotEmpty()) {
                        println("Product has an image, attempting to delete the image.")  // Debugging print
                        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(product.imageUrl)
                        storageReference.delete().addOnCompleteListener { deleteImageTask ->
                            if (deleteImageTask.isSuccessful) {
                                println("Image deleted successfully.")  // Debugging print
                            } else {
                                println("Failed to delete image: ${deleteImageTask.exception?.message}")  // Debugging print
                                Toast.makeText(context, "Failed to delete product image: ${deleteImageTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener { exception ->
                            println("Error while deleting image: ${exception.message}")  // Debugging print
                            Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Product deletion is complete (whether or not image exists or image deletion succeeds)
                    onSuccess()
                }
            } else {
                println("Product not found.")  // Debugging print
                progress.dismiss()
                Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Helper function to delete product record from the Firebase Realtime Database
    private fun deleteProductFromDatabase(id: String, onSuccess: () -> Unit) {
        println("Deleting product from database with id: $id")  // Debugging print
        val delRef = FirebaseDatabase.getInstance().getReference("Uploads/$id")
        progress.show()

        delRef.removeValue().addOnCompleteListener { task ->
            progress.dismiss()
            if (task.isSuccessful) {
                println("Product deleted successfully from the database.")  // Debugging print
                Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show()
                onSuccess() // Proceed to delete the image after the product details
            } else {
                println("Error deleting product from database: ${task.exception?.message}")  // Debugging print
                Toast.makeText(context, "Error deleting product: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            println("Error while deleting product: ${exception.message}")  // Debugging print
            progress.dismiss()
            Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }





    fun updateProduct(name: String, quantity: String, price: String, id: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Uploads/$id")
        val updatedProduct = Product(name, quantity, price, id)
        progress.show()

        databaseRef.setValue(updatedProduct).addOnCompleteListener { task ->
            progress.dismiss()
            if (task.isSuccessful) {
                Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun saveProductWithImage(
        productName: String,
        productQuantity: String,
        productPrice: String,
        filePath: Uri
    ) {
        var id = System.currentTimeMillis().toString()
        var storageReference = FirebaseStorage.getInstance().getReference().child("Uploads/$id")
        progress.show()

        storageReference.putFile(filePath).addOnCompleteListener {
            progress.dismiss()
            if (it.isSuccessful) {
                // Proceed to store other data into the db
                storageReference.downloadUrl.addOnSuccessListener {
                    var imageUrl = it.toString()
                    var houseData = Upload(
                        productName, productQuantity,
                        productPrice, imageUrl, id
                    )
                    var dbRef = FirebaseDatabase.getInstance()
                        .getReference().child("Uploads/$id")
                    dbRef.setValue(houseData)
                    Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, it.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun viewUploads(
        upload: MutableState<Upload>,
        uploads: SnapshotStateList<Upload>
    ): SnapshotStateList<Upload> {
        var ref = FirebaseDatabase.getInstance().getReference().child("Uploads")

        progress.show()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progress.dismiss()
                uploads.clear()
                for (snap in snapshot.children) {
                    val value = snap.getValue(Upload::class.java)
                    upload.value = value!!
                    uploads.add(value)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        return uploads
    }


    fun uriToFile(uri: Uri, context: Context, callback: (File?) -> Unit) {
        try {
            val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            callback(file)
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null) // Return null if there's an error
        }
    }


    fun updateProductWithImage(
        productName: String,
        productQuantity: String,
        productPrice: String,
        imageUri: Uri?,
        id: String
    ) {
        val storageReference = FirebaseStorage.getInstance().getReference("Uploads/$id")

        // If an image is selected, upload it and update the product details
        if (imageUri != null) {
            progress.show()

            storageReference.putFile(imageUri).addOnCompleteListener { task ->
                progress.dismiss()
                if (task.isSuccessful) {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        // Create the updated product object with the new image URL
                        val updatedProduct =
                            Upload(productName, productQuantity, productPrice, imageUrl, id)

                        // Update the product in Firebase Realtime Database
                        val uploadRef = FirebaseDatabase.getInstance().getReference("Uploads/$id")
                        uploadRef.setValue(updatedProduct).addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    updateTask.exception?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // If no image is selected, fetch the existing image URL and update the other details
            val uploadRef = FirebaseDatabase.getInstance().getReference("Uploads/$id")
            uploadRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val existingProduct = snapshot.getValue(Upload::class.java)
                    if (existingProduct != null) {
                        // Use the existing imageUrl for the update
                        val updatedProduct = Upload(
                            productName,
                            productQuantity,
                            productPrice,
                            existingProduct.imageUrl, // Keep the existing image URL
                            id
                        )

                        // Update the product in the database
                        uploadRef.setValue(updatedProduct).addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    updateTask.exception?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to retrieve product data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }



    fun getProductById(id: String, onResult: (Upload?) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Uploads/$id")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(Upload::class.java)
                if (product != null) {
                    // Check if imageUrl exists
                    if (product.imageUrl.isNotEmpty()) {
                        // Fetch the image URL from Firebase Storage
                        val storageReference =
                            FirebaseStorage.getInstance().getReferenceFromUrl(product.imageUrl)
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            product.imageUrl = uri.toString() // Update with the valid image URL
                            onResult(product)
                        }.addOnFailureListener {
                            // In case fetching the image URL fails, still return the product
                            onResult(product)
                        }
                    } else {
                        onResult(product)
                    }
                } else {
                    onResult(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to fetch product data", Toast.LENGTH_SHORT).show()
                onResult(null)
            }
        })
    }
}