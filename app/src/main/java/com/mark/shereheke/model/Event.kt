package com.mark.shereheke.model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("location")
    val location: String = "",
    @SerializedName("hotelName")
    val hotelName: String = "",
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("date")
    val date: String = "",          // e.g. "AUG 15"
    @SerializedName("time")
    val time: String = "",          // e.g. "14:00"
    @SerializedName("imageUrl")
    val imageUrl: String = "",      // Cloudinary secure_url
    @SerializedName("createdBy")
    val createdBy: String = "",     // member ID or name
    @SerializedName("createdAt")
    val createdAt: Long = System.currentTimeMillis(),
    
    // Additional fields to maintain compatibility with different screens
    @SerializedName("category")
    val category: String = "",
    @SerializedName("venue")
    val venue: String = "",
    @SerializedName("ticketPrice")
    val ticketPrice: String = "",
    @SerializedName("capacity")
    val capacity: String = "",
    @SerializedName("hotelId")
    val hotelId: String = ""
)

val sampleEvents = listOf(
    Event(
        id = "1",
        title = "Jazz & Soul Night",
        hotelName = "The Radisson Blu",
        location = "Nairobi, Upperhill",
        venue = "Nairobi, Upperhill",
        price = 3500.0,
        date = "AUG 15",
        time = "19:00",
        description = "An evening of smooth jazz and soulful melodies featuring local and international artists."
    ),
    Event(
        id = "2",
        title = "Summer Rooftop Party",
        hotelName = "Sarabi Rooftop",
        location = "Westlands, Nairobi",
        venue = "Westlands, Nairobi",
        price = 2000.0,
        date = "AUG 22",
        time = "14:00",
        description = "The ultimate summer party with the best DJs and refreshing cocktails."
    ),
    Event(
        id = "3",
        title = "Classical Gala",
        hotelName = "Villa Rosa Kempinski",
        location = "Westlands, Nairobi",
        venue = "Westlands, Nairobi",
        price = 5000.0,
        date = "SEP 05",
        time = "18:00",
        description = "A sophisticated evening of classical music and fine dining."
    )
)
