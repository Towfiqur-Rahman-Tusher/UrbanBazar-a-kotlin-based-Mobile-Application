package com.example.ecommerceapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.example.ecommerceapp.models.Product
import com.example.ecommerceapp.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class PriceSortOption {
    ASCENDING, DESCENDING
}

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _cart = MutableStateFlow<List<Product>>(emptyList())
    val cart: StateFlow<List<Product>> = _cart.asStateFlow()

    private val _favorites = MutableStateFlow<List<Product>>(emptyList())
    val favorites: StateFlow<List<Product>> = _favorites.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _currentSortOption = MutableStateFlow(PriceSortOption.ASCENDING)
    val currentSortOption: StateFlow<PriceSortOption> = _currentSortOption.asStateFlow()

    private val database = FirebaseDatabase.getInstance().reference

    init {
        fetchProducts()
        fetchUsers()
    }

    fun fetchProducts() {
        database.child("products").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let {
                        productList.add(it)
                    }
                }
                _products.value = productList
                sortProducts(_currentSortOption.value)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProductViewModel", "Failed to fetch products", error.toException())
            }
        })
    }

    fun fetchUsers() {
        database.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let {
                        userList.add(it)
                    }
                }
                _users.value = userList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProductViewModel", "Failed to fetch users", error.toException())
            }
        })
    }

    fun addProduct(product: Product) {
        database.child("products").child(product.id).setValue(product)
            .addOnSuccessListener {
                fetchProducts()
            }
    }

    fun updateProduct(product: Product) {
        database.child("products").child(product.id).setValue(product)
            .addOnSuccessListener {
                fetchProducts()
            }
    }

    fun deleteProduct(product: Product) {
        database.child("products").child(product.id).removeValue()
            .addOnSuccessListener {
                fetchProducts()
            }
    }

    fun updateSortOption(sortOption: PriceSortOption) {
        _currentSortOption.value = sortOption
        sortProducts(sortOption)
    }

    private fun sortProducts(sortOption: PriceSortOption) {
        _products.value = when (sortOption) {
            PriceSortOption.ASCENDING -> _products.value.sortedBy { it.price }
            PriceSortOption.DESCENDING -> _products.value.sortedByDescending { it.price }
        }
    }

    fun addToCart(product: Product) {
        if (!_cart.value.contains(product)) {
            _cart.value = _cart.value + product
        }
    }

    fun removeFromCart(product: Product) {
        _cart.value = _cart.value.filterNot { it.id == product.id }
    }

    fun addToFavorites(product: Product) {
        if (!_favorites.value.contains(product)) {
            _favorites.value = _favorites.value + product
        }
    }

    fun removeFromFavorites(product: Product) {
        _favorites.value = _favorites.value.filterNot { it.id == product.id }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }
}
