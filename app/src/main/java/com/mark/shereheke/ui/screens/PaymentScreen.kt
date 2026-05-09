package com.mark.shereheke.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mark.shereheke.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, eventId: String?) {
    var phoneNumber by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("M-Pesa Payment") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Pay via M-Pesa", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Enter your M-Pesa phone number to receive a payment prompt.")
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number (e.g., 0712...)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (isProcessing) {
                CircularProgressIndicator()
                Text(text = "Sending STK Prompt...", modifier = Modifier.padding(top = 8.dp))
            } else {
                Button(
                    onClick = {
                        isProcessing = true
                        // Simulate payment processing
                        // After success:
                        // navController.navigate(Screen.MyTickets.route) {
                        //     popUpTo(Screen.Home.route) { inclusive = false }
                        // }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pay Now")
                }
            }
            
            // Temporary button to simulate success and go to tickets
            TextButton(onClick = { navController.navigate(Screen.MyTickets.route) }) {
                Text("Simulate Success")
            }
        }
    }
}
