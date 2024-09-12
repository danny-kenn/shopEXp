package com.kiamba.myfirebasemvvm.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.kiamba.myfirebasemvvm.data.AuthViewModel
import com.kiamba.myfirebasemvvm.model.User
import com.kiamba.myfirebasemvvm.navigation.ROUTE_ADD_PRODUCT
import com.kiamba.myfirebasemvvm.navigation.ROUTE_HOME
import com.kiamba.myfirebasemvvm.navigation.ROUTE_PROFILE
import com.kiamba.myfirebasemvvm.navigation.ROUTE_REGISTER
import com.kiamba.myfirebasemvvm.navigation.ROUTE_VIEW_UPLOAD

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: AuthViewModel, navController: NavHostController) {
    var userData by remember { mutableStateOf<User?>(null) }
    var profilePictureUrl by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Image picker for updating profile picture
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadProfilePicture(it) { downloadUrl ->
                profilePictureUrl = downloadUrl
                viewModel.updateUserProfile(name = newName, phone = phoneNumber, email = newEmail, profilePictureUrl = downloadUrl)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getUserProfile { user ->
            if (user != null) {
                userData = user
                phoneNumber = user.phone ?: ""
                profilePictureUrl = user.profilePictureUrl ?: ""
                newName = user.name
                newEmail = user.email
                newPhone = user.phone ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF128C7E)) // WhatsApp's theme color
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture section
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(3.dp, Color.Gray, CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profilePictureUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(profilePictureUrl),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Profile",
                        tint = Color.White,
                        modifier = Modifier.size(100.dp)
                    )
                }

                // Edit Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xFF25D366)), // WhatsApp's green accent color
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Edit Icon", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // User Name and Phone Number Display
            if (!isEditing) {
                Text(
                    text = userData?.name ?: "Loading...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = userData?.email ?: "Loading...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = userData?.phone ?: "Phone number not added",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Edit Profile Button
                Button(
                    onClick = { isEditing = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)), // WhatsApp green
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = "Edit Profile", color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Logout Button
                Button(
                    onClick = {
                        viewModel.logout()
                        navController.navigate(ROUTE_REGISTER) { popUpTo(0) } // Navigate to login screen
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = "Logout", color = Color.White)
                }
            } else {
                // Editable Name and Phone Number
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                TextField(
                    value = newEmail,
                    onValueChange = { newEmail = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Save and Cancel Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.updateUserProfile(newName, phoneNumber, newEmail, profilePictureUrl)
                            isEditing = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128C7E)), // Save in WhatsApp theme
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(text = "Save", color = Color.White)
                    }

                    Button(
                        onClick = { isEditing = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(text = "Cancel", color = Color.White)
                    }
                }
            }
        }
    }
}


