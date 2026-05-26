package com.example.urbanbazar.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanbazar.R
import com.example.urbanbazar.databinding.ActivityOrdersBinding
import com.example.urbanbazar.models.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class OrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    private lateinit var orderAdapter: OrderAdapter
    private val ordersList = mutableListOf<Order>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Custom header is used instead of Toolbar
        supportActionBar?.title = "My Orders"

        setupRecyclerView()
        loadOrders()
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(ordersList)
        binding.rvOrders.layoutManager = LinearLayoutManager(this)
        binding.rvOrders.adapter = orderAdapter
    }

    private fun loadOrders() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            showMessage("Please login to view orders")
            return
        }

        showLoading(true)

        // Query orders from Firestore
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                showLoading(false)
                ordersList.clear()

                for (document in documents) {
                    val order = document.toObject(Order::class.java)
                    ordersList.add(order)
                }

                // Sort by timestamp (newest first)
                ordersList.sortByDescending { it.timestamp }

                if (ordersList.isEmpty()) {
                    showMessage("No orders yet!\nStart shopping now!")
                    binding.rvOrders.visibility = View.GONE
                } else {
                    binding.rvOrders.visibility = View.VISIBLE
                    orderAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                showLoading(false)
                showMessage("Failed to load orders: ${e.message}")
                binding.rvOrders.visibility = View.GONE
            }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.rvOrders.visibility = View.GONE
            val progressBar = com.google.android.material.progressindicator.CircularProgressIndicator(this)
            progressBar.id = View.generateViewId()
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            progressBar.layoutParams = params
            binding.root.addView(progressBar)
        } else {
            val progressBar = binding.root.findViewById<View>(View.generateViewId())
            if (progressBar != null) {
                binding.root.removeView(progressBar)
            }
        }
    }

    private fun showMessage(msg: String) {
        binding.rvOrders.visibility = View.GONE
        val tvMessage = TextView(this)
        tvMessage.text = msg
        tvMessage.setTextSize(18f)
        tvMessage.gravity = android.view.Gravity.CENTER
        tvMessage.setPadding(32, 32, 32, 32)
        binding.root.addView(tvMessage)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Adapter for orders
    inner class OrderAdapter(private val orders: List<Order>) :
        RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order, parent, false)
            return OrderViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            holder.bind(orders[position])
        }

        override fun getItemCount() = orders.size

        inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
            private val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
            private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
            private val tvItems: TextView = itemView.findViewById(R.id.tvItems)

            fun bind(order: Order) {
                // Show order ID (last 8 characters)
                val shortId = if (order.orderId.length > 8) {
                    order.orderId.takeLast(8)
                } else {
                    order.orderId
                }
                tvOrderId.text = "Order #$shortId"

                // Show total amount
                tvTotal.text = "Total: ৳${String.format("%.2f", order.totalAmount)}"

                // Show date
                val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                tvDate.text = dateFormat.format(Date(order.timestamp))

                // Show items list
                val itemsText = order.items.joinToString(", ") {
                    "${it.productName} (${it.quantity})"
                }
                tvItems.text = itemsText

                // Limit to 2 lines
                tvItems.maxLines = 2
                tvItems.ellipsize = android.text.TextUtils.TruncateAt.END
            }
        }
    }
}