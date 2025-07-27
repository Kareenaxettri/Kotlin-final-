package com.example.dipindonuts.data.repository

import com.example.dipindonuts.data.model.CartItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    suspend fun getCartItems(userId: String): Result<List<CartItem>> {
        return try {
            val snapshot = firestore.collection("cartItems")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            val cartItems = snapshot.documents.mapNotNull { doc ->
                doc.toObject(CartItem::class.java)?.copy(id = doc.id)
            }
            Result.success(cartItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addToCart(cartItem: CartItem): Result<String> {
        return try {
            val docRef = firestore.collection("cartItems").add(cartItem).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateCartItem(cartItem: CartItem): Result<Unit> {
        return try {
            firestore.collection("cartItems").document(cartItem.id).set(cartItem).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun removeFromCart(cartItemId: String): Result<Unit> {
        return try {
            firestore.collection("cartItems").document(cartItemId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun clearCart(userId: String): Result<Unit> {
        return try {
            val snapshot = firestore.collection("cartItems")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            val batch = firestore.batch()
            snapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            //Result.failure(e)
        }
    }
} 