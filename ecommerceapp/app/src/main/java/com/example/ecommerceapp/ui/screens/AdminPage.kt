package com.example.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPage(
    products: List<Product>,
    cartItems: List<Product>,
    favorites: List<Product>,
    users: List<User>,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Admin Dashboard") },
            navigationIcon = {
                TextButton(onClick = onBack) {
                    Text("← Back")
                }
            }
        )

        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Products", modifier = Modifier.padding(16.dp))
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Users", modifier = Modifier.padding(16.dp))
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("Analytics", modifier = Modifier.padding(16.dp))
            }
        }

        when (selectedTab) {
            0 -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(products) { product ->
                        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(product.name, style = MaterialTheme.typography.titleMedium)
                                Text("Price: $${product.price}")
                                Text("Category: ${product.category}")
                                Text("ID: ${product.id}")
                            }
                        }
                    }
                }
            }
            1 -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(users) { user ->
                        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(user.name, style = MaterialTheme.typography.titleMedium)
                                Text("Email: ${user.email}")
                                Text("Phone: ${user.phone}")
                                Text("Address: ${user.address}")
                                Text("Admin: ${if (user.isAdmin) "Yes" else "No"}")
                            }
                        }
                    }
                }
            }
            2 -> {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("Analytics", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Total Products: ${products.size}")
                            Text("Total Users: ${users.size}")
                            Text("Total Cart Items: ${cartItems.size}")
                            Text("Total Favorites: ${favorites.size}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Admin Users: ${users.count { it.isAdmin }}")
                            Text("Regular Users: ${users.count { !it.isAdmin }}")
                        }
                    }
                }
            }
        }
    }
}
