package com.kiamba.myfirebasemvvm.ui.theme.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kiamba.myfirebasemvvm.navigation.*

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
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                    .background(Color(0xFF070707)), // Light gray background for a clean look
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to the Home Page",
                    color = Color(0xFFFAF8F8), // Slightly softer black
                    fontSize = 28.sp, // Increased font size for a more prominent title
                    fontWeight = FontWeight.SemiBold, // Added font weight for emphasis
                    modifier = Modifier.padding(16.dp)
                )
            }
        },


        bottomBar = {
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
    )
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(rememberNavController())
}
