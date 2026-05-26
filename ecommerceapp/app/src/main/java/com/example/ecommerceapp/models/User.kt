package com.example.ecommerceapp.models

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val isAdmin: Boolean = false
) {
    constructor() : this("", "", "", "", "", false)
}
