package com.example.urbanbazar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanbazar.R
import com.example.urbanbazar.models.CartItem

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.bind(item, position, onRemove)
    }

    override fun getItemCount() = cartItems.size

    class CartViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvName: TextView = itemView.findViewById(R.id.tvCartName)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvCartPrice)
        private val btnRemove: ImageButton = itemView.findViewById(R.id.btnRemove)

        fun bind(item: CartItem, position: Int, onRemove: (Int) -> Unit) {
            tvName.text = item.productName
            tvPrice.text = "₹${item.price} x ${item.quantity}"
            btnRemove.setOnClickListener { onRemove(position) }
        }
    }
}