package com.example.dipindonuts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dipindonuts.data.model.CartItem
import com.example.dipindonuts.data.model.Product
import com.example.dipindonuts.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    
    private val _cartState = MutableStateFlow<CartState>(CartState.Loading)
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()
    
    fun loadCartItems(userId: String) {
        viewModelScope.launch {
            _cartState.value = CartState.Loading
            val result = cartRepository.getCartItems(userId)
            _cartState.value = when {
                result.isSuccess -> {
                    val items = result.getOrNull() ?: emptyList()
                    CartState.Success(items, calculateTotal(items))
                }
                result.isFailure -> CartState.Error(result.exceptionOrNull()?.message ?: "Failed to load cart")
                else -> CartState.Error("Unknown error occurred")
            }
        }
    }
    
    fun addToCart(product: Product, userId: String, quantity: Int = 1) {
        viewModelScope.launch {
            val cartItem = CartItem(
                productId = product.id,
                productName = product.name,
                productPrice = product.price,
                productImageUrl = product.imageUrl,
                quantity = quantity,
                userId = userId,
                sellerId = product.sellerId
            )
            
            val result = cartRepository.addToCart(cartItem)
            if (result.isSuccess) {
                loadCartItems(userId)
            }
        }
    }
    
    fun updateCartItemQuantity(cartItem: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            val updatedItem = cartItem.copy(quantity = newQuantity)
            val result = cartRepository.updateCartItem(updatedItem)
            if (result.isSuccess) {
                loadCartItems(cartItem.userId)
            }
        }
    }
    
    fun removeFromCart(cartItemId: String, userId: String) {
        viewModelScope.launch {
            val result = cartRepository.removeFromCart(cartItemId)
            if (result.isSuccess) {
                loadCartItems(userId)
            }
        }
    }
    
    fun clearCart(userId: String) {
        viewModelScope.launch {
            val result = cartRepository.clearCart(userId)
            if (result.isSuccess) {
                _cartState.value = CartState.Success(emptyList(), 0.0)
            }
        }
    }
    
    private fun calculateTotal(items: List<CartItem>): Double {
        return items.sumOf { it.productPrice * it.quantity }
    }
}

sealed class CartState {
    object Loading : CartState()
    data class Success(val items: List<CartItem>, val total: Double) : CartState()
    //data class Error(val message: String) : CartState()
} 