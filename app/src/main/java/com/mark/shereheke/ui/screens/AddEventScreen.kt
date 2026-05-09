package com.mark.shereheke.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.mark.shereheke.model.Event
import com.mark.shereheke.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(navController: NavController, viewModel: EventViewModel = viewModel()) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var venue by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var ticketPrice by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Event", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0F2027),
                    titleContentColor = Color(0xFFD4AF37)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F2027), Color(0xFF16213E))
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Picker Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Event Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Add Photo",
                            tint = Color(0xFFD4AF37),
                            modifier = Modifier.size(48.dp)
                        )
                        Text("Upload Event Banner", color = Color.White.copy(alpha = 0.6f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            CustomTextField(value = title, onValueChange = { title = it }, label = "Event Title", icon = Icons.Default.Event)
            CustomTextField(value = category, onValueChange = { category = it }, label = "Category (e.g. Gala, Concert)", icon = Icons.Default.Category)
            CustomTextField(value = description, onValueChange = { description = it }, label = "Description", icon = Icons.Default.Description, singleLine = false)
            CustomTextField(value = venue, onValueChange = { venue = it }, label = "Venue", icon = Icons.Default.Place)
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    CustomTextField(value = date, onValueChange = { date = it }, label = "Date", icon = Icons.Default.CalendarToday)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    CustomTextField(value = time, onValueChange = { time = it }, label = "Time", icon = Icons.Default.AccessTime)
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    CustomTextField(value = ticketPrice, onValueChange = { ticketPrice = it }, label = "Price (KES)", icon = Icons.Default.Payments)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    CustomTextField(value = capacity, onValueChange = { capacity = it }, label = "Capacity", icon = Icons.Default.People)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (title.isBlank() || imageUri == null) {
                        Toast.makeText(context, "Please fill in title and select an image", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    isUploading = true
                    // 1. Upload to Cloudinary
                    MediaManager.get().upload(imageUri)
                        .unsigned("sherehe_preset") // You need to create an unsigned upload preset in Cloudinary
                        .callback(object : UploadCallback {
                            override fun onStart(requestId: String?) {}
                            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                            override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                                val imageUrl = resultData?.get("secure_url") as String
                                
                                // 2. Create Event in Firebase
                                val newEvent = Event(
                                    title = title,
                                    category = category,
                                    description = description,
                                    venue = venue,
                                    date = date,
                                    time = time,
                                    ticketPrice = ticketPrice,
                                    capacity = capacity,
                                    imageUrl = imageUrl,
                                    hotelId = "hotel_123" // In real app, get from Auth
                                )
                                viewModel.addEvent(newEvent)
                                isUploading = false
                                Toast.makeText(context, "Event created successfully!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                            override fun onError(requestId: String?, error: ErrorInfo?) {
                                isUploading = false
                                Toast.makeText(context, "Upload failed: ${error?.description}", Toast.LENGTH_SHORT).show()
                            }
                            override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
                        }).dispatch()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                enabled = !isUploading
            ) {
                if (isUploading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Celebration, contentDescription = null, tint = Color.Black)
                        Spacer(Modifier.width(8.dp))
                        Text("POST VIBRANT EVENT", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White.copy(alpha = 0.6f)) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFFD4AF37)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFD4AF37),
            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = singleLine,
        maxLines = if (singleLine) 1 else 5
    )
}
