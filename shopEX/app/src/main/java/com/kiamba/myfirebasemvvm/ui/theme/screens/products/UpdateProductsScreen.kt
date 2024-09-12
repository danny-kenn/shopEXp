package com.kiamba.myfirebasemvvm.ui.theme.screens.products

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.kiamba.myfirebasemvvm.data.productviewmodel
import com.kiamba.myfirebasemvvm.model.Upload
import com.kiamba.myfirebasemvvm.navigation.ROUTE_VIEW_UPLOAD

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProductScreen(
    navController: NavHostController,
    Id: String
) {
    val context = LocalContext.current
    val productRepository = productviewmodel(navController, context)

    var product by remember { mutableStateOf<Upload?>(null) }
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    // Fetch product details
    LaunchedEffect(Id) {
        productRepository.getProductById(Id) { fetchedProduct ->
            if (fetchedProduct != null) {
                name = fetchedProduct.name
                quantity = fetchedProduct.quantity
                price = fetchedProduct.price
                imageUri = Uri.parse(fetchedProduct.imageUrl)
            }
            isLoading.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Product", fontSize = 20.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        },
        content = { paddingValues ->
            if (isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Product Name TextField
                    TextField(
                        value = name,
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        onValueChange = { name = it },
                        label = { Text("Product Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedLabelColor = Color(0xFF6200EE)
                        )
                    )

                    // Quantity TextField
                    TextField(
                        value = quantity,
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        onValueChange = { quantity = it },
                        label = { Text("Quantity") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedLabelColor = Color(0xFF6200EE)
                        )
                    )

                    // Price TextField
                    TextField(
                        value = price,
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        onValueChange = { price = it },
                        label = { Text("Price") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedLabelColor = Color(0xFF6200EE)
                        )
                    )

                    // Image Picker
                    ImagePickerComponent(imageUri, onImageUriSelected = { uri -> imageUri = uri })

                    // Update Product Button
                    Button(
                        onClick = {
                            if (name.isNotEmpty() && quantity.isNotEmpty() && price.isNotEmpty()) {
                                if (imageUri != null) {
                                    productRepository.updateProductWithImage(name, quantity, price, imageUri!!, Id)
                                } else {
                                    productRepository.updateProduct(name, quantity, price, Id)
                                }
                                navController.navigate(ROUTE_VIEW_UPLOAD) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text("Update Product", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
    )
}

@Composable
fun ImagePickerComponent(
    imageUri: Uri?,
    onImageUriSelected: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        onImageUriSelected(uri)
    }

    Column(modifier = modifier) {
        if (imageUri != null) {
            Image(
                painter = rememberImagePainter(imageUri),
                contentDescription = "Selected image",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Change Image", color = Color.White)
            }
        } else {
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Select Image", color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun PreviewUpdateProductScreen() {
    UpdateProductScreen(rememberNavController(), Id = "sampleId")
}
