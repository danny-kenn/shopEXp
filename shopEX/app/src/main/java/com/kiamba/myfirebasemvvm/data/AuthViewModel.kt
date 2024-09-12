package com.kiamba.myfirebasemvvm.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kiamba.myfirebasemvvm.model.User
import com.kiamba.myfirebasemvvm.navigation.ROUTE_HOME
import com.kiamba.myfirebasemvvm.navigation.ROUTE_LOGIN
import com.kiamba.myfirebasemvvm.navigation.ROUTE_REGISTER


class AuthViewModel(
    private val navController: NavHostController,
    private val context: Context
) : ViewModel() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, pass: String) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Successfully Logged in", Toast.LENGTH_LONG).show()
                navController.navigate(ROUTE_HOME)
            } else {
                Toast.makeText(context, "${it.exception!!.message}", Toast.LENGTH_LONG).show()
                navController.navigate(ROUTE_LOGIN)
            }
        }
    }

    fun logout() {
        mAuth.signOut()
        navController.navigate(ROUTE_LOGIN)
    }

    fun isloggedin(): Boolean {
        return mAuth.currentUser != null
    }

    fun signup(name: String, email: String, pass: String, confirmpass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank() || confirmpass.isBlank()) {
            Toast.makeText(context, "Please Input all Fields", Toast.LENGTH_LONG).show()
        } else if (pass != confirmpass) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    val userData = User(
                        name = name,
                        email = email,
                        pass = pass,
                        confirmpass = confirmpass,
                        userid = mAuth.currentUser!!.uid,
                        phone = null,  // Default to null or "" if no phone is provided during sign-up
                        profilePictureUrl = null  // Default to null or "" for profile picture during sign-up
                    )
                    val regRef = FirebaseDatabase.getInstance().getReference("Users/${mAuth.currentUser!!.uid}")
                    regRef.setValue(userData).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_LONG).show()
                            navController.navigate(ROUTE_LOGIN)
                        } else {
                            Toast.makeText(context, "${task.exception!!.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    navController.navigate(ROUTE_REGISTER)
                }
            }
        }
    }


    fun getUserProfile(onResult: (User?) -> Unit) {
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = FirebaseDatabase.getInstance().getReference("Users/$userId")
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    onResult(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    onResult(null)
                }
            })
        } else {
            onResult(null)
        }
    }

    fun updateUserProfile(name: String, phone: String, profilePictureUrl: String) {
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = FirebaseDatabase.getInstance().getReference("Users/$userId")

            val updatedUser = User(
                name = name,
                email = currentUser.email ?: "",
                pass = "",  // Not updating password
                confirmpass = "",  // Not updating password confirmation
                userid = userId,
                phone = phone,
                profilePictureUrl = profilePictureUrl
            )

            userRef.setValue(updatedUser).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Profile Update Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


// Method to update password
fun updatePassword(newPassword: String) {
    val user = FirebaseAuth.getInstance().currentUser
    user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Handle success
        } else {
            // Handle failure
            Log.e("AuthViewModel", "Password update failed", task.exception)
        }
    }
}




