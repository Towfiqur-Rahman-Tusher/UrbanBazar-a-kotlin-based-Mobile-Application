package com.example.urbanbazar.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanbazar.R
import com.example.urbanbazar.databinding.ActivityOrdersBinding
import com.example.urbanbazar.models.CartItem
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

        binding.btnBack.setOnClickListener { finish() }

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

        db.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                ordersList.clear()

                for (document in documents) {
                    val itemsList = mutableListOf<CartItem>()
                    val items = document.get("items") as? List<Map<String, Any>> ?: emptyList()

                    for (item in items) {
                        val priceVal = (item["price"] as? Number)?.toDouble() ?: 0.0
                        val qtyVal = (item["quantity"] as? Number)?.toInt() ?: 1

                        itemsList.add(
                            CartItem(
                                productId = item["productId"] as? String ?: "",
                                productName = item["productName"] as? String ?: "",
                                price = priceVal,
                                quantity = qtyVal
                            )
                        )
                    }

                    val totalAmountVal = (document.get("totalAmount") as? Number)?.toDouble()
                        ?: document.getDouble("totalAmount") ?: 0.0
                    val timestampVal = (document.get("timestamp") as? Number)?.toLong()
                        ?: document.getLong("timestamp") ?: 0L

                    val order = Order(
                        orderId = document.getString("orderId") ?: document.id,
                        userId = document.getString("userId") ?: "",
                        items = itemsList,
                        totalAmount = totalAmountVal,
                        timestamp = timestampVal
                    )

                    ordersList.add(order)
                }

                ordersList.sortByDescending { it.timestamp }

                if (ordersList.isEmpty()) {
                    showMessage("No orders yet!\nStart shopping now!")
                } else {
                    binding.rvOrders.visibility = View.VISIBLE
                    binding.tvEmptyOrders.visibility = View.GONE
                    orderAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                showMessage("Failed to load orders: ${e.message}")
            }
    }

    private fun showMessage(msg: String) {
        binding.rvOrders.visibility = View.GONE
        binding.tvEmptyOrders.text = msg
        binding.tvEmptyOrders.visibility = View.VISIBLE
    }

    // Adapter
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
                val shortId = if (order.orderId.length > 8) {
                    order.orderId.takeLast(8)
                } else {
                    order.orderId
                }

                tvOrderId.text = "Order #$shortId"
                tvTotal.text = "Total: ৳${String.format(Locale.getDefault(), "%.2f", order.totalAmount)}"

                val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                tvDate.text = dateFormat.format(Date(order.timestamp))

                val itemsText = order.items.joinToString(", ") {
                    "${it.productName} (${it.quantity})"
                }

                tvItems.text = if (itemsText.isEmpty()) {
                    "No items found"
                } else {
                    itemsText
                }

                tvItems.maxLines = 2
                tvItems.ellipsize = TextUtils.TruncateAt.END
            }
        }
    }
}
