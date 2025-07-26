package com.example.dipindonuts.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val role: UserRole = UserRole.BUYER,
    val phoneNumber: String = "",
    val address: String = ""
)

enum class UserRole {
    BUYER,
    //SELLER
} 