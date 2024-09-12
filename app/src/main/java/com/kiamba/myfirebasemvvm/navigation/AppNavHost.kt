package com.kiamba.myfirebasemvvm.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiamba.myfirebasemvvm.data.AuthViewModel
import com.kiamba.myfirebasemvvm.ui.ProfileScreen
import com.kiamba.myfirebasemvvm.ui.theme.screens.home.HomeScreen
import com.kiamba.myfirebasemvvm.ui.theme.screens.login.LoginScreen
import com.kiamba.myfirebasemvvm.ui.theme.screens.products.AddProductScreen

import com.kiamba.myfirebasemvvm.ui.theme.screens.products.UpdateProductScreen
import com.kiamba.myfirebasemvvm.ui.theme.screens.products.ViewProductsScreen
import com.kiamba.myfirebasemvvm.ui.theme.screens.products.ViewUploadsScreen
import com.kiamba.myfirebasemvvm.ui.theme.screens.register.RegisterScreen
import com.kiamba.myfirebasemvvm.ui.theme.screens.splash.SplashScreen



@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination // Use the passed startDestination here
    ) {
        composable(ROUTE_SPLASH) {
            SplashScreen(navController)
        }
        composable(ROUTE_HOME) {
            HomeScreen(navController)
        }
        composable(ROUTE_LOGIN) {
            LoginScreen(navController)
        }
        composable(ROUTE_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUTE_ADD_PRODUCT) {
            AddProductScreen(navController)
        }
        composable(ROUTE_VIEW_PRODUCT) {
            ViewProductsScreen(navController)
        }
        composable("$ROUTE_UPDATE_PRODUCT/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            UpdateProductScreen(navController, id)
        }
        composable(ROUTE_VIEW_UPLOAD) {
            ViewUploadsScreen(navController)
        }
        composable(ROUTE_PROFILE) {
            val context = LocalContext.current
            val viewModel =
                remember { AuthViewModel(navController = navController, context = context) }
            ProfileScreen(
                viewModel = viewModel,
                navController = navController
            )
        }



    }
}
