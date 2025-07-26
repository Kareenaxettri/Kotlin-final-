package com.example.dipindonuts.data.model

data class Order(
    val id: String = "",
    val userId: String = "",
    val sellerId: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: OrderStatus = OrderStatus.PENDING,
    val orderDate: Long = System.currentTimeMillis(),
    val deliveryAddress: String = "",
    val customerName: String = "",
    val customerPhone: String = ""
)

data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 1,
    val price: Double = 0.0
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    READY_FOR_PICKUP,
    DELIVERED,
    //CANCELLED
} 