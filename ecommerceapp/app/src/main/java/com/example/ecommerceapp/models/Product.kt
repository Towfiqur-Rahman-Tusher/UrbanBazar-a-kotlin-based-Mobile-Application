package com.example.ecommerceapp.models

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val description: String = "",
    val category: String = "",
    val imageRes: Int = 0 // Added this to match some usages that were expecting imageRes
) {
    constructor() : this("", "", 0.0, "", "", "", 0)
}
