package com.example.schedulapatientapp


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Data Model to cleanly represent support cases
data class SupportTicket(
    val id: String,
    val title: String,
    val description: String,
    val status: String, // "Open", "In Progress", "Resolved"
    val date: String,
    val statusBgColor: Color,
    val statusTextColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerSupportScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Open, 1 = Resolved

    // Sample dynamic mock data matching the screenshot representation
    val openTickets = remember {
        listOf(
            SupportTicket(
                id = "SUP-1047",
                title = "Payment Refund Pending",
                description = "Refund for cancelled appointment has not been credited yet. Waiting for over 7 business days.",
                status = "Open",
                date = "20 July 2026",
                statusBgColor = Color(0xFFFEF3C7),
                statusTextColor = Color(0xFFD97706)
            ),
            SupportTicket(
                id = "SUP-1051",
                title = "Doctor Reschedule Issue",
                description = "The appointment was rescheduled without prior notice. Need urgent assistance with rescheduling.",
                status = "In Progress",
                date = "22 July 2026",
                statusBgColor = Color(0xFFDBEAFE),
                statusTextColor = Color(0xFF2563EB)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Customer S...",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1E293B)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF1E293B)
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { /* Handle new support ticket creation logic */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.padding(end = 12.dp).height(38.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AddCircleOutline, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("New Support", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { SupportBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF1F5F9))
        ) {

            // --- 1. PILL SWITCH SELECTOR TABS ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                color = if (selectedTab == 0) Color(0xFF2196F3) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedTab = 0 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Open",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 0) Color.White else Color(0xFF64748B),
                            fontSize = 14.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                color = if (selectedTab == 1) Color(0xFF2196F3) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedTab = 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Resolved",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 1) Color.White else Color(0xFF64748B),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // --- 2. CONTAINER BODY BLOCK WITH INNER CARDS ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (selectedTab == 0) {
                        // Summary Text Header
                        Text(
                            text = "${openTickets.size} OPEN TICKETS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Render Ticket Items List Loop
                        openTickets.forEach { ticket ->
                            TicketItemRow(ticket = ticket)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    } else {
                        // Empty/Fallback state display for Resolved category
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No resolved tickets found.", color = Color(0xFF94A3B8), fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketItemRow(ticket: SupportTicket) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Title & Badge Container Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = ticket.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                Surface(
                    color = ticket.statusBgColor,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = ticket.status,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ticket.statusTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Body Context Text Content Description
            Text(
                text = ticket.description,
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Meta Details Footers Container Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ConfirmationNumber, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(ticket.id, fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.width(16.dp))

                Icon(Icons.Outlined.CalendarToday, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(ticket.date, fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Outlined Action Interactive Target Hook Element
            OutlinedButton(
                onClick = { /* Redirect to individual ticket thread tracking layout details screen */ },
                modifier = Modifier.fillMaxWidth().height(38.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF3B82F6)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3B82F6))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("View Details", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.OpenInNew, null, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
fun SupportBottomBar(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(icon = { Icon(Icons.Default.PersonSearch, null) }, label = { Text("Find a Doctor", fontSize = 10.sp) }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Folder, null) }, label = { Text("My Records", fontSize = 10.sp) }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Outlined.CalendarMonth, null) }, label = { Text("My Appt", fontSize = 10.sp) }, selected = true, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile", fontSize = 10.sp) }, selected = false, onClick = { })
    }
}