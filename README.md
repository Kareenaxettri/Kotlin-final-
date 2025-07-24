# Dip in Donuts 🍩

A modern donut-selling mobile application built with Kotlin, Jetpack Compose, and Firebase.

## Features

### 🔐 Authentication
- Email/Password login and signup
- Role-based user system (Buyer/Seller)
- Secure Firebase Authentication

### 👥 Buyer Features
- Browse available donuts
- View product details with images
- Add items to cart with quantity selection
- Shopping cart management
- Checkout process with delivery details
- Order history and tracking

### 🏪 Seller Features
- Add new donut products with image upload (Base64)
- Edit existing products
- Delete products with confirmation
- Manage product inventory
- View and manage received orders
- Order status updates

## Tech Stack

- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Backend**: Firebase (Auth + Firestore)
- **Image Storage**: Base64 encoding in Firestore (cost-effective)
- **Language**: Kotlin
- **Dependency Injection**: Hilt
- **Navigation**: Navigation Compose
- **Image Loading**: Coil
- **Asynchronous Programming**: Coroutines & Flow

## Project Structure

```
com.example.dipindonuts/
├── data/
│   ├── model/           # Data classes (User, Product, CartItem, Order)
│   └── repository/      # Firebase integration and business logic
├── di/                  # Hilt dependency injection modules
├── navigation/          # Navigation setup and routes
├── ui/
│   ├── screens/         # Composable screens
│   │   ├── auth/        # Authentication screens
│   │   ├── buyer/       # Buyer-specific screens
│   │   └── seller/      # Seller-specific screens
│   └── theme/           # Theme and styling
├── viewmodel/           # ViewModels per screen/module
├── MainActivity.kt      # Entry point
└── DipInDonutsApp.kt    # Application class
```

## Firebase Structure

### Collections
- **users**: User profiles with roles (buyer/seller)
- **products**: Donut products with details and Base64 images
- **cartItems**: Shopping cart items
- **orders**: Order information and status

### User Roles
- **Buyer**: Can browse, add to cart, and place orders
- **Seller**: Can manage products and view received orders

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+
- Firebase project

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/dipindonuts.git
   cd dipindonuts
   ```

2. **Firebase Setup**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Enable Authentication (Email/Password)
   - Enable Firestore Database
   - Download `google-services.json` and place it in the `app/` directory

3. **Firebase Configuration**

   **Authentication:**
   - Go to Authentication > Sign-in method
   - Enable Email/Password provider

   **Firestore Database:**
   - Go to Firestore Database > Rules
   - Replace with the rules from `firestore.rules`
   - Go to Firestore Database > Indexes
   - Import the indexes from `firestore.indexes.json`

4. **Build and Run**
   - Open the project in Android Studio
   - Sync Gradle files
   - Run the app on an emulator or device

## Firebase Security Rules

### Firestore Rules
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can read/write their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Products can be read by all, written by sellers
    match /products/{productId} {
      allow read: if true;
      allow write: if request.auth != null && 
        get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'seller';
    }
    
    // Cart items belong to users
    match /cartItems/{itemId} {
      allow read, write: if request.auth != null && 
        resource.data.userId == request.auth.uid;
    }
    
    // Orders can be read by buyer and seller
    match /orders/{orderId} {
      allow read, write: if request.auth != null && 
        (resource.data.userId == request.auth.uid || 
         resource.data.sellerId == request.auth.uid);
    }
  }
}
```

## Image Storage Strategy

### Base64 Encoding
- Images are converted to Base64 strings and stored directly in Firestore
- **Advantages:**
  - No additional Firebase Storage costs
  - Works with free tier
  - No permission issues
  - Simpler setup
- **Considerations:**
  - Images are compressed to 800px max dimension
  - JPEG quality set to 80% for optimal size
  - Base64 increases size by ~33% but still cost-effective

## Troubleshooting

### Common Issues

1. **"Create index in console" error**
   - Solution: Import the `firestore.indexes.json` file in Firebase Console
   - Go to Firestore Database > Indexes > Import

2. **Infinite loading in Seller Dashboard**
   - Solution: Make sure the user is properly authenticated and has seller role
   - Check Firebase Authentication is enabled

3. **Image upload not working**
   - Solution: Check that image permissions are granted
   - Ensure images are not too large (app automatically compresses)

4. **Cart not updating**
   - Solution: Ensure user is authenticated before accessing cart
   - Check Firestore rules for cartItems collection

### Build Issues

1. **Gradle sync fails**
   - Clean and rebuild project
   - Invalidate caches and restart Android Studio

2. **Missing dependencies**
   - Sync project with Gradle files
   - Check internet connection for dependency downloads

## Architecture Overview

### MVVM Pattern
- **Model**: Data classes and repositories
- **View**: Jetpack Compose UI components
- **ViewModel**: Business logic and state management

### State Management
- Uses Kotlin Flow for reactive state management
- Sealed classes for UI states (Loading, Success, Error)
- Single source of truth for data

### Dependency Injection
- Hilt for dependency injection
- Singleton repositories
- ViewModel injection with Hilt

## Key Components

### Authentication Flow
1. Splash screen checks for existing user
2. Login/Signup screens with role selection
3. Role-based navigation to appropriate home screen

### Buyer Flow
1. Browse products in grid layout
2. View product details with images
3. Add to cart functionality with quantity
4. Shopping cart management
5. Checkout process with delivery details
6. Order tracking

### Seller Flow
1. Product management dashboard
2. Add/Edit product forms with image upload
3. Order management
4. Status updates

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Firebase for backend services
- Jetpack Compose for modern UI development
- Material Design 3 for beautiful components
- The donut community for inspiration 🍩 