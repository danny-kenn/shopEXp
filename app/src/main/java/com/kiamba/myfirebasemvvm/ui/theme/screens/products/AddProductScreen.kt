package com.kiamba.myfirebasemvvm.ui.theme.screens.products


import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kiamba.myfirebasemvvm.data.productviewmodel
import com.kiamba.myfirebasemvvm.navigation.ROUTE_VIEW_PRODUCT
import com.kiamba.myfirebasemvvm.navigation.ROUTE_VIEW_UPLOAD

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavHostController, isUpdate: Boolean = false) {
    val context = LocalContext.current
    var productName by remember { mutableStateOf(TextFieldValue("")) }
    var productQuantity by remember { mutableStateOf(TextFieldValue("")) }
    var productPrice by remember { mutableStateOf(TextFieldValue("")) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var hasImage by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            hasImage = uri != null
            imageUri = uri
        }
    )

    // Main background and layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0C)),
        contentAlignment = Alignment.TopCenter
    ) {
        // Top navigation bar
        TopAppBar(
            title = {
                Text(
                    text =  "Add New Product",
                    color = Color.White,
                    fontSize = 20.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF00695C) // Set the background color here
            ),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }
        )


        // Inner content layout
        Column(
            modifier = Modifier
                .padding(top = 150.dp, start = 20.dp, end = 20.dp, bottom = 20.dp) // Increased padding at the top to lower the card
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title with a modern font and style
            Text(
                text = "Add New Product",
                fontSize = 28.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF00695C),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Product fields
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text(text = "Product Name", color = Color(0xFF0A0A0A)) },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = productQuantity,
                onValueChange = { productQuantity = it },
                label = { Text(text = "Product Quantity", color = Color(0xFF0A0A0A)) },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = productPrice,
                onValueChange = { productPrice = it },
                label = { Text(text = "Product Price", color = Color(0xFF090909)) },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Image picker with a circular image preview
            if (hasImage && imageUri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to select image
            Button(
                onClick = { imagePicker.launch("image/*") },
                colors = ButtonDefaults.buttonColors(Color(0xFF00695C)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Select Image", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Save and upload button
            Button(
                onClick = {
                    if (productName.text.trim().isNotEmpty() && productQuantity.text.trim().isNotEmpty() && productPrice.text.trim().isNotEmpty()) {
                        if (hasImage && imageUri != null) {
                            val productRepository = productviewmodel(navController, context)
                            productRepository.saveProduct(
                                productName.text.trim(),
                                productQuantity.text.trim(),
                                productPrice.text
                            )
                            productRepository.saveProductWithImage(
                                productName.text.trim(),
                                productQuantity.text.trim(),
                                productPrice.text,
                                imageUri!!
                            )
                            navController.navigate(ROUTE_VIEW_UPLOAD)
                        } else {
                            Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill in all product details", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Save and Upload", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddOrUpdateProductScreen() {
    val navController = rememberNavController()
    AddProductScreen(navController, isUpdate = false)
}
