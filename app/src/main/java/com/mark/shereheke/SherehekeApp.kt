package com.mark.shereheke

import android.app.Application
import com.cloudinary.android.MediaManager

class SherehekeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Cloudinary
        val config = mapOf(
            "cloud_name" to "YOUR_CLOUD_NAME", // Replace with your cloud name
            "api_key" to "YOUR_API_KEY",       // Replace with your API key
            "api_secret" to "YOUR_API_SECRET"  // Replace with your API secret
        )
        MediaManager.init(this, config)
    }
}
