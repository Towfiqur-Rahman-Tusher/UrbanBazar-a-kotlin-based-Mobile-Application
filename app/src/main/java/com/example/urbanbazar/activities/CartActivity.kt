package com.example.urbanbazar.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.urbanbazar.adapters.CartAdapter
import com.example.urbanbazar.databinding.ActivityCartBinding
import com.example.urbanbazar.models.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

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

        binding.btnBack.setOnClickListener { finish() }

        setupRecyclerView()
        loadCart()

        binding.btnPlaceOrder.setOnClickListener { placeOrder() }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems) { position ->
            if (position in cartItems.indices) {
                val itemToRemove = cartItems[position]
                if (itemToRemove.cartItemId.isNotEmpty()) {
                    db.collection("carts").document(itemToRemove.cartItemId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Item removed from cart", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to remove item: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                cartItems.removeAt(position)
                cartAdapter.notifyItemRemoved(position)
                cartAdapter.notifyItemRangeChanged(position, cartItems.size - position)
                updateTotal()
            }
        }
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = cartAdapter
    }

    private fun loadCart() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Please login to view cart", Toast.LENGTH_SHORT).show()
            updateTotal()
            return
        }

        db.collection("carts")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                cartItems.clear()
                for (doc in documents) {
                    val priceVal = (doc.get("price") as? Number)?.toDouble() ?: doc.getDouble("price") ?: 0.0
                    val quantityVal = (doc.get("quantity") as? Number)?.toInt() ?: doc.getLong("quantity")?.toInt() ?: 1

                    cartItems.add(
                        CartItem(
                            cartItemId = doc.id,
                            productId = doc.getString("productId") ?: "",
                            productName = doc.getString("productName") ?: "",
                            price = priceVal,
                            quantity = quantityVal
                        )
                    )
                }
                cartAdapter.notifyDataSetChanged()
                updateTotal()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load cart: ${e.message}", Toast.LENGTH_SHORT).show()
                updateTotal()
            }
    }

    private fun updateTotal() {
        val total = cartItems.sumOf { it.price * it.quantity }
        binding.tvTotal.text = "৳${String.format(Locale.getDefault(), "%.2f", total)}"

        if (cartItems.isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.rvCart.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.rvCart.visibility = View.VISIBLE
        }
    }

    private fun placeOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid ?: run {
            Toast.makeText(this, "Please login to place order", Toast.LENGTH_SHORT).show()
            return
        }

        val total = cartItems.sumOf { it.price * it.quantity }
        val orderId = System.currentTimeMillis().toString()

        val orderData = hashMapOf(
            "orderId" to orderId,
            "userId" to userId,
            "items" to cartItems.map { item ->
                hashMapOf(
                    "productId" to item.productId,
                    "productName" to item.productName,
                    "price" to item.price,
                    "quantity" to item.quantity
                )
            },
            "totalAmount" to total,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("orders").document(orderId).set(orderData)
            .addOnSuccessListener {
                db.collection("carts").whereEqualTo("userId", userId).get()
                    .addOnSuccessListener { docs ->
                        val batch = db.batch()
                        for (doc in docs) {
                            batch.delete(doc.reference)
                        }
                        batch.commit()
                    }

                Toast.makeText(this, "Order placed successfully! Total: ৳${String.format(Locale.getDefault(), "%.2f", total)}", Toast.LENGTH_LONG).show()
                cartItems.clear()
                cartAdapter.notifyDataSetChanged()
                updateTotal()

                startActivity(Intent(this, OrdersActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Order failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
