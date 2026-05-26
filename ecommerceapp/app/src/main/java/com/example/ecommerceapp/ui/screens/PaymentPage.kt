package com.example.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.models.Product

@Composable
fun PaymentPage(
    cartItems: List<Product>,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    var showSuccess by remember { mutableStateOf(false) }
    val total = cartItems.sumOf { it.price }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showSuccess) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("✅", fontSize = 48.sp)
                    Text("Payment Successful!", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    Text("Thank you for your purchase!", color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onComplete) {
                        Text("Continue Shopping")
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Payment Details", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Order Summary", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))

                    cartItems.forEach { product ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(product.name)
                            Text("$${product.price}")
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount:", style = MaterialTheme.typography.titleMedium)
                        Text("$${total}", style = MaterialTheme.typography.titleMedium)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Payment Method: Cash on Delivery")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Shipping to your registered address")

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = onBack,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Back")
                        }
                        Button(
                            onClick = { showSuccess = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00a37a))
                        ) {
                            Text("Confirm Payment")
                        }
                    }
                }
            }
        }
    }
}
