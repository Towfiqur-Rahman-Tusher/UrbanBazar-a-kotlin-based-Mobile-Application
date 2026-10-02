package com.example.urbanbazar.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.urbanbazar.R
import com.example.urbanbazar.adapters.ProductAdapter
import com.example.urbanbazar.databinding.ActivityHomeBinding
import com.example.urbanbazar.models.Product
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var productAdapter: ProductAdapter
    private val allProducts = mutableListOf<Product>()
    private val displayedProducts = mutableListOf<Product>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private var currentCategory = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupToolbar()
        setupRecyclerView()
        setupBottomNavigation()
        setupSearchView()
        setupCategoryChips()
        loadProducts()
    }

    private fun setupToolbar() {
        // Toolbar is replaced with a custom LinearLayout for the left-aligned logo
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(displayedProducts) { product ->
            addToCart(product)
        }
        binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
        binding.rvProducts.adapter = productAdapter
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Already on home
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.nav_orders -> {
                    startActivity(Intent(this, OrdersActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterProducts(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProducts(newText)
                return true
            }
        })
    }

    private fun setupCategoryChips() {
        val chips = listOf(
            binding.chipAll to "All",
            binding.chipSmartphones to "Smartphones",
            binding.chipLaptops to "Laptops",
            binding.chipAudio to "Audio",
            binding.chipSmartwatches to "Smartwatches",
            binding.chipGaming to "Gaming",
            binding.chipTVs to "TVs"
        )

        chips.forEach { (chip, category) ->
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    currentCategory = category
                    filterProducts(binding.searchView.query?.toString())
                    // Update chip colors
                    chips.forEach { (c, _) ->
                        c.chipBackgroundColor = if (c == chip) {
                            androidx.core.content.ContextCompat.getColorStateList(this, R.color.primary)
                        } else {
                            androidx.core.content.ContextCompat.getColorStateList(this, R.color.chip_unselected)
                        }
                    }
                }
            }
        }
    }

    private fun filterProducts(searchQuery: String?) {
        var filtered = allProducts.toList()

        // Filter by category
        if (currentCategory != "All") {
            filtered = filtered.filter { it.category == currentCategory }
        }

        // Filter by search query
        if (!searchQuery.isNullOrEmpty()) {
            filtered = filtered.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.category.contains(searchQuery, ignoreCase = true)
            }
        }

        displayedProducts.clear()
        displayedProducts.addAll(filtered)
        productAdapter.notifyDataSetChanged()

        if (displayedProducts.isEmpty()) {
            Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProducts() {
        // Load from Firestore or use dummy data
        loadElectronicProducts()
    }

    private fun loadElectronicProducts() {
        allProducts.clear()
        allProducts.addAll(getAllElectronicProducts())
        displayedProducts.clear()
        displayedProducts.addAll(allProducts)
        productAdapter.notifyDataSetChanged()
        Toast.makeText(this, "${allProducts.size} products loaded!", Toast.LENGTH_SHORT).show()
    }

    private fun getAllElectronicProducts(): List<Product> {
        return listOf(
            // ============ SMARTPHONES (20 products) ============
            Product("1", "iPhone 15 Pro Max", 165000.0, "", "Smartphones"),
            Product("2", "Samsung Galaxy S24 Ultra", 145000.0, "", "Smartphones"),
            Product("3", "Xiaomi 14 Pro", 85000.0, "", "Smartphones"),
            Product("4", "OnePlus 12", 75000.0, "", "Smartphones"),
            Product("5", "Google Pixel 8 Pro", 120000.0, "", "Smartphones"),
            Product("6", "Realme GT 5G", 45000.0, "", "Smartphones"),
            Product("7", "Vivo X100 Pro", 95000.0, "", "Smartphones"),
            Product("8", "Oppo Find X7", 88000.0, "", "Smartphones"),
            Product("9", "Nothing Phone 2", 55000.0, "", "Smartphones"),
            Product("10", "Motorola Edge 40", 48000.0, "", "Smartphones"),
            Product("11", "Samsung Galaxy A54", 38000.0, "", "Smartphones"),
            Product("12", "Xiaomi Redmi Note 13", 28000.0, "", "Smartphones"),
            Product("13", "Honor Magic 5 Pro", 89000.0, "", "Smartphones"),
            Product("14", "Tecno Spark 20", 15000.0, "", "Smartphones"),
            Product("15", "Infinix Note 30", 22000.0, "", "Smartphones"),
            Product("16", "Symphony Z60", 12000.0, "", "Smartphones"),
            Product("17", "Walton Primo NX9", 18000.0, "", "Smartphones"),
            Product("18", "iPhone 14", 110000.0, "", "Smartphones"),
            Product("19", "Samsung Galaxy Z Fold 5", 220000.0, "", "Smartphones"),
            Product("20", "OnePlus Nord CE 3", 32000.0, "", "Smartphones"),

            // ============ LAPTOPS (25 products) ============
            Product("21", "MacBook Pro M3 14-inch", 220000.0, "", "Laptops"),
            Product("22", "Dell XPS 15", 195000.0, "", "Laptops"),
            Product("23", "HP Spectre x360", 165000.0, "", "Laptops"),
            Product("24", "Lenovo ThinkPad X1", 180000.0, "", "Laptops"),
            Product("25", "Asus ROG Zephyrus G14", 155000.0, "", "Laptops"),
            Product("26", "Acer Predator Helios", 140000.0, "", "Laptops"),
            Product("27", "MSI Stealth 14", 170000.0, "", "Laptops"),
            Product("28", "MacBook Air M2", 135000.0, "", "Laptops"),
            Product("29", "Dell Inspiron 15", 85000.0, "", "Laptops"),
            Product("30", "HP Pavilion 15", 78000.0, "", "Laptops"),
            Product("31", "Lenovo IdeaPad 3", 65000.0, "", "Laptops"),
            Product("32", "Asus Vivobook 15", 62000.0, "", "Laptops"),
            Product("33", "Acer Aspire 5", 58000.0, "", "Laptops"),
            Product("34", "Microsoft Surface Laptop 5", 145000.0, "", "Laptops"),
            Product("35", "Razer Blade 15", 210000.0, "", "Laptops"),
            Product("36", "Samsung Galaxy Book 3", 125000.0, "", "Laptops"),
            Product("37", "Honor MagicBook X14", 72000.0, "", "Laptops"),
            Product("38", "Xiaomi Notebook Pro", 88000.0, "", "Laptops"),
            Product("39", "Realme Book Slim", 68000.0, "", "Laptops"),
            Product("40", "Walton Laptop X3", 45000.0, "", "Laptops"),
            Product("41", "Google Pixelbook Go", 95000.0, "", "Laptops"),
            Product("42", "Lenovo Legion 5 Pro", 160000.0, "", "Laptops"),
            Product("43", "HP Omen 16", 148000.0, "", "Laptops"),
            Product("44", "Dell G15 Gaming", 120000.0, "", "Laptops"),
            Product("45", "Asus TUF Gaming F15", 112000.0, "", "Laptops"),

            // ============ TABLETS (15 products) ============
            Product("46", "iPad Pro 12.9-inch M2", 155000.0, "", "Tablets"),
            Product("47", "iPad Air 5th Gen", 85000.0, "", "Tablets"),
            Product("48", "iPad 10th Gen", 65000.0, "", "Tablets"),
            Product("49", "Samsung Galaxy Tab S9 Ultra", 140000.0, "", "Tablets"),
            Product("50", "Samsung Galaxy Tab S9+", 105000.0, "", "Tablets"),
            Product("51", "Samsung Galaxy Tab A8", 32000.0, "", "Tablets"),
            Product("52", "Xiaomi Pad 6", 42000.0, "", "Tablets"),
            Product("53", "Xiaomi Pad 5", 35000.0, "", "Tablets"),
            Product("54", "Lenovo Tab P12", 48000.0, "", "Tablets"),
            Product("55", "Lenovo Tab M10", 22000.0, "", "Tablets"),
            Product("56", "Huawei MatePad 11", 55000.0, "", "Tablets"),
            Product("57", "Realme Pad 2", 28000.0, "", "Tablets"),
            Product("58", "OnePlus Pad", 52000.0, "", "Tablets"),
            Product("59", "Amazon Fire HD 10", 18000.0, "", "Tablets"),
            Product("60", "Walton Tab 10", 15000.0, "", "Tablets"),

            // ============ AUDIO (20 products) ============
            Product("61", "Apple AirPods Pro 2", 28000.0, "", "Audio"),
            Product("62", "Sony WH-1000XM5", 38000.0, "", "Audio"),
            Product("63", "Samsung Galaxy Buds 2 Pro", 16000.0, "", "Audio"),
            Product("64", "Bose QC45", 32000.0, "", "Audio"),
            Product("65", "JBL Tune 760NC", 12000.0, "", "Audio"),
            Product("66", "Xiaomi Buds 4 Pro", 11000.0, "", "Audio"),
            Product("67", "OnePlus Buds Pro 2", 12000.0, "", "Audio"),
            Product("68", "Realme Buds Air 5", 4500.0, "", "Audio"),
            Product("69", "Nothing Ear 2", 14000.0, "", "Audio"),
            Product("70", "Beats Studio Pro", 35000.0, "", "Audio"),
            Product("71", "Sennheiser Momentum 4", 40000.0, "", "Audio"),
            Product("72", "Anker Soundcore Q45", 16000.0, "", "Audio"),
            Product("73", "Skullcandy Crusher Evo", 18000.0, "", "Audio"),
            Product("74", "Razer BlackShark V2", 10000.0, "", "Audio"),
            Product("75", "Logitech G435", 7000.0, "", "Audio"),
            Product("76", "Mi Neckband Pro", 2500.0, "", "Audio"),
            Product("77", "Oppo Enco X2", 13000.0, "", "Audio"),
            Product("78", "Vivo TWS 3", 8000.0, "", "Audio"),
            Product("79", "Walton Bassbuds", 1800.0, "", "Audio"),
            Product("80", "Symphony Earbuds", 1500.0, "", "Audio"),

            // ============ SMARTWATCHES (15 products) ============
            Product("81", "Apple Watch Ultra 2", 85000.0, "", "Smartwatches"),
            Product("82", "Apple Watch Series 9", 55000.0, "", "Smartwatches"),
            Product("83", "Samsung Galaxy Watch 6 Classic", 45000.0, "", "Smartwatches"),
            Product("84", "Samsung Galaxy Watch 6", 35000.0, "", "Smartwatches"),
            Product("85", "Google Pixel Watch 2", 38000.0, "", "Smartwatches"),
            Product("86", "Xiaomi Watch S2", 15000.0, "", "Smartwatches"),
            Product("87", "OnePlus Watch 2", 28000.0, "", "Smartwatches"),
            Product("88", "Realme Watch 3", 6000.0, "", "Smartwatches"),
            Product("89", "Amazfit GTR 4", 18000.0, "", "Smartwatches"),
            Product("90", "Garmin Venu 3", 52000.0, "", "Smartwatches"),
            Product("91", "Huawei Watch GT 4", 25000.0, "", "Smartwatches"),
            Product("92", "Noise Colorfit Pro 5", 5000.0, "", "Smartwatches"),
            Product("93", "Fire-Boltt Phoenix", 3500.0, "", "Smartwatches"),
            Product("94", "BoAt Xtend Pro", 4000.0, "", "Smartwatches"),
            Product("95", "Walton Watch GT", 3000.0, "", "Smartwatches"),

            // ============ TVS (15 products) ============
            Product("121", "Samsung 55-inch QLED 4K", 120000.0, "", "TVs"),
            Product("122", "LG C3 55-inch OLED", 165000.0, "", "TVs"),
            Product("123", "Sony Bravia X90L 55-inch", 155000.0, "", "TVs"),
            Product("124", "TCL 65-inch QLED", 95000.0, "", "TVs"),
            Product("125", "Hisense 55-inch ULED", 75000.0, "", "TVs"),
            Product("126", "Walton 43-inch Smart TV", 35000.0, "", "TVs"),
            Product("127", "Samsung 32-inch HD TV", 25000.0, "", "TVs"),
            Product("128", "LG 43-inch 4K TV", 48000.0, "", "TVs"),
            Product("129", "Sony 65-inch X95L", 250000.0, "", "TVs"),
            Product("130", "Xiaomi TV A2 43-inch", 38000.0, "", "TVs"),
            Product("131", "OnePlus TV Y1 43-inch", 35000.0, "", "TVs"),
            Product("132", "Realme TV 43-inch", 32000.0, "", "TVs"),
            Product("133", "Samsung 85-inch Neo QLED", 420000.0, "", "TVs"),
            Product("134", "LG 75-inch NanoCell", 195000.0, "", "TVs"),
            Product("135", "TCL 98-inch QLED", 550000.0, "", "TVs"),

            // ============ GAMING (20 products) ============
            Product("151", "PlayStation 5", 65000.0, "", "Gaming"),
            Product("152", "Xbox Series X", 62000.0, "", "Gaming"),
            Product("153", "Nintendo Switch OLED", 42000.0, "", "Gaming"),
            Product("154", "Steam Deck 512GB", 68000.0, "", "Gaming"),
            Product("155", "ASUS ROG Ally", 72000.0, "", "Gaming"),
            Product("156", "PS5 DualSense Controller", 7000.0, "", "Gaming"),
            Product("157", "Xbox Elite Controller", 14000.0, "", "Gaming"),
            Product("158", "Logitech G29 Racing Wheel", 38000.0, "", "Gaming"),
            Product("159", "Thrustmaster T300 RS", 55000.0, "", "Gaming"),
            Product("160", "Meta Quest 3", 75000.0, "", "Gaming"),
            Product("161", "PS VR2", 72000.0, "", "Gaming"),
            Product("162", "Razer Kitsune Arcade", 30000.0, "", "Gaming"),
            Product("163", "Corsair HS80 Headset", 12000.0, "", "Gaming"),
            Product("164", "SteelSeries Arctis Nova Pro", 28000.0, "", "Gaming"),
            Product("165", "Elgato Stream Deck XL", 25000.0, "", "Gaming"),
            Product("166", "Blue Yeti X Microphone", 15000.0, "", "Gaming"),
            Product("167", "Razer Kiyo Pro Webcam", 16000.0, "", "Gaming"),
            Product("168", "Logitech Brio 4K", 18000.0, "", "Gaming"),
            Product("169", "Corsair K100 Keyboard", 22000.0, "", "Gaming"),
            Product("170", "Razer Basilisk V3 Pro", 16000.0, "", "Gaming"),

            // ============ PC COMPONENTS (25 products) ============
            Product("96", "Intel Core i9-14900K", 68000.0, "", "Components"),
            Product("97", "Intel Core i7-14700K", 45000.0, "", "Components"),
            Product("98", "Intel Core i5-14600K", 32000.0, "", "Components"),
            Product("99", "AMD Ryzen 9 7950X", 72000.0, "", "Components"),
            Product("100", "AMD Ryzen 7 7800X3D", 52000.0, "", "Components"),
            Product("101", "AMD Ryzen 5 7600X", 28000.0, "", "Components"),
            Product("102", "NVIDIA RTX 4090", 210000.0, "", "Components"),
            Product("103", "NVIDIA RTX 4080", 145000.0, "", "Components"),
            Product("104", "NVIDIA RTX 4070 Ti", 95000.0, "", "Components"),
            Product("105", "NVIDIA RTX 4060", 48000.0, "", "Components"),
            Product("106", "AMD RX 7900 XTX", 135000.0, "", "Components"),
            Product("107", "AMD RX 7800 XT", 75000.0, "", "Components"),
            Product("108", "Corsair Vengeance 32GB DDR5", 15000.0, "", "Components"),
            Product("109", "Samsung 980 Pro 1TB SSD", 14000.0, "", "Components"),
            Product("110", "WD Black SN850X 1TB", 13000.0, "", "Components"),
            Product("111", "ASUS ROG Strix Z790", 55000.0, "", "Components"),
            Product("112", "MSI B650 Tomahawk", 28000.0, "", "Components"),
            Product("113", "Corsair RM1000e PSU", 22000.0, "", "Components"),
            Product("114", "Lian Li PC-O11 Dynamic", 15000.0, "", "Components"),
            Product("115", "NZXT Kraken Elite 360", 28000.0, "", "Components"),

            // ============ PERIPHERALS (5 products) ============
            Product("116", "Logitech MX Master 3S", 12000.0, "", "Peripherals"),
            Product("117", "Razer DeathAdder V3", 9000.0, "", "Peripherals"),
            Product("118", "Keychron K2 Pro", 10000.0, "", "Peripherals"),
            Product("119", "Logitech G Pro X", 14000.0, "", "Peripherals"),
            Product("120", "Samsung Odyssey G7 27", 55000.0, "", "Monitors"),

            // ============ CAMERAS (15 products) ============
            Product("136", "Sony A7 IV", 220000.0, "", "Cameras"),
            Product("137", "Canon EOS R6 Mark II", 240000.0, "", "Cameras"),
            Product("138", "Nikon Z8", 380000.0, "", "Cameras"),
            Product("139", "Fujifilm X-T5", 175000.0, "", "Cameras"),
            Product("140", "Sony ZV-E10", 85000.0, "", "Cameras"),
            Product("141", "Canon EOS R50", 75000.0, "", "Cameras"),
            Product("142", "GoPro Hero 12 Black", 55000.0, "", "Cameras"),
            Product("143", "DJI Osmo Pocket 3", 65000.0, "", "Cameras"),
            Product("144", "Insta360 X3", 52000.0, "", "Cameras"),
            Product("145", "Panasonic Lumix GH6", 195000.0, "", "Cameras"),
            Product("146", "Sony RX100 VII", 125000.0, "", "Cameras"),
            Product("147", "Canon G7 X Mark III", 78000.0, "", "Cameras"),
            Product("148", "Leica Q3", 550000.0, "", "Cameras"),
            Product("149", "Fujifilm Instax Mini 12", 8000.0, "", "Cameras"),
            Product("150", "Kodak Pixpro WPZ2", 22000.0, "", "Cameras"),

            // ============ APPLIANCES (20 products) ============
            Product("171", "Samsung Refrigerator 300L", 65000.0, "", "Appliances"),
            Product("172", "LG Washing Machine 7kg", 55000.0, "", "Appliances"),
            Product("173", "Walton AC 1.5 Ton", 58000.0, "", "Appliances"),
            Product("174", "Midea Microwave Oven", 12000.0, "", "Appliances"),
            Product("175", "Panasonic Rice Cooker", 4500.0, "", "Appliances"),
            Product("176", "Philips Air Fryer", 18000.0, "", "Appliances"),
            Product("177", "Dyson V15 Vacuum", 75000.0, "", "Appliances"),
            Product("178", "iRobot Roomba j7+", 85000.0, "", "Appliances"),
            Product("179", "Samsung Air Purifier", 35000.0, "", "Appliances"),
            Product("180", "Xiaomi Mi Robot Vacuum", 32000.0, "", "Appliances"),
            Product("181", "Instant Pot Duo", 14000.0, "", "Appliances"),
            Product("182", "KitchenAid Stand Mixer", 55000.0, "", "Appliances"),
            Product("183", "Breville Espresso Machine", 85000.0, "", "Appliances"),
            Product("184", "Nespresso Vertuo Plus", 35000.0, "", "Appliances"),
            Product("185", "Samsung Dishwasher", 68000.0, "", "Appliances"),
            Product("186", "LG Dryer 8kg", 62000.0, "", "Appliances"),
            Product("187", "Walton Water Purifier", 25000.0, "", "Appliances"),
            Product("188", "Panasonic Iron", 3500.0, "", "Appliances"),
            Product("189", "Philips Hair Dryer", 4000.0, "", "Appliances"),
            Product("190", "Braun Electric Shaver", 12000.0, "", "Appliances"),

            // ============ NETWORKING & STORAGE (10 products) ============
            Product("191", "TP-Link Archer AX73 Router", 8000.0, "", "Networking"),
            Product("192", "ASUS ROG Rapture GT-AX11000", 45000.0, "", "Networking"),
            Product("193", "Google Nest WiFi Pro", 22000.0, "", "Networking"),
            Product("194", "Synology DS220+ NAS", 42000.0, "", "Networking"),
            Product("195", "Western Digital 4TB External HDD", 12000.0, "", "Storage"),
            Product("196", "Samsung T7 Shield 1TB", 15000.0, "", "Storage"),
            Product("197", "SanDisk 256GB USB 3.0", 3000.0, "", "Storage"),
            Product("198", "Seagate 2TB Portable HDD", 8500.0, "", "Storage"),
            Product("199", "TP-Link Powerline Adapter", 6000.0, "", "Networking"),
            Product("200", "Netgear Nighthawk M5 5G", 85000.0, "", "Networking")
        )
    }

    private fun addToCart(product: Product) {
        val userId = auth.currentUser?.uid ?: run {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("carts")
            .whereEqualTo("userId", userId)
            .whereEqualTo("productId", product.id)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val doc = documents.documents[0]
                    val currentQty = (doc.getLong("quantity") ?: 1L).toInt()
                    doc.reference.update("quantity", currentQty + 1)
                        .addOnSuccessListener {
                            Toast.makeText(this, "✓ ${product.name} quantity updated in cart!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to update cart: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    val cartItem = hashMapOf(
                        "userId" to userId,
                        "productId" to product.id,
                        "productName" to product.name,
                        "price" to product.price,
                        "quantity" to 1,
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("carts").add(cartItem)
                        .addOnSuccessListener {
                            Toast.makeText(this, "✓ ${product.name} added to cart!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to add to cart: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding to cart: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 1, 0, "Cart")
        menu.add(0, 2, 1, "Orders")
        menu.add(0, 3, 2, "Logout")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            1 -> startActivity(Intent(this, CartActivity::class.java))
            2 -> startActivity(Intent(this, OrdersActivity::class.java))
            3 -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        return true
    }
}