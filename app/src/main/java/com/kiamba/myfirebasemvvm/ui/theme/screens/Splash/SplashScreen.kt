package com.kiamba.myfirebasemvvm.ui.theme.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kiamba.myfirebasemvvm.R
import com.kiamba.myfirebasemvvm.navigation.ROUTE_HOME
import com.kiamba.myfirebasemvvm.navigation.ROUTE_VIEW_UPLOAD
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    // Delay for splash screen display
    LaunchedEffect(Unit) {
        delay(3000) // Show splash screen for 3 seconds
        navController.navigate(ROUTE_HOME) { // Navigate to home screen
            popUpTo("splash") { inclusive = true }
        }
    }

    // Splash screen UI
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // Background color
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_profile_pic),
                contentDescription = "SplashLogo",
                modifier = Modifier.size(200.dp) // Adjust the size as needed
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
