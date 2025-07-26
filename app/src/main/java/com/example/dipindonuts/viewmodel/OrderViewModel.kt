package com.example.dipindonuts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dipindonuts.data.model.CartItem
import com.example.dipindonuts.data.model.Order
import com.example.dipindonuts.data.model.OrderItem
import com.example.dipindonuts.data.model.OrderStatus
import com.example.dipindonuts.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {
    
    private val _userOrdersState = MutableStateFlow<OrdersState>(OrdersState.Loading)
    val userOrdersState: StateFlow<OrdersState> = _userOrdersState.asStateFlow()
    
    private val _sellerOrdersState = MutableStateFlow<OrdersState>(OrdersState.Loading)
    val sellerOrdersState: StateFlow<OrdersState> = _sellerOrdersState.asStateFlow()
    
    //private val _orderCreationState = MutableStateFlow<OrderCreationState>(OrderCreationState.Idle)
    val orderCreationState: StateFlow<OrderCreationState> = _orderCreationState.asStateFlow()
    
    fun loadUserOrders(userId: String) {
        viewModelScope.launch {
            _userOrdersState.value = OrdersState.Loading
            val result = orderRepository.getOrdersByUser(userId)
            _userOrdersState.value = when {
                result.isSuccess -> OrdersState.Success(result.getOrNull() ?: emptyList())
                result.isFailure -> OrdersState.Error(result.exceptionOrNull()?.message ?: "Failed to load orders")
                else -> OrdersState.Error("Unknown error occurred")
            }
        }
    }
    
    fun loadSellerOrders(sellerId: String) {
        viewModelScope.launch {
            _sellerOrdersState.value = OrdersState.Loading
            val result = orderRepository.getOrdersBySeller(sellerId)
            _sellerOrdersState.value = when {
                result.isSuccess -> OrdersState.Success(result.getOrNull() ?: emptyList())
                result.isFailure -> OrdersState.Error(result.exceptionOrNull()?.message ?: "Failed to load seller orders")
                else -> OrdersState.Error("Unknown error occurred")
            }
        }
    }
    
    fun createOrder(
        userId: String,
        sellerId: String,
        cartItems: List<CartItem>,
        deliveryAddress: String,
        customerName: String,
        customerPhone: String
    ) {
        viewModelScope.launch {
            _orderCreationState.value = OrderCreationState.Loading
            
            val orderItems = cartItems.map { cartItem ->
                OrderItem(
                    productId = cartItem.productId,
                    productName = cartItem.productName,
                    quantity = cartItem.quantity,
                    price = cartItem.productPrice
                )
            }
            
            val totalAmount = cartItems.sumOf { it.productPrice * it.quantity }
            
            val order = Order(
                userId = userId,
                sellerId = sellerId,
                items = orderItems,
                totalAmount = totalAmount,
                deliveryAddress = deliveryAddress,
                customerName = customerName,
                customerPhone = customerPhone
            )
            
            val result = orderRepository.createOrder(order)
            _orderCreationState.value = when {
                result.isSuccess -> OrderCreationState.Success(result.getOrNull() ?: "")
                result.isFailure -> OrderCreationState.Error(result.exceptionOrNull()?.message ?: "Failed to create order")
                else -> OrderCreationState.Error("Unknown error occurred")
            }
        }
    }
    
    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        viewModelScope.launch {
            val result = orderRepository.updateOrderStatus(orderId, status)
            // Reload orders after status update
            // You might want to reload specific orders based on the context
        }
    }
    
    fun resetOrderCreationState() {
        _orderCreationState.value = OrderCreationState.Idle
    }
}

sealed class OrdersState {
    object Loading : OrdersState()
    data class Success(val orders: List<Order>) : OrdersState()
    data class Error(val message: String) : OrdersState()
}

sealed class OrderCreationState {
    object Idle : OrderCreationState()
    object Loading : OrderCreationState()
    data class Success(val orderId: String) : OrderCreationState()
    data class Error(val message: String) : OrderCreationState()
} 