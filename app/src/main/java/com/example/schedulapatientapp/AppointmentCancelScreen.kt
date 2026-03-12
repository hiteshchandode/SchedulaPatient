package com.example.schedulapatientapp


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCancelScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Appointment cancel", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { CancelBottomNavigation(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F9FC))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- 1. DOCTOR INFO CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Doctor Image Placeholder (Letter L as requested previously)
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.LightGray
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("L", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Gynecologist", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("15 yrs experience", fontSize = 13.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(color = Color(0xFF26C6DA), shape = RoundedCornerShape(8.dp)) {
                            Text(
                                "Gold Medalist",
                                color = Color.White,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
            Text("Dr. Lavangi", fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp).fillMaxWidth(), textAlign = TextAlign.Start)

            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. APPOINTMENT DETAILS CARD ---
            CancelInfoCard(title = "Appointment Details") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFE3F2FD), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.CalendarToday, null, tint = Color(0xFF2196F3), modifier = Modifier.padding(8.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Oct 7, 8 PM", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Token: #47", fontSize = 13.sp, color = Color.Gray)
                    }
                }
            }

            // --- 3. PATIENT INFORMATION CARD ---
            CancelInfoCard(title = "Patient Information") {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    PatientDetailItem("Name", "Meena")
                    PatientDetailItem("Age", "28 Years")
                    PatientDetailItem("Sex", "Female")
                    Column {
                        Text("Weight", fontSize = 11.sp, color = Color.Gray)
                        Surface(border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(4.dp)) {
                            Text("55 kg", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // --- 4. COMPLAINT CARD ---
            CancelInfoCard(title = "Complaint") {
                Text("Stomach pain, nausea", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 5. CANCELLATION WARNING SECTION ---
            Surface(modifier = Modifier.size(40.dp), color = Color(0xFFFFEBEE), shape = CircleShape) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Cancel Appointment?", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "You are about to cancel your appointment with Dr. Lavangi.\nThis action cannot be undone.",
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 6. ACTION BUTTONS ---
            Button(
                onClick = {
                    // Logic to confirm cancellation
                    navController.navigate("my_appointments")
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)), // Red color
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Confirm cancelation", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Keep Appointment", color = Color.Gray, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// Helper Components
@Composable
fun CancelInfoCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
            content()
        }
    }
}

@Composable
fun PatientDetailItem(label: String, value: String) {
    Column {
        Text(label, fontSize = 11.sp, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
fun CancelBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(icon = { Icon(Icons.Default.Search, null) }, label = { Text("Search") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Payments, null) }, label = { Text("Payments") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.CalendarToday, null) }, label = { Text("My Appt") }, selected = true, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") }, selected = false, onClick = { })
    }
}