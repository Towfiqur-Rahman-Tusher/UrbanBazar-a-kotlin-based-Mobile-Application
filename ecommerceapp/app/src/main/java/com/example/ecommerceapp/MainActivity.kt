package com.example.ecommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.ui.screens.*
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    var currentUser by remember { mutableStateOf<User?>(null) }
    
    NavHost(navController = navController, startDestination = "startPage") {
        composable("startPage") {
            StartPage(
                onGuest = {
                    currentUser = null
                    navController.navigate("ecommerceApp") {
                        popUpTo("startPage") { inclusive = true }
                    }
                },
                onMember = { navController.navigate("register") },
                onLogin = { navController.navigate("login") }
            )
        }
        
        composable("register") {
            RegistrationPage(navController) { newUser ->
                currentUser = newUser
                navController.navigate("ecommerceApp") {
                    popUpTo("startPage") { inclusive = true }
                }
            }
        }
        
        composable("login") {
            LoginPage(navController) { loggedInUser ->
                currentUser = loggedInUser
                navController.navigate("ecommerceApp") {
                    popUpTo("startPage") { inclusive = true }
                }
            }
        }
        
        composable("ecommerceApp") {
            EcommerceApp(
                navController = navController,
                user = currentUser
            )
        }
        
        composable("admin") {
            if (currentUser?.isAdmin == true) {
                AdminPage(
                    products = emptyList(),
                    cartItems = emptyList(),
                    favorites = emptyList(),
                    users = emptyList(),
                    onBack = { navController.popBackStack() }
                )
            } else {
                UnauthorizedPage { navController.popBackStack() }
            }
        }
    }
}

@Composable
fun UnauthorizedPage(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.unauthorized_access), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.unauthorized_message))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text(stringResource(R.string.go_back))
        }
    }
}
