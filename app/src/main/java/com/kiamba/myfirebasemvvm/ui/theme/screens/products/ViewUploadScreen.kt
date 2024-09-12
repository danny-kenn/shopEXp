package com.kiamba.myfirebasemvvm.ui.theme.screens.products

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.kiamba.myfirebasemvvm.data.productviewmodel
import com.kiamba.myfirebasemvvm.model.Upload
import com.kiamba.myfirebasemvvm.navigation.ROUTE_ADD_PRODUCT
import com.kiamba.myfirebasemvvm.navigation.ROUTE_HOME
import com.kiamba.myfirebasemvvm.navigation.ROUTE_PROFILE
import com.kiamba.myfirebasemvvm.navigation.ROUTE_REGISTER
import com.kiamba.myfirebasemvvm.navigation.ROUTE_UPDATE_PRODUCT
import com.kiamba.myfirebasemvvm.navigation.ROUTE_VIEW_UPLOAD
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewUploadsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val productRepository = productviewmodel(navController, context)

    val emptyUploadState = remember { mutableStateOf(Upload("", "", "", "", "")) }
    val emptyUploadsListState = remember { mutableStateListOf<Upload>() }

    val uploads = productRepository.viewUploads(emptyUploadState, emptyUploadsListState)

    // State for search query
    val searchQuery = remember { mutableStateOf("") }

    // Filtered uploads based on search query
    val filteredUploads = remember(searchQuery.value) {
        if (searchQuery.value.isEmpty()) {
            uploads
        } else {
            uploads.filter {
                it.name.contains(searchQuery.value, ignoreCase = true) ||
                        it.quantity.contains(searchQuery.value, ignoreCase = true) ||
                        it.price.contains(searchQuery.value, ignoreCase = true)
            }
        }
    }

    // State for search bar visibility
    var isSearchExpanded by remember { mutableStateOf(false) }
    val searchBarWidth by animateDpAsState(
        targetValue = if (isSearchExpanded) 300.dp else 0.dp,
        animationSpec = tween(durationMillis = 300)
    )
    val searchBarAlpha by animateFloatAsState(
        targetValue = if (isSearchExpanded) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isSearchExpanded) {
                            OutlinedTextField(
                                value = searchQuery.value,
                                onValueChange = { query -> searchQuery.value = query },
                                label = { Text("Search Products") },
                                modifier = Modifier
                                    .width(searchBarWidth)
                                    .alpha(searchBarAlpha)
                                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Search,
                                    keyboardType = KeyboardType.Text
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        // Handle search action (optional)
                                    }
                                )
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                isSearchExpanded = !isSearchExpanded
                            }
                        ) {
                            Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00695C), // Soft teal color for elegance
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController) // Bottom Nav Bar
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF1F1F1)), // Soft background color for better contrast
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Your Uploads",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00695C),
                    modifier = Modifier.padding(24.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(24.dp)
                ) {
                    items(filteredUploads) { upload ->
                        SlimUploadCard(
                            name = upload.name,
                            quantity = upload.quantity,
                            price = upload.price,
                            imageUrl = upload.imageUrl,
                            id = upload.id,
                            navController = navController,
                            productRepository = productRepository
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun SlimUploadCard(
    name: String,
    quantity: String,
    price: String,
    imageUrl: String,
    id: String,
    navController: NavHostController,
    productRepository: productviewmodel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(8.dp, RoundedCornerShape(8.dp)), // Add shadow for depth
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Row for product details and image
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Product details on the left
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f), // Take remaining width after image
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00695C),
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Quantity: $quantity",
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Price: KSH$price",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00695C),
                        textAlign = TextAlign.Start
                    )
                }

                // Image on the right side
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(16.dp)) // Rounded corners
                        .background(Color.White) // Background in case image fails
                        .padding(8.dp) // Padding around the image for a cleaner look
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons below product data and image
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly // Center and space between buttons
            ) {
                // Delete Button
                Button(
                    onClick = {
                        // Confirm deletion
                        // Here, you might want to add a confirmation dialog before proceeding
                        productRepository.deleteProduct(
                            id,
                            onSuccess = {
                                // Handle successful deletion
                                // Navigate to ROUTE_VIEW_UPLOAD after successful deletion
                                navController.navigate(ROUTE_VIEW_UPLOAD) {
                                    // Clear the back stack to avoid navigating back to the deleted item
                                    popUpTo(ROUTE_VIEW_UPLOAD) { inclusive = true }
                                }
                            },
                            onFailure = {
                                // Handle failure
                                var isDeleting = false
                                // You might want to show an error message
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB71C1C) // Red for delete
                    )
                ) {
                    Text(text = "Delete", color = Color.White)
                }

                // Update Button
                Button(
                    onClick = {
                        // Navigate to update screen
                        navController.navigate("$ROUTE_UPDATE_PRODUCT/$id")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF004D40) // Dark teal for update
                    )
                ) {
                    Text(text = "Update", color = Color.White)
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color(0xFF00695C), // Matching the top bar color
        contentColor = Color.White,
        tonalElevation = 4.dp // Added elevation for depth
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate(ROUTE_HOME) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "View Products") },
            label = { Text("products") },
            selected = false,
            onClick = { navController.navigate(ROUTE_VIEW_UPLOAD) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Add Product") },
            label = { Text("Add") },
            selected = false,
            onClick = { navController.navigate(ROUTE_ADD_PRODUCT) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Sign Up") },
            label = { Text("Sign Up") },
            selected = false,
            onClick = { navController.navigate(ROUTE_REGISTER) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Login") },
            label = { Text("Profile") },
            selected = false,
            onClick = { navController.navigate(ROUTE_PROFILE) }
        )
    }
}

@Preview
@Composable
fun ViewUploadsPreview() {
    ViewUploadsScreen(rememberNavController())
}
