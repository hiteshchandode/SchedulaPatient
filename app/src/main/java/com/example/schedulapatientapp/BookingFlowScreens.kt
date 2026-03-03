package com.example.schedulapatientapp


import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
                val dates = listOf("Mon\n1 Mar", "Tue\n2 Mar", "Wed\n3 Mar", "Thu\n4 Mar", "Fri\n5 Mar")
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

//Time slot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSlotScreen(navController: NavController, viewModel: BookingViewModel) {
    val scrollState = rememberScrollState()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Doctor Initial Alphabet "K" (Same as Doctor Details)
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE3F2FD)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("K", color = Color(0xFF2196F3), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text("Dr. Kumar", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))

            // 2. Specialty Details Card (Same as Doctor Details)
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("SPECIALTY", color = Color(0xFF2196F3), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Cardiologist", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("🕒 12 yrs Experience", color = Color.Gray, fontSize = 13.sp)
                }
            }

            // 3. Time Slots Section (Morning and Evening)
            Text("Choose your slot", modifier = Modifier.fillMaxWidth().padding(top = 20.dp), fontWeight = FontWeight.Bold)

            // Morning Slots
            TimeSlotGroup(
                sectionTitle = "Morning",
                timeList = listOf("10:00 AM", "11:00 AM", ),
                selectedTime = viewModel.selectedTime,
                onSelect = { viewModel.selectedTime = it }
            )

            // Evening Slots
            TimeSlotGroup(
                sectionTitle = "Evening",
                timeList = listOf("05:00 PM", "06:00 PM", ),
                selectedTime = viewModel.selectedTime,
                onSelect = { viewModel.selectedTime = it }
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 4. Next Button
            AppButton("Next: Select time") {
                if (viewModel.selectedTime.isNotEmpty()) {
                    navController.navigate("exact_time_picker")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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



@Composable
fun TimeSlotGroup(
    sectionTitle: String,
    timeList: List<String>,
    selectedTime: String,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        Text(sectionTitle, fontWeight = FontWeight.Bold, color = Color.Gray)

        timeList.forEach { time ->
            val isSelected = (time == selectedTime)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable { onSelect(time) }, // When you click the box, it selects the time
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFF2196F3) else Color.White
                ),
                border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = time,
                        color = if (isSelected) Color.White else Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelect(time) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.White,
                            unselectedColor = Color.LightGray
                        )
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectExactTimeScreen(navController: NavController, viewModel: BookingViewModel) {
    val scrollState = rememberScrollState()

    // 1. This is the "Memory" for the Clock (Setting default to 10:00 AM)
    val timePickerState = rememberTimePickerState(
        initialHour = 10,
        initialMinute = 0,
        is24Hour = false // This ensures it shows 1-12 with AM/PM
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Booking Screen", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BookingBottomBar(navController, "doctor_list") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- DOCTOR CARD (Same as before) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(modifier = Modifier.size(60.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFE3F2FD)) {
                        Box(contentAlignment = Alignment.Center) { Text("Dr", color = Color(0xFF2196F3)) }
                    }
                    Text("Dr. Kumar", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
                    Text("Cardiologist • 12 yrs Exp", color = Color.Gray, fontSize = 14.sp)
                }
            }

            // --- CHOOSE TIME HEADER ---
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Choose time", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Next available slot", color = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))
                // Date Display
                Surface(shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Color(0xFFE2E8F0)), color = Color.White) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Default.DateRange, null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
                        Text(" march 26", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            // --- MORNING HEADER ---
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Morning  09:00 AM - 12:00 PM",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2196F3),
                modifier = Modifier.fillMaxWidth()
            )

            // --- THE REAL INTERACTIVE CLOCK CARD ---
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // This shows the Time Picker (Hours 1-12)
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            selectorColor = Color(0xFF2196F3), // The blue hand
                            periodSelectorSelectedContainerColor = Color(0xFFE3F2FD), // AM/PM box
                            periodSelectorSelectedContentColor = Color(0xFF2196F3)
                        )
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { /* Clear selection */ }) { Text("CANCEL", color = Color.Gray) }
                        TextButton(onClick = {
                            // Save picked time to ViewModel
                            viewModel.selectedTime = "${timePickerState.hour}:${timePickerState.minute}"
                        }) { Text("OK", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold) }
                    }
                }
            }

            // --- BUTTON ---
            Spacer(modifier = Modifier.height(32.dp))
            AppButton("Book Appointment →") {
                navController.navigate("final_confirmation")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

//confirmation screen after dial clock screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalConfirmationScreen(navController: NavController, viewModel: BookingViewModel) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Booking Confirmation", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BookingBottomBar(navController, "doctor_list") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. DOCTOR INFO CARD (Top Section)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(modifier = Modifier.size(60.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFE3F2FD)) {
                        Box(contentAlignment = Alignment.Center) { Text("Doc", color = Color(0xFF2196F3)) }
                    }
                    Text("Dr. Kumar", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
                    Text("⭐ 4.9", color = Color(0xFFFFA000), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Cardiologist • 12 Years Experience", color = Color.Gray, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(color = Color(0xFFE0F7FA), shape = RoundedCornerShape(20.dp)) {
                        Text("🥇 GOLD MEDALIST", color = Color(0xFF00ACC1), modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. SUCCESS MESSAGE & TOKEN BOX
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)), // Light Blue background
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Appointment confirmed with Dr. Kumar.", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Token Small Card
                        Surface(modifier = Modifier.weight(1f), color = Color.White, shape = RoundedCornerShape(12.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("TOKEN", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("#14", color = Color(0xFF2196F3), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                        // Time Small Card
                        Surface(modifier = Modifier.weight(1f), color = Color.White, shape = RoundedCornerShape(12.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("TIME", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("10:15 AM", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. APPOINTMENT DETAILS (Text Rows)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailRow("DATE", "Monday, 5 March 2026")
                    DetailRow("DURATION", "30 Minutes")
                    DetailRow("VISIT TYPE", "In-Person")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. ACTION BUTTONS
            OutlinedButton(
                onClick = { /* Add to Calendar */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF2196F3))
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF2196F3))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add to Calendar", color = Color(0xFF2196F3))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("doctor_list") },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("View Appointment →", fontWeight = FontWeight.Bold)
            }

            TextButton(onClick = { navController.navigate("doctor_list") }) {
                Text("Back to Home", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// Helper for the Text Rows
@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}