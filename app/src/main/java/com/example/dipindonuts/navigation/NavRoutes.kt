package com.example.dipindonuts.navigation

sealed class NavRoutes(val route: String) {
    // Auth Routes
    object Splash : NavRoutes("splash")
    object Login : NavRoutes("login")
    object Signup : NavRoutes("signup")
    
    // Buyer Routes
    object BuyerHome : NavRoutes("buyer_home")
    object ProductDetail : NavRoutes("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object Cart : NavRoutes("cart")
    object Checkout : NavRoutes("checkout")
    object BuyerOrders : NavRoutes("buyer_orders")
    
    // Seller Routes
    object SellerHome : NavRoutes("seller_home")
    object AddProduct : NavRoutes("add_product")
    object EditProduct : NavRoutes("edit_product/{productId}") {
        fun createRoute(productId: String) = "edit_product/$productId"
    }
    //object SellerOrders : NavRoutes("seller_orders")
} 