package com.mark.shereheke.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mark.shereheke.model.sampleEvents
import com.mark.shereheke.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController, eventId: String?) {
    val event = sampleEvents.find { it.id == eventId }
    var quantity by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        event?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(text = "Summary", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = it.title, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Price: KES ${it.price}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Quantity", style = MaterialTheme.typography.bodyLarge)
                    Row {
                        IconButton(onClick = { if (quantity > 1) quantity-- }) {
                            Text("-")
                        }
                        Text(text = quantity.toString(), modifier = Modifier.padding(top = 12.dp))
                        IconButton(onClick = { quantity++ }) {
                            Text("+")
                        }
                    }
                }
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = "KES ${it.price * quantity}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { navController.navigate(Screen.Payment.createRoute(it.id)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Proceed to Payment")
                }
            }
        }
    }
}
