package com.example.dipindonuts.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dipindonuts.data.model.User
import com.example.dipindonuts.data.model.UserRole
import com.example.dipindonuts.ui.screens.auth.LoginScreen
import com.example.dipindonuts.ui.screens.auth.SignupScreen
import com.example.dipindonuts.ui.screens.auth.SplashScreen
import com.example.dipindonuts.ui.screens.buyer.BuyerHomeScreen
import com.example.dipindonuts.ui.screens.buyer.CartScreen
import com.example.dipindonuts.ui.screens.buyer.CheckoutScreen
import com.example.dipindonuts.ui.screens.buyer.OrdersScreen
import com.example.dipindonuts.ui.screens.buyer.ProductDetailScreen
import com.example.dipindonuts.ui.screens.seller.AddProductScreen
import com.example.dipindonuts.ui.screens.seller.EditProductScreen
import com.example.dipindonuts.ui.screens.seller.SellerHomeScreen
import com.example.dipindonuts.ui.screens.seller.SellerOrdersScreen
import com.example.dipindonuts.viewmodel.AuthViewModel

@Composable
fun DipInDonutsNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
//    val authState by authViewModel.authState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {
        // Auth Routes
        composable(NavRoutes.Splash.route) {
            SplashScreen(
                authState = authState,
                onNavigateToLogin = { navController.navigate(NavRoutes.Login.route) },
                onNavigateToHome = { user ->
                    when (user.role) {
                        UserRole.BUYER -> navController.navigate(NavRoutes.BuyerHome.route) {
                            popUpTo(NavRoutes.Splash.route) { inclusive = true }
                        }
                        UserRole.SELLER -> navController.navigate(NavRoutes.SellerHome.route) {
                            popUpTo(NavRoutes.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(NavRoutes.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignup = { navController.navigate(NavRoutes.Signup.route) },
                onNavigateToHome = { user ->
                    when (user.role) {
                        UserRole.BUYER -> navController.navigate(NavRoutes.BuyerHome.route) {
                            popUpTo(NavRoutes.Login.route) { inclusive = true }
                        }
                        UserRole.SELLER -> navController.navigate(NavRoutes.SellerHome.route) {
                            popUpTo(NavRoutes.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(NavRoutes.Signup.route) {
            SignupScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(NavRoutes.Login.route) },
                onNavigateToHome = { user ->
                    when (user.role) {
                        UserRole.BUYER -> navController.navigate(NavRoutes.BuyerHome.route) {
                            popUpTo(NavRoutes.Signup.route) { inclusive = true }
                        }
                        UserRole.SELLER -> navController.navigate(NavRoutes.SellerHome.route) {
                            popUpTo(NavRoutes.Signup.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        // Buyer Routes
        composable(NavRoutes.BuyerHome.route) {
            BuyerHomeScreen(
                onNavigateToProductDetail = { productId ->
                    navController.navigate(NavRoutes.ProductDetail.createRoute(productId))
                },
                onNavigateToCart = { navController.navigate(NavRoutes.Cart.route) },
                onNavigateToOrders = { navController.navigate(NavRoutes.BuyerOrders.route) },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavRoutes.ProductDetail.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductDetailScreen(
                productId = productId ?: "",
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCart = { navController.navigate(NavRoutes.Cart.route) }
            )
        }
        
        composable(NavRoutes.Cart.route) {
            CartScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCheckout = { navController.navigate(NavRoutes.Checkout.route) }
            )
        }
        
        composable(NavRoutes.Checkout.route) {
            CheckoutScreen(
                onNavigateBack = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate(NavRoutes.BuyerOrders.route) {
                        popUpTo(NavRoutes.BuyerHome.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavRoutes.BuyerOrders.route) {
            OrdersScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Seller Routes
        composable(NavRoutes.SellerHome.route) {
            SellerHomeScreen(
                onNavigateToAddProduct = { navController.navigate(NavRoutes.AddProduct.route) },
                onNavigateToEditProduct = { productId ->
                    navController.navigate(NavRoutes.EditProduct.createRoute(productId))
                },
                onNavigateToOrders = { navController.navigate(NavRoutes.SellerOrders.route) },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavRoutes.AddProduct.route) {
            AddProductScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.EditProduct.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            EditProductScreen(
                productId = productId ?: "",
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.SellerOrders.route) {
            SellerOrdersScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
} 