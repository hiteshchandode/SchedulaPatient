package com.example.schedulapatientapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// --- SHARED COMPONENTS ---

@Composable
fun AppButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BookingBottomBar(navController: NavController, currentScreen: String) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        val items = listOf(
            Triple("Find Doctor", "doctor_list", Icons.Default.Search),
            Triple("Records", "records", Icons.Default.DateRange),
            Triple("Appointment", "booking_step1", Icons.Default.CheckCircle),
            Triple("Profile", "profile", Icons.Default.Person)
        )

        items.forEach { (label, route, icon) ->
            val isSelected = currentScreen == route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(route) {
                            popUpTo("doctor_list") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = Color.Black
                    )
                },
                icon = { Icon(icon, contentDescription = null) },
                colors = NavigationBarItemDefaults.colors(
                    // EXACT COLOR MATCH FOR SEARCH SCREEN
                    selectedIconColor = Color(0xFF374151), // Dark Grey/Black icon
                    indicatorColor = Color(0xFFE0E7FF),    // Very light lavender/blue pill
                    unselectedIconColor = Color(0xFF6B7280), // Medium grey for inactive
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.Black
                )
            )
        }
    }
}

// --- PAGE 4: BOOKING DETAILS ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingStepOne(navController: NavController, viewModel: BookingViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Booking Details", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { BookingBottomBar(navController, "booking_step1") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(modifier = Modifier.size(60.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFE3F2FD)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("Dr", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Dr. Kumar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Cardiologist", color = Color.Gray, fontSize = 14.sp)
                        Text("⭐ 4.9 (120 reviews)", color = Color(0xFFFFA000), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Select Date", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val dates = listOf("Mon\n1 Oct", "Tue\n2 Oct", "Wed\n3 Oct", "Thu\n4 Oct", "Fri\n5 Oct")
                dates.forEach { date ->
                    val isSelected = viewModel.selectedDate.contains(date.takeLast(5))
                    Surface(
                        modifier = Modifier.width(62.dp).clickable { viewModel.selectedDate = date.replace("\n", " ") },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFF2196F3) else Color.White,
                        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
                    ) {
                        Text(text = date, modifier = Modifier.padding(vertical = 12.dp), textAlign = TextAlign.Center, fontSize = 12.sp, color = if (isSelected) Color.White else Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Consulting type", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(viewModel.consultType, fontWeight = FontWeight.Medium)
                    Text("▼", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AppButton("Book Appointment →") {
                navController.navigate("booking_step2")
            }
        }
    }
}

// --- PAGE 5: TIME SELECTION ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSlotScreen(navController: NavController, viewModel: BookingViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Select Time", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BookingBottomBar(navController, "booking_step1") }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp).background(Color.White)) {
            Text("Available Slots", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            val slots = listOf("10:00 AM", "11:00 AM", "12:00 PM", "05:00 PM")
            slots.forEach { slot ->
                val isSelected = viewModel.selectedTime == slot
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clickable { viewModel.selectedTime = slot },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, if (isSelected) Color(0xFF2196F3) else Color.Transparent),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Text(slot, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AppButton("Next: Patient Details") { navController.navigate("patient_details") }
        }
    }
}

// --- PAGE 10: PATIENT DETAILS ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(navController: NavController, viewModel: BookingViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Patient Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BookingBottomBar(navController, "booking_step1") }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp).background(Color.White)) {
            OutlinedTextField(
                value = viewModel.patientName,
                onValueChange = { viewModel.patientName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = viewModel.patientAge,
                onValueChange = { viewModel.patientAge = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = viewModel.patientComplaint,
                onValueChange = { viewModel.patientComplaint = it },
                label = { Text("Complaint") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3
            )

            Spacer(modifier = Modifier.weight(1f))
            AppButton("Confirm Appointment") { navController.navigate("confirmation") }
        }
    }
}

// --- PAGE 7: CONFIRMATION ---

@Composable
fun ConfirmationScreen(navController: NavController, viewModel: BookingViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Appointment Confirmed!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(20.dp))
        Text("Your Token Number", color = Color.Gray)
        Text(viewModel.tokenNumber, fontSize = 60.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2196F3))
        Spacer(modifier = Modifier.height(32.dp))
        AppButton("Back to Search") {
            navController.navigate("doctor_list") {
                popUpTo("doctor_list") { inclusive = true }
            }
        }
    }
}



@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BookingBottomBar(navController, "profile") }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("User Profile Screen (Coming Soon)", fontSize = 20.sp, color = Color.Gray)
        }
    }
}



