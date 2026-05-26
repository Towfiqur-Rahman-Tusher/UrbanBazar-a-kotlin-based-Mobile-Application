package com.example.urbanbazar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.urbanbazar.R
import com.example.urbanbazar.models.Product

class ProductAdapter(
    private val products: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position], onAddToCart)
    }

    override fun getItemCount() = products.size

    class ProductViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {

        private val ivProduct: ImageView = itemView.findViewById(R.id.ivProduct)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)

        fun bind(product: Product, onAddToCart: (Product) -> Unit) {
            tvName.text = product.name
            tvPrice.text = "৳${product.price}"

            // Set image based on product name
            val imageResId = getImageForProduct(product.name, product.category)
            ivProduct.setImageResource(imageResId)

            // Make image look professional
            ivProduct.scaleType = ImageView.ScaleType.CENTER_CROP
            ivProduct.setPadding(8, 8, 8, 8)

            btnAddToCart.setOnClickListener {
                onAddToCart(product)
            }
        }

        private fun getImageForProduct(productName: String, category: String): Int {
            // FIRST: Check by specific product names

            // ============ SAMSUNG TV (Check BEFORE Samsung phone) ============
            if ((productName.contains("Samsung", ignoreCase = true) ||
                        productName.contains("Galaxy", ignoreCase = true)) &&
                (productName.contains("TV", ignoreCase = true) ||
                        productName.contains("QLED", ignoreCase = true) ||
                        productName.contains("Neo", ignoreCase = true) ||
                        productName.contains("OLED", ignoreCase = true))) {
                return R.drawable.samsungtv
            }

            // ============ LG TV ============
            if (productName.contains("LG", ignoreCase = true) &&
                (productName.contains("TV", ignoreCase = true) ||
                        productName.contains("OLED", ignoreCase = true))) {
                return R.drawable.lgtv
            }

            // ============ SONY TV ============
            if (productName.contains("Sony", ignoreCase = true) &&
                (productName.contains("TV", ignoreCase = true) ||
                        productName.contains("Bravia", ignoreCase = true))) {
                return R.drawable.sonytv
            }

            // ============ TCL TV ============
            if (productName.contains("TCL", ignoreCase = true) &&
                productName.contains("TV", ignoreCase = true)) {
                return R.drawable.tcl_tv  // Add tcl_tv image
            }

            // ============ GOOGLE PIXEL ============
            if (productName.contains("Google Pixel", ignoreCase = true) ||
                productName.contains("Pixel", ignoreCase = true) ||
                productName.contains("Pixel 8", ignoreCase = true) ||
                productName.contains("Pixel 7", ignoreCase = true)) {
                return R.drawable.googlepixel
            }

            // ============ IPAD ============
            if (productName.contains("iPad", ignoreCase = true)) {
                return R.drawable.ipad
            }

            // ============ OPPO ============
            if (productName.contains("Oppo", ignoreCase = true) ||
                productName.contains("OPPO", ignoreCase = true)) {
                return R.drawable.oppo
            }

            // ============ HONOR ============
            if (productName.contains("Honor", ignoreCase = true) ||
                productName.contains("HONOR", ignoreCase = true)) {
                return R.drawable.honor
            }

            // ============ VIVO ============
            if (productName.contains("Vivo", ignoreCase = true) ||
                productName.contains("VIVO", ignoreCase = true)) {
                return R.drawable.vivo
            }

            // ============ REALME ============
            if (productName.contains("Realme", ignoreCase = true) ||
                productName.contains("REALME", ignoreCase = true)) {
                return R.drawable.realme
            }

            // ============ XBOX ============
            if (productName.contains("Xbox", ignoreCase = true) ||
                productName.contains("XBOX", ignoreCase = true)) {
                return R.drawable.xbox
            }

            // ============ NINTENDO SWITCH ============
            if (productName.contains("Nintendo", ignoreCase = true) ||
                productName.contains("Switch", ignoreCase = true)) {
                return R.drawable.nintendo
            }

            // ============ PLAYSTATION 5 ============
            if (productName.contains("PlayStation", ignoreCase = true) ||
                productName.contains("Playstation", ignoreCase = true) ||
                productName.contains("PS5", ignoreCase = true)) {
                return R.drawable.playstation5
            }

            // ============ iPHONE ============
            if (productName.contains("iPhone 15 Pro Max", ignoreCase = true)) {
                return R.drawable.iphone15_pro_max
            }
            if (productName.contains("iPhone", ignoreCase = true) ||
                productName.contains("iphone", ignoreCase = true)) {
                return R.drawable.iphone15
            }

            // ============ MACBOOK ============
            if (productName.contains("MacBook", ignoreCase = true) ||
                productName.contains("Macbook", ignoreCase = true)) {
                return R.drawable.macbook
            }

            // ============ SAMSUNG PHONES ============
            if (productName.contains("Samsung", ignoreCase = true) ||
                productName.contains("Galaxy", ignoreCase = true)) {
                return R.drawable.samsungs24
            }

            // ============ XIAOMI ============
            if (productName.contains("Xiaomi", ignoreCase = true) ||
                productName.contains("Mi", ignoreCase = true) ||
                productName.contains("Redmi", ignoreCase = true)) {
                return R.drawable.xiaomipro
            }

            // ============ ONEPLUS ============
            if (productName.contains("OnePlus", ignoreCase = true) ||
                productName.contains("Oneplus", ignoreCase = true)) {
                return R.drawable.oneplus
            }

            // ============ AIRPODS ============
            if (productName.contains("AirPods", ignoreCase = true) ||
                productName.contains("Airpod", ignoreCase = true)) {
                return R.drawable.appleairpods
            }

            // ============ SONY HEADPHONES ============
            if (productName.contains("Sony", ignoreCase = true) &&
                (productName.contains("WH", ignoreCase = true) ||
                        productName.contains("Headphone", ignoreCase = true) ||
                        productName.contains("1000XM", ignoreCase = true))) {
                return R.drawable.sony
            }

            // ============ BOSE ============
            if (productName.contains("Bose", ignoreCase = true)) {
                return R.drawable.bose
            }

            // ============ JBL ============
            if (productName.contains("JBL", ignoreCase = true)) {
                return R.drawable.jbl
            }

            // ============ SMARTWATCHES ============
            if (productName.contains("Watch", ignoreCase = true) ||
                productName.contains("Smartwatch", ignoreCase = true)) {
                return R.drawable.applewatchultra
            }

            // ============ CAMERAS (NEW) ============
            if (category == "Cameras" ||
                productName.contains("Camera", ignoreCase = true) ||
                productName.contains("Sony A7", ignoreCase = true) ||
                productName.contains("Canon EOS", ignoreCase = true) ||
                productName.contains("Nikon", ignoreCase = true) ||
                productName.contains("GoPro", ignoreCase = true) ||
                productName.contains("DJI", ignoreCase = true) ||
                productName.contains("Fujifilm", ignoreCase = true)) {
                return R.drawable.camera  // Add camera.png to drawable
            }

            // ============ APPLIANCES (NEW) ============
            if (category == "Appliances" ||
                productName.contains("Refrigerator", ignoreCase = true) ||
                productName.contains("Washing Machine", ignoreCase = true) ||
                productName.contains("AC", ignoreCase = true) ||
                productName.contains("Microwave", ignoreCase = true) ||
                productName.contains("Air Fryer", ignoreCase = true) ||
                productName.contains("Vacuum", ignoreCase = true) ||
                productName.contains("Dishwasher", ignoreCase = true) ||
                productName.contains("Dryer", ignoreCase = true) ||
                productName.contains("Purifier", ignoreCase = true)) {
                return R.drawable.appliance  // Add appliance.png to drawable
            }

            // ============ NETWORKING (NEW) ============
            if (category == "Networking" ||
                productName.contains("Router", ignoreCase = true) ||
                productName.contains("WiFi", ignoreCase = true) ||
                productName.contains("NAS", ignoreCase = true) ||
                productName.contains("Switch", ignoreCase = true) ||
                productName.contains("Modem", ignoreCase = true)) {
                return R.drawable.networking  // Add networking.png to drawable
            }

            // ============ STORAGE (NEW) ============
            if (category == "Storage" ||
                productName.contains("SSD", ignoreCase = true) ||
                productName.contains("HDD", ignoreCase = true) ||
                productName.contains("External", ignoreCase = true) ||
                productName.contains("USB", ignoreCase = true) ||
                productName.contains("Drive", ignoreCase = true)) {
                return R.drawable.storage  // Add storage.png to drawable
            }

            // ============ PERIPHERALS (NEW) ============
            if (category == "Peripherals" ||
                productName.contains("Mouse", ignoreCase = true) ||
                productName.contains("Keyboard", ignoreCase = true) ||
                productName.contains("Keychron", ignoreCase = true) ||
                productName.contains("Logitech", ignoreCase = true) ||
                productName.contains("Razer", ignoreCase = true)) {
                return R.drawable.peripheral  // Add peripheral.png to drawable
            }

            // ============ MONITORS (NEW) ============
            if (category == "Monitors" ||
                productName.contains("Monitor", ignoreCase = true) ||
                productName.contains("Display", ignoreCase = true) ||
                productName.contains("Odyssey", ignoreCase = true)) {
                return R.drawable.monitor  // Add monitor.png to drawable
            }

            // ============ COMPONENTS (NEW) ============
            if (category == "Components" ||
                productName.contains("Intel", ignoreCase = true) ||
                productName.contains("AMD", ignoreCase = true) ||
                productName.contains("NVIDIA", ignoreCase = true) ||
                productName.contains("RTX", ignoreCase = true) ||
                productName.contains("Ryzen", ignoreCase = true) ||
                productName.contains("Core i", ignoreCase = true) ||
                productName.contains("Corsair", ignoreCase = true) ||
                productName.contains("Motherboard", ignoreCase = true) ||
                productName.contains("GPU", ignoreCase = true) ||
                productName.contains("CPU", ignoreCase = true)) {
                return R.drawable.component  // Add component.png to drawable
            }

            // ============ GAMING ACCESSORIES ============
            if (category == "Gaming" ||
                productName.contains("Controller", ignoreCase = true) ||
                productName.contains("Headset", ignoreCase = true) ||
                productName.contains("Steam Deck", ignoreCase = true) ||
                productName.contains("ROG Ally", ignoreCase = true)) {
                return R.drawable.gaming
            }

            // ============ DEFAULT ============
            return R.drawable.googlepixel
        }
    }
}