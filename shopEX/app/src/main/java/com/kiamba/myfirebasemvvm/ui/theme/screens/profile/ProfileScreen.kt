package com.kiamba.myfirebasemvvm.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.kiamba.myfirebasemvvm.data.AuthViewModel
import com.kiamba.myfirebasemvvm.model.User

@Composable
fun ProfileScreen(viewModel: AuthViewModel) {
    var userData by remember { mutableStateOf<User?>(null) }
    var phoneNumber by remember { mutableStateOf("") }
    var profilePictureUrl by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Load the user's profile data
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        // Profile Picture
        Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
            if (profilePictureUrl.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(data = profilePictureUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Upload", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Information
        if (isEditing) {
            // Editable Fields
            TextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = newEmail,
                onValueChange = { newEmail = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = newPhone,
                onValueChange = { newPhone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Update Profile Button
            Button(
                onClick = {
                    // Update user profile
                    viewModel.updateUserProfile(
                        name = newName,
                        phone = newPhone,
                        profilePictureUrl = profilePictureUrl
                    )
                    // Handle password update if newPassword matches confirmPassword
                    if (newPassword == confirmPassword && newPassword.isNotEmpty()) {
                        viewModel.updatePassword(newPassword)
                    }
                    isEditing = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Cancel Edit Button
            Button(
                onClick = { isEditing = false },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text(text = "Cancel")
            }
        } else {
            // Display User Info
            Text(
                text = "Name: ${userData?.name ?: "Loading..."}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Email: ${userData?.email ?: "Loading..."}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Phone: ${phoneNumber.ifEmpty { "Not added" }}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Edit Profile Button
            Button(
                onClick = { isEditing = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text(text = "Logout")
            }
        }
    }
}
