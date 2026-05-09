package com.mark.shereheke.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mark.shereheke.model.Event
import com.mark.shereheke.network.RetrofitClient
import com.mark.shereheke.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repository = EventRepository(RetrofitClient.instance)

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchEvents()
    }

    fun fetchEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val eventMap = repository.getEvents()
                if (eventMap != null) {
                    _events.value = eventMap.map { (key, value) ->
                        // If id is part of the object, we use it, otherwise we use the Firebase key
                        value.copy(id = key)
                    }
                } else {
                    _errorMessage.value = "Failed to load events"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.createEvent(event)
                if (success) {
                    fetchEvents() // Refresh list
                } else {
                    _errorMessage.value = "Failed to add event"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateEvent(id: String, event: Event) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.updateEvent(id, event)
                if (success) {
                    fetchEvents()
                } else {
                    _errorMessage.value = "Failed to update event"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteEvent(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.deleteEvent(id)
                if (success) {
                    fetchEvents()
                } else {
                    _errorMessage.value = "Failed to delete event"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}
