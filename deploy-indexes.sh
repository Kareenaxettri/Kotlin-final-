#!/bin/bash

echo "🍩 Deploying Firebase indexes for Dip in Donuts..."

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

echo "🚀 Deploying indexes to project: $PROJECT_ID"

# Initialize Firebase project
firebase use $PROJECT_ID

# Deploy Firestore indexes
echo "📊 Deploying Firestore indexes..."
firebase deploy --only firestore:indexes

echo "✅ Firebase indexes deployed successfully!"
echo ""
echo "🎉 The missing index error should now be resolved!" 