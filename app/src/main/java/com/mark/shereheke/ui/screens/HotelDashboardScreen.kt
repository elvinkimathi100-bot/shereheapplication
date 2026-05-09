package com.mark.shereheke.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mark.shereheke.model.sampleEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDashboardScreen(navController: NavController) {
    // For simplicity, we just filter events by a fake hotel
    val hotelEvents = sampleEvents.filter { it.hotelName.contains("Hilton") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hotel Dashboard") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Navigate to Create Event */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "My Events", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(hotelEvents) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = event.title, style = MaterialTheme.typography.titleMedium)
                                Text(text = event.date, style = MaterialTheme.typography.bodySmall)
                            }
                            Text(text = "KES ${event.price}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
