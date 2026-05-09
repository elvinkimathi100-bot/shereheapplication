package com.mark.shereheke.repository

import com.mark.shereheke.model.Event
import com.mark.shereheke.network.ApiService
import retrofit2.Response

class EventRepository(private val apiService: ApiService) {

    suspend fun getEvents(): Map<String, Event>? {
        val response = apiService.getEvents()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun createEvent(event: Event): Boolean {
        val response = apiService.createEvent(event)
        return response.isSuccessful
    }

    suspend fun updateEvent(id: String, event: Event): Boolean {
        val response = apiService.updateEvent(id, event)
        return response.isSuccessful
    }

    suspend fun deleteEvent(id: String): Boolean {
        val response = apiService.deleteEvent(id)
        return response.isSuccessful
    }
}
