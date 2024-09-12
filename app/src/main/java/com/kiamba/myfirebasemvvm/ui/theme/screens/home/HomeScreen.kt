package com.kiamba.myfirebasemvvm.ui.theme.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kiamba.myfirebasemvvm.navigation.ROUTE_ADD_PRODUCT
import com.kiamba.myfirebasemvvm.navigation.ROUTE_HOME
import com.kiamba.myfirebasemvvm.navigation.ROUTE_PROFILE
import com.kiamba.myfirebasemvvm.navigation.ROUTE_REGISTER
import com.kiamba.myfirebasemvvm.navigation.ROUTE_VIEW_UPLOAD

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Home",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUTE_PROFILE) }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Hello", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00695C), // Refined blue shade
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFECECEC)) // Light background for a clean look
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to the Home Page",
                    color = Color(0xFF333333), // Darker color for better readability
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // Add some cards or sections
                HomeSection(
                    title = "Featured Products",
                    icon = Icons.Default.ShoppingCart,
                    description = "Check out our latest products.",
                    navController = navController,
                    route = ROUTE_VIEW_UPLOAD
                )

                Spacer(modifier = Modifier.height(16.dp))

                HomeSection(
                    title = "Add New Product",
                    icon = Icons.Default.AddCircle,
                    description = "Add new products to your store.",
                    navController = navController,
                    route = ROUTE_ADD_PRODUCT
                )

                Spacer(modifier = Modifier.height(16.dp))

                HomeSection(
                    title = "Profile",
                    icon = Icons.Default.Person,
                    description = "View and edit your profile.",
                    navController = navController,
                    route = ROUTE_PROFILE
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF00695C), // Matching the top bar color
                contentColor = Color.White,
                tonalElevation = 4.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate(ROUTE_HOME) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "View Products") },
                    label = { Text("Products") },
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
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { navController.navigate(ROUTE_PROFILE) }
                )
            }
        }
    )
}

@Composable
fun HomeSection(
    title: String,
    icon: ImageVector,
    description: String,
    navController: NavHostController,
    route: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(route) }
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,  // Correct usage of ImageVector
                contentDescription = title,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF00695C)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 16.sp,
                color = Color(0xFF757575),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(rememberNavController())
}