package com.kiamba.myfirebasemvvm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.kiamba.myfirebasemvvm.navigation.AppNavHost
import com.kiamba.myfirebasemvvm.navigation.ROUTE_SPLASH
import com.kiamba.myfirebasemvvm.ui.theme.MyFirebasemvvmTheme

class MainActivity : ComponentActivity() {

    // Permission request launcher
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the permission request launcher
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: false
            val storagePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
            if (cameraPermissionGranted && storagePermissionGranted) {
                // Permissions are granted, proceed with your app logic
            } else {
                // Handle the case where permissions are not granted
                Toast.makeText(this, "Camera and Storage permissions are required.", Toast.LENGTH_SHORT).show()
            }
        }

        // Request permissions when the activity is created
        requestPermissionLauncher.launch(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ))

        setContent {
            MyFirebasemvvmTheme {
                // Set up the navigation controller
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // App navigation host
                    AppNavHost(navController = navController, startDestination = ROUTE_SPLASH)
                }
            }
        }
    }
}
