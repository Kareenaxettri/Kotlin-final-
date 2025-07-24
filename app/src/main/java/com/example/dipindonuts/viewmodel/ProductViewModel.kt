package com.example.dipindonuts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dipindonuts.data.model.Product
import com.example.dipindonuts.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    
    private val _productsState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val productsState: StateFlow<ProductsState> = _productsState.asStateFlow()
    
    private val _sellerProductsState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val sellerProductsState: StateFlow<ProductsState> = _sellerProductsState.asStateFlow()
    
    fun loadAllProducts() {
        viewModelScope.launch {
            _productsState.value = ProductsState.Loading
            val result = productRepository.getAllProducts()
            _productsState.value = when {
                result.isSuccess -> ProductsState.Success(result.getOrNull() ?: emptyList())
                result.isFailure -> ProductsState.Error(result.exceptionOrNull()?.message ?: "Failed to load products")
                else -> ProductsState.Error("Unknown error occurred")
            }
        }
    }
    
    fun loadSellerProducts(sellerId: String) {
        viewModelScope.launch {
            _sellerProductsState.value = ProductsState.Loading
            val result = productRepository.getProductsBySeller(sellerId)
            _sellerProductsState.value = when {
                result.isSuccess -> ProductsState.Success(result.getOrNull() ?: emptyList())
                result.isFailure -> ProductsState.Error(result.exceptionOrNull()?.message ?: "Failed to load seller products")
                else -> ProductsState.Error("Unknown error occurred")
            }
        }
    }
    
    fun getProductById(productId: String, onComplete: (Product?) -> Unit) {
        viewModelScope.launch {
            val result = productRepository.getProductById(productId)
            if (result.isSuccess) {
                onComplete(result.getOrNull())
            } else {
                onComplete(null)
            }
        }
    }
    
    fun addProduct(product: Product, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = productRepository.addProduct(product)
            if (result.isSuccess) {
                onComplete(true, null)
                loadSellerProducts(product.sellerId)
            } else {
                onComplete(false, result.exceptionOrNull()?.message)
            }
        }
    }
    
    fun updateProduct(product: Product, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = productRepository.updateProduct(product)
            if (result.isSuccess) {
                onComplete(true, null)
                loadSellerProducts(product.sellerId)
            } else {
                onComplete(false, result.exceptionOrNull()?.message)
            }
        }
    }
    
    fun deleteProduct(productId: String, sellerId: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = productRepository.deleteProduct(productId)
            if (result.isSuccess) {
                onComplete(true, null)
                loadSellerProducts(sellerId)
            } else {
                onComplete(false, result.exceptionOrNull()?.message)
            }
        }
    }
}

sealed class ProductsState {
    object Loading : ProductsState()
    data class Success(val products: List<Product>) : ProductsState()
    data class Error(val message: String) : ProductsState()
} 