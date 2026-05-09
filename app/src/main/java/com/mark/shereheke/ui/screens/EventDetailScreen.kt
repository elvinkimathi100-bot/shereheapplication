package com.mark.shereheke.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mark.shereheke.model.Event

// ─────────────────────────────────────────────
//  Color palette
// ─────────────────────────────────────────────
private val DeepNavy    = Color(0xFF0D1B2A)
private val CardBg      = Color(0xFF162231)
private val AccentTeal  = Color(0xFF00C2A8)
private val AccentAmber = Color(0xFFFFC24B)
private val TextPrimary = Color(0xFFF0F4F8)
private val TextMuted   = Color(0xFF8BA3B8)
private val DividerCol  = Color(0xFF1E2E3D)

// ─────────────────────────────────────────────
//  Screen
// ─────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    events: List<Event>,
    isLoading: Boolean,
    onAddEvent: () -> Unit,
    onEditEvent: (Event) -> Unit,
    onDeleteEvent: (Event) -> Unit,
    onRefresh: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val filtered = remember(events, searchQuery) {
        if (searchQuery.isBlank()) events
        else events.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.location.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Shereheke",
                            color = AccentTeal,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        )
                        Text(
                            "${events.size} upcoming events",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = TextMuted)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepNavy)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEvent,
                containerColor = AccentTeal,
                contentColor = DeepNavy,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Search bar ──
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search events…", color = TextMuted) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentTeal,
                    unfocusedBorderColor = DividerCol,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = AccentTeal,
                    focusedContainerColor = CardBg,
                    unfocusedContainerColor = CardBg
                )
            )

            Spacer(Modifier.height(16.dp))

            // ── Content ──
            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AccentTeal)
                    }
                }
                filtered.isEmpty() -> {
                    EmptyState(hasSearch = searchQuery.isNotBlank())
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(filtered, key = { it.id }) { event ->
                            EventCard(
                                event = event,
                                onEdit = { onEditEvent(event) },
                                onDelete = {
                                    selectedEvent = event
                                    showDeleteDialog = true
                                }
                            )
                        }
                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }

    // ── Delete confirmation dialog ──
    if (showDeleteDialog && selectedEvent != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = CardBg,
            title = {
                Text("Delete Event", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${selectedEvent!!.title}\"? This cannot be undone.",
                    color = TextMuted
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedEvent?.let(onDeleteEvent)
                    showDeleteDialog = false
                    selectedEvent = null
                }) {
                    Text("Delete", color = Color(0xFFFF5C5C), fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = AccentTeal)
                }
            }
        )
    }
}

// ─────────────────────────────────────────────
//  Event card
// ─────────────────────────────────────────────
@Composable
private fun EventCard(
    event: Event,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // ── Banner image ──
            if (event.imageUrl.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                ) {
                    AsyncImage(
                        model = event.imageUrl,
                        contentDescription = event.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // gradient overlay for legibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, DeepNavy.copy(alpha = 0.7f))
                                )
                            )
                    )
                }
            }

            // ── Body ──
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = event.title.ifBlank { "Untitled Event" },
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        maxLines = if (expanded) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    // Date chip
                    if (event.date.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AccentTeal.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = event.date,
                                color = AccentTeal,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Location row
                if (event.location.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = AccentAmber,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(event.location, color = TextMuted, fontSize = 13.sp)
                    }
                    Spacer(Modifier.height(6.dp))
                }

                // Created by row
                if (event.createdBy.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(event.createdBy, color = TextMuted, fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(6.dp))
                }

                // Description (expandable)
                if (event.description.isNotBlank()) {
                    Text(
                        text = event.description,
                        color = TextMuted,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (expanded) "Show less" else "Show more",
                        color = AccentTeal,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                }

                Spacer(Modifier.height(12.dp))
                Divider(color = DividerCol)
                Spacer(Modifier.height(8.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDelete,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFFF5C5C).copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5C5C)),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Delete", fontSize = 13.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = onEdit,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentTeal,
                            contentColor = DeepNavy
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Edit", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
//  Empty state
// ─────────────────────────────────────────────
@Composable
private fun EmptyState(hasSearch: Boolean) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                if (hasSearch) Icons.Default.SearchOff else Icons.Outlined.EventBusy,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                if (hasSearch) "No events match \"$hasSearch\""
                else "No events yet",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                if (hasSearch) "Try a different search term"
                else "Tap + to create your first event",
                color = TextMuted,
                fontSize = 14.sp
            )
        }
    }
}