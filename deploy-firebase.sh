#!/bin/bash

# Dip in Donuts Firebase Deployment Script
echo "🍩 Deploying Firebase configuration for Dip in Donuts..."

# Check if Firebase CLI is installed
if ! command -v firebase &> /dev/null; then
    echo "❌ Firebase CLI not found. Please install it first:"
    echo "npm install -g firebase-tools"
    exit 1
fi

# Check if user is logged in
if ! firebase projects:list &> /dev/null; then
    echo "❌ Please login to Firebase first:"
    echo "firebase login"
    exit 1
fi

echo "📋 Available Firebase projects:"
firebase projects:list

echo ""
echo "🔧 Please enter your Firebase project ID:"
read -p "Project ID: " PROJECT_ID

if [ -z "$PROJECT_ID" ]; then
    echo "❌ Project ID is required"
    exit 1
fi

echo "🚀 Deploying to project: $PROJECT_ID"

# Initialize Firebase project
firebase use $PROJECT_ID

# Deploy Firestore rules
echo "📝 Deploying Firestore rules..."
firebase deploy --only firestore:rules

# Deploy Firestore indexes
echo "📊 Deploying Firestore indexes..."
firebase deploy --only firestore:indexes

# Deploy Storage rules
echo "📦 Deploying Storage rules..."
firebase deploy --only storage

echo "✅ Firebase configuration deployed successfully!"
echo ""
echo "🎉 Your Dip in Donuts app is ready to use!"
echo ""
echo "📱 Next steps:"
echo "1. Make sure google-services.json is in the app/ directory"
echo "2. Build and run the Android app"
echo "3. Test authentication and image upload functionality" 