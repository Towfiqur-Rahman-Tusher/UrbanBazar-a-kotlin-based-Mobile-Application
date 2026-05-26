package com.example.ecommerceapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.User
import com.example.ecommerceapp.viewmodel.ProductViewModel
import com.example.ecommerceapp.viewmodel.PriceSortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcommerceApp(
    navController: NavController,
    user: User?,
    productViewModel: ProductViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()
    val cartItems by productViewModel.cart.collectAsState()
    val favorites by productViewModel.favorites.collectAsState()
    var showCart by remember { mutableStateOf(false) }
    var showPayment by remember { mutableStateOf(false) }
    var showFavorites by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Urban Bazaar",
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    if (user?.isAdmin == true) {
                        IconButton(onClick = { navController.navigate("admin") }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Admin")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Products") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        showCart = !showCart
                        showFavorites = false
                        showPayment = false
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00a37a))
                ) {
                    Text(if (showCart) "Hide Cart" else "View Cart (${cartItems.size})")
                }

                Button(
                    onClick = {
                        showFavorites = !showFavorites
                        showCart = false
                        showPayment = false
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00a37a))
                ) {
                    Text(if (showFavorites) "Hide Favorites" else "Favorites (${favorites.size})")
                }

                // Filter Dropdown
                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00a37a))
                    ) {
                        Text("Filter")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Price: Low to High") },
                            onClick = {
                                productViewModel.updateSortOption(PriceSortOption.ASCENDING)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Price: High to Low") },
                            onClick = {
                                productViewModel.updateSortOption(PriceSortOption.DESCENDING)
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Content
            when {
                showPayment -> PaymentPage(cartItems = cartItems, onBack = { showPayment = false }, onComplete = {
                    productViewModel.clearCart()
                    showPayment = false
                })
                showCart -> CartView(cartItems, onPay = { showPayment = true })
                showFavorites -> FavoritesView(favorites) { product ->
                    productViewModel.removeFromFavorites(product)
                }
                else -> {
                    Text("Products", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(8.dp))

                    val filteredProducts = products.filter {
                        it.name.contains(searchQuery, ignoreCase = true)
                    }

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(filteredProducts) { product ->
                            ProductItem(
                                product = product,
                                onAddToCart = { productViewModel.addToCart(it) },
                                onAddToFavorites = { productViewModel.addToFavorites(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onAddToFavorites: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Price: $${product.price}")
                if (product.description.isNotEmpty()) {
                    Text(text = product.description, style = MaterialTheme.typography.bodySmall)
                }
            }
            Column {
                Button(
                    onClick = { onAddToCart(product) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00a37a)),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text("Add to Cart")
                }
                Button(
                    onClick = { onAddToFavorites(product) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00a37a))
                ) {
                    Text("❤️ Favorite")
                }
            }
        }
    }
}

@Composable
fun CartView(cartItems: List<Product>, onPay: () -> Unit) {
    Column {
        Text("Your Cart", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(8.dp))

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Your cart is empty")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { product ->
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
                                Text(text = "Price: $${product.price}")
                            }
                            Text("Quantity: 1")
                        }
                    }
                }
            }

            val total = cartItems.sumOf { it.price }
            Text(
                text = "Total: $${total}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = onPay,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00a37a))
            ) {
                Text("Proceed to Pay")
            }
        }
    }
}

@Composable
fun FavoritesView(favoriteItems: List<Product>, onRemove: (Product) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("Favorites", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(8.dp))

        if (favoriteItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No favorites yet")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(favoriteItems) { product ->
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
                                Text(text = "Price: $${product.price}")
                            }
                            Button(
                                onClick = { onRemove(product) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00a37a))
                            ) {
                                Text("Remove")
                            }
                        }
                    }
                }
            }
        }
    }
}
