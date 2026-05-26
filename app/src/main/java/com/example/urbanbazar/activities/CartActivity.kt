package com.example.urbanbazar.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.urbanbazar.adapters.CartAdapter
import com.example.urbanbazar.databinding.ActivityCartBinding
import com.example.urbanbazar.models.CartItem
import com.example.urbanbazar.models.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        // Custom header is used instead of Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        loadCart()

        binding.btnPlaceOrder.setOnClickListener { placeOrder() }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems) { position ->
            cartItems.removeAt(position)
            cartAdapter.notifyItemRemoved(position)
            updateTotal()
        }
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = cartAdapter
    }

    private fun loadCart() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("carts")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                cartItems.clear()
                for (doc in documents) {
                    cartItems.add(CartItem(
                        productId = doc.getString("productId") ?: "",
                        productName = doc.getString("productName") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        quantity = doc.getLong("quantity")?.toInt() ?: 1
                    ))
                }
                cartAdapter.notifyDataSetChanged()
                updateTotal()
            }
    }

    private fun updateTotal() {
        val total = cartItems.sumOf { it.price * it.quantity }
        binding.tvTotal.text = "Total: ৳$total"
    }

    private fun placeOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid ?: return
        val total = cartItems.sumOf { it.price * it.quantity }
        val order = Order(
            orderId = System.currentTimeMillis().toString(),
            userId = userId,
            items = cartItems.toList(),
            totalAmount = total,
            timestamp = System.currentTimeMillis()
        )

        db.collection("orders").document(order.orderId).set(order)
            .addOnSuccessListener {
                // Clear cart
                db.collection("carts").whereEqualTo("userId", userId).get()
                    .addOnSuccessListener { docs ->
                        for (doc in docs) {
                            doc.reference.delete()
                        }
                    }
                Toast.makeText(this, "Order placed! Total: ৳$total", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order failed", Toast.LENGTH_SHORT).show()
            }
    }
}