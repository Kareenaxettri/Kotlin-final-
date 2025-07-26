package com.example.dipindonuts.data.model

import com.google.firebase.firestore.PropertyName

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    @PropertyName("isAvailable")
    val isAvailable: Boolean = true,
    val sellerId: String = "",
    val createdAt: Long = System.currentTimeMillis()
) 