package com.mark.shereheke.network

import com.mark.shereheke.model.Event
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("events.json")
    suspend fun getEvents(): Response<Map<String, Event>>

    @POST("events.json")
    suspend fun createEvent(@Body event: Event): Response<Map<String, String>>

    @PATCH("events/{id}.json")
    suspend fun updateEvent(@Path("id") id: String, @Body event: Event): Response<Event>

    @DELETE("events/{id}.json")
    suspend fun deleteEvent(@Path("id") id: String): Response<Unit>
}
