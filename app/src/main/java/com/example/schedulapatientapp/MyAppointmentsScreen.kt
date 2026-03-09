package com.example.schedulapatientapp


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


// PAGE: MY APPOINTMENTS
// Displays upcoming, past, and cancelled appointments with doctor details.


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Appointments", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = { AppointmentsBottomNavigation(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add new appointment action */ },
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
        ) {
            // --- TAB SELECTOR (Upcoming, Past, Cancelled) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabButton("Upcoming", isSelected = true, modifier = Modifier.weight(1f))
                TabButton("Past", isSelected = false, modifier = Modifier.weight(1f))
                TabButton("Cancelled", isSelected = false, modifier = Modifier.weight(1f))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    DateHeader("TODAY", "March 8, 2026")
                }

                // Sample Data representing the wireframe
                item {
                    AppointmentCard(
                        doctorName = "Dr. Lavangi",
                        patientName = "Meena",
                        time = "8:00 PM",
                        avatarLetter = "L",
                        onViewClick = { navController.navigate("medical_chat") }
                    )
                }

                item {
                    AppointmentCard(
                        doctorName = "Dr. Kumar",
                        patientName = "Muthukumar",
                        time = "Tomorrow, 11:00 AM",
                        avatarLetter = "K",
                        onViewClick = { }
                    )
                }

                item {
                    AppointmentCard(
                        doctorName = "Dr. Sarah Wilson",
                        patientName = "Meena",
                        time = "Mar 12, 10:30 AM",
                        avatarLetter = "S",
                        onViewClick = { }
                    )
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF2196F3) else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (isSelected) Color.White else Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DateHeader(label: String, date: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            color = Color(0xFFE3F2FD),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color(0xFF2196F3),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(date, color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun AppointmentCard(
    doctorName: String,
    patientName: String,
    time: String,
    avatarLetter: String,
    onViewClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Alphabet Avatar
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = Color(0xFFE3F2FD)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(avatarLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(14.dp), tint = Color(0xFF2196F3))
                        Text(" $patientName", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Default.AccessTime, null, modifier = Modifier.size(14.dp), tint = Color(0xFF2196F3))
                        Text(" $time", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF2196F3))
                ) {
                    Text("Reschedule", color = Color(0xFF2196F3))
                }
                Button(
                    onClick = onViewClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("View", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun AppointmentsBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search", fontSize = 10.sp) },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, null) },
            label = { Text("Payments", fontSize = 10.sp) },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, null) },
            label = { Text("My Appt", fontSize = 10.sp) },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2196F3),
                selectedTextColor = Color(0xFF2196F3)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile", fontSize = 10.sp) },
            selected = false,
            onClick = { }
        )
    }
}
