package com.mark.shereheke.model

data class Ticket(
    val id: String,
    val eventId: String,
    val eventTitle: String,
    val date: String,
    val location: String,
    val price: Double,
    val quantity: Int,
    val status: String // e.g., "Paid", "Pending"
)
