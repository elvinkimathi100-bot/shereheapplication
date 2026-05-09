package com.mark.shereheke.data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.mark.shereheke.model.Event
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class EventViewModel : ViewModel() {

    private val database = FirebaseDatabase
        .getInstance("https://chamapay-34883-default-rtdb.firebaseio.com/")
        .getReference("events")

    // Observed list of all events
    var events by mutableStateOf<List<Event>>(emptyList())
        private set

    // Upload state for UI feedback
    var isUploading by mutableStateOf(false)
        private set

    init {
        fetchEvents()
    }

    // ─────────────────────────────────────────────
    // FETCH – real-time listener
    // ─────────────────────────────────────────────
    private fun fetchEvents() {
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                events = snapshot.children.mapNotNull {
                    it.getValue(Event::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("EventViewModel", "Fetch error: ${error.message}")
            }
        })
    }

    // ─────────────────────────────────────────────
    // ADD EVENT
    // ─────────────────────────────────────────────
    fun addEvent(
        event: Event,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val eventId = database.push().key ?: return onError("Could not generate event ID")

        val finalEvent = event.copy(id = eventId)

        database.child(eventId).setValue(finalEvent)
            .addOnSuccessListener {
                Log.d("EventViewModel", "Event added: $eventId")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("EventViewModel", it.message ?: "Unknown error")
                onError(it.message ?: "Failed to add event")
            }
    }

    // ─────────────────────────────────────────────
    // UPDATE EVENT
    // ─────────────────────────────────────────────
    fun updateEvent(
        event: Event,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (event.id.isBlank()) return onError("Event ID is missing")

        database.child(event.id).setValue(event)
            .addOnSuccessListener {
                Log.d("EventViewModel", "Event updated: ${event.id}")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("EventViewModel", it.message ?: "Update failed")
                onError(it.message ?: "Failed to update event")
            }
    }

    // ─────────────────────────────────────────────
    // DELETE EVENT
    // ─────────────────────────────────────────────
    fun deleteEvent(
        event: Event,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (event.id.isBlank()) return onError("Event ID is missing")

        database.child(event.id).removeValue()
            .addOnSuccessListener {
                Log.d("EventViewModel", "Event deleted: ${event.id}")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("EventViewModel", it.message ?: "Delete failed")
                onError(it.message ?: "Failed to delete event")
            }
    }

    // ─────────────────────────────────────────────
    // UPLOAD EVENT BANNER / IMAGE TO CLOUDINARY
    // ─────────────────────────────────────────────
    fun uploadEventImage(
        context: Context,
        imageUri: Uri,
        onSuccess: (String) -> Unit,   // returns the secure_url
        onError: (String) -> Unit
    ) {
        val uploadPreset = "sherehe"

        isUploading = true

        Thread {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                    ?: return@Thread run {
                        isUploading = false
                        onError("Could not open image")
                    }

                val bytes = inputStream.readBytes()
                inputStream.close()

                val boundary = "===" + System.currentTimeMillis() + "==="
                val url = URL("https://api.cloudinary.com/v1_1/dw4drxbto/image/upload")
                val connection = url.openConnection() as HttpURLConnection

                connection.doOutput = true
                connection.doInput  = true
                connection.requestMethod = "POST"
                connection.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=$boundary"
                )

                val output = connection.outputStream

                // --- upload_preset field ---
                output.write("--$boundary\r\n".toByteArray())
                output.write("Content-Disposition: form-data; name=\"upload_preset\"\r\n\r\n".toByteArray())
                output.write("$uploadPreset\r\n".toByteArray())

                // --- file field ---
                output.write("--$boundary\r\n".toByteArray())
                output.write("Content-Disposition: form-data; name=\"file\"; filename=\"event_image.jpg\"\r\n".toByteArray())
                output.write("Content-Type: image/jpeg\r\n\r\n".toByteArray())
                output.write(bytes)
                output.write("\r\n--$boundary--\r\n".toByteArray())
                output.flush()
                output.close()

                val responseCode = connection.responseCode
                val response = if (responseCode == 200) {
                    connection.inputStream.bufferedReader().readText()
                } else {
                    connection.errorStream?.bufferedReader()?.readText() ?: "Unknown error"
                }

                isUploading = false

                if (responseCode == 200) {
                    val json     = JSONObject(response)
                    val imageUrl = json.getString("secure_url")
                    onSuccess(imageUrl)
                } else {
                    onError("Upload failed: $response")
                }

            } catch (e: Exception) {
                isUploading = false
                onError(e.message ?: "Upload failed")
            }
        }.start()
    }

    // ─────────────────────────────────────────────
    // CONVENIENCE: upload image THEN add event
    // ─────────────────────────────────────────────
    fun uploadImageAndAddEvent(
        context: Context,
        imageUri: Uri,
        event: Event,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        uploadEventImage(
            context  = context,
            imageUri = imageUri,
            onSuccess = { imageUrl ->
                val eventWithImage = event.copy(imageUrl = imageUrl)
                addEvent(eventWithImage, onSuccess, onError)
            },
            onError = onError
        )
    }
}
