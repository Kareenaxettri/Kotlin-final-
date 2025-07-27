package com.example.dipindonuts.data.repository

import com.example.dipindonuts.data.model.Order
import com.example.dipindonuts.data.model.OrderStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    suspend fun createOrder(order: Order): Result<String> {
        return try {
            val docRef = firestore.collection("orders").add(order).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getOrdersByUser(userId: String): Result<List<Order>> {
        return try {
            val snapshot = firestore.collection("orders")
                .whereEqualTo("userId", userId)
                // Temporarily removed orderBy to avoid index requirement
                // .orderBy("orderDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            
            val orders = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Order::class.java)?.copy(id = doc.id)
            }.sortedByDescending { it.orderDate } // Sort in memory instead
            
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getOrdersBySeller(sellerId: String): Result<List<Order>> {
        return try {
            val snapshot = firestore.collection("orders")
                .whereEqualTo("sellerId", sellerId)
                // Temporarily removed orderBy to avoid index requirement
                // .orderBy("orderDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            
            val orders = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Order::class.java)?.copy(id = doc.id)
            }.sortedByDescending { it.orderDate } // Sort in memory instead
            
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Result<Unit> {
        return try {
            firestore.collection("orders").document(orderId)
                .update("status", status)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getOrderById(orderId: String): Result<Order?> {
        return try {
            val document = firestore.collection("orders").document(orderId).get().await()
            val order = document.toObject(Order::class.java)?.copy(id = document.id)
            Result.success(order)
        } catch (e: Exception) {
           // Result.failure(e)
        }
    }
} 