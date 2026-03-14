package com.example.schedulapatientapp


import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Appointment details", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { DetailsBottomNavigation(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F9FC)) // The exact light background from the image
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. DOCTOR IMAGE & NAME
            Surface(
                modifier = Modifier.size(90.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.LightGray
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("L", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
            Text("Dr. Kumar", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.height(16.dp))

            // 2. GYNECOLOGIST CARD
            InfoCard {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Cardiologist", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(color = Color(0xFF26C6DA), shape = CircleShape) {
                            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.WorkspacePremium, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Text(" Gold Medalist", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Surface(color = Color(0xFFEDF2F7), shape = RoundedCornerShape(8.dp)) {
                        Text("12 yrs exp", modifier = Modifier.padding(6.dp), fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            // 3. TIME & TOKEN CARD (Exactly like image)
            InfoCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFE3F2FD), shape = RoundedCornerShape(12.dp), modifier = Modifier.size(44.dp)) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.padding(10.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Consulting time", fontSize = 12.sp, color = Color.Gray)
                        Text("Oct 7, 8 PM", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Token", fontSize = 12.sp, color = Color.Gray)
                        Text("#47", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF2196F3))
                    }
                }
            }

            // 4. PATIENT DETAILS
            InfoCard {
                Column {
                    Text("Patient Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Name", fontSize = 12.sp, color = Color.Gray)
                            Text("Meena", fontWeight = FontWeight.Medium)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Age", fontSize = 12.sp, color = Color.Gray)
                            Text("28 yrs", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // 5. COMPLAINT
            InfoCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MedicalInformation, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Complaint", fontSize = 12.sp, color = Color.Gray)
                        Text("Stomach pain", fontWeight = FontWeight.Medium)
                    }
                }
            }

            // 6. LIVE TRACKING (With Teal Border)
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(2.dp, Color(0xFF26C6DA), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF26C6DA), CircleShape))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Live tracking", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("15 patients. ETA: 8:20 PM", fontSize = 13.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))








            // 7. STATUS INDICATORS (NO NAVIGATION HERE)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // We use Modifier.weight(1f) to make them sit side-by-side
                BlueButton("Waiting", isPrimary = false, modifier = Modifier.weight(1f)) { /* Status Only */ }
                BlueButton("Consulted", isPrimary = false, modifier = Modifier.weight(1f)) { /* Status Only */ }
            }

// Cancel Button in Red
            OutlinedButton(
                onClick = { navController.navigate("appointment_cancel") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(45.dp),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF5350))
            ) {
                Text("Cancel Appointment", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold)
            }















            // 7. ACTION BUTTONS (WORKING)
//            BlueButton("Reschedule", isPrimary = true) { /* Action */ }
//            BlueButton("Waiting", isPrimary = false) { /* Action */ }
//            BlueButton("Consulted", isPrimary = false) { /* Action */ }
//
//            // Cancel Button in Red
//            OutlinedButton(
//                onClick = { navController.navigate("appointment_cancel") },
//                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(45.dp),
//                shape = RoundedCornerShape(8.dp),
//                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF5350))
//            ) {
//                Text("Cancel", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold)
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))









            // 8. THE MAIN RESCHEDULE ACTION
            Button(
                onClick = {
                    //for next screen
                    // This opens the slot selection screen we built
                    navController.navigate("reschedule_appointment")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Reschedule Appointment", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
















            // 8. WORKING MAKE PAYMENT BUTTON
//            Button(
//                onClick = {
//                    // GO TO NEXT SCREEN
//                    navController.navigate("reschedule_appointment")
//                },
//                modifier = Modifier.fillMaxWidth().height(50.dp),
//                shape = RoundedCornerShape(8.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
//            ) {
//                Text("Reschedule", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//            }
//
//            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// COMPONENT: CLEAN INFO CARD
@Composable
fun InfoCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) { content() }
    }
}

// COMPONENT: CUSTOM BLUE BUTTONS
@Composable
fun BlueButton(text: String, isPrimary: Boolean, modifier:Modifier = Modifier, onClick: () -> Unit) {
    if (isPrimary) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(45.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text(text, color = Color.White, fontWeight = FontWeight.Bold)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(45.dp),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2196F3))
        ) {
            Text(text, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DetailsBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(icon = { Icon(Icons.Default.Search, null) }, label = { Text("Search") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Payments, null) }, label = { Text("Payments") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.CalendarToday, null) }, label = { Text("My Appt") }, selected = true, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") }, selected = false, onClick = { })
    }
}