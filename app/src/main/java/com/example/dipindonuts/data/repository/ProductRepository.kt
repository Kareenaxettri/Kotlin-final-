package com.example.dipindonuts.data.repository

import com.example.dipindonuts.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val snapshot = firestore.collection("products")
                .whereEqualTo("available", true)
                .get()
                .await()
            
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getProductsBySeller(sellerId: String): Result<List<Product>> {
        return try {
            val snapshot = firestore.collection("products")
                .whereEqualTo("sellerId", sellerId)
                .get()
                .await()
            
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addProduct(product: Product): Result<String> {
        return try {
            val docRef = firestore.collection("products").add(product).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateProduct(product: Product): Result<Unit> {
        return try {
            firestore.collection("products").document(product.id).set(product).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            firestore.collection("products").document(productId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getProductById(productId: String): Result<Product?> {
        return try {
            val document = firestore.collection("products").document(productId).get().await()
            val product = document.toObject(Product::class.java)?.copy(id = document.id)
            Result.success(product)
        } catch (e: Exception) {
            //Result.failure(e)
        }
    }
} 