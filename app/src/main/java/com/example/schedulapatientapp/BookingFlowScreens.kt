package com.example.schedulapatientapp

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
                    selectedIconColor = Color(0xFF374151),
                    indicatorColor = Color(0xFFE0E7FF),
                    unselectedIconColor = Color(0xFF6B7280),
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
    val doctorName = viewModel.selectedDoctorName
    val doctorSpecialty = viewModel.selectedDoctorSpecialty
    val initialLetter = doctorName.replace("Dr. ", "").take(1).uppercase()
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val dynamicDates = remember {
        val dateList = mutableListOf<Pair<String, String>>()
        val calendar = java.util.Calendar.getInstance()
        val dayFormat = java.text.SimpleDateFormat("EEE", java.util.Locale.getDefault())
        val dateFormat = java.text.SimpleDateFormat("d MMM", java.util.Locale.getDefault())
        val fullDateFormat = java.text.SimpleDateFormat("d MMMM yyyy", java.util.Locale.getDefault())

        for (i in 0..4) {
            val dayName = dayFormat.format(calendar.time)
            val dateNumMonth = dateFormat.format(calendar.time)
            val fullDate = fullDateFormat.format(calendar.time)

            val displayText = "$dayName\n$dateNumMonth"
            dateList.add(Pair(displayText, fullDate))

            calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
        }
        dateList
    }

    LaunchedEffect(Unit) {
        if (viewModel.selectedDate.isEmpty()) {
            viewModel.selectedDate = dynamicDates.first().second
        }
    }

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
                            Text(initialLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(doctorSpecialty, color = Color.Gray, fontSize = 14.sp)
                        Text("⭐ 4.9 (120 reviews)", color = Color(0xFFFFA000), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Select Date", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                dynamicDates.forEach { (displayText, fullDate) ->
                    val isSelected = (viewModel.selectedDate == fullDate)

                    Surface(
                        modifier = Modifier.width(62.dp).clickable { viewModel.selectedDate = fullDate },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFF2196F3) else Color.White,
                        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
                    ) {
                        Text(text = displayText, modifier = Modifier.padding(vertical = 12.dp), textAlign = TextAlign.Center, fontSize = 12.sp, color = if (isSelected) Color.White else Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Consulting type", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isDropdownExpanded = true },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = viewModel.consultType,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Arrow",
                            tint = Color.Gray
                        )
                    }
                }

                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(Color.White)
                ) {
                    val consultingOptions = listOf(
                        "In-Person Consultation",
                        "Video Consultation",
                        "Audio Call Consultation"
                    )

                    consultingOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, fontSize = 14.sp) },
                            onClick = {
                                viewModel.consultType = option
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AppButton("Book Appointment →") {
                navController.navigate("booking_step2")
            }
        }
    }
}

// --- TIME SLOT SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSlotScreen(navController: NavController, viewModel: BookingViewModel) {
    val scrollState = rememberScrollState()
    val doctorName = viewModel.selectedDoctorName
    val doctorSpecialty = viewModel.selectedDoctorSpecialty
    val initialLetter = doctorName.replace("Dr. ", "").take(1).uppercase()
    val context = androidx.compose.ui.platform.LocalContext.current

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
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE3F2FD)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(initialLetter, color = Color(0xFF2196F3), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))

            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("SPECIALTY", color = Color(0xFF2196F3), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(doctorSpecialty, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("🕒 12 yrs Experience", color = Color.Gray, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "📅 Date: ${viewModel.selectedDate.ifEmpty { "Not selected" }}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1565C0)
                    )
                    Text(
                        text = "🩺 ${viewModel.consultType}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1565C0)
                    )
                }
            }

            Text("Choose your slot", modifier = Modifier.fillMaxWidth().padding(top = 20.dp), fontWeight = FontWeight.Bold)

            TimeSlotGroup(
                sectionTitle = "Morning",
                timeList = listOf("10:00 AM", "11:00 AM"),
                selectedTime = viewModel.selectedTime,
                onSelect = { viewModel.selectedTime = it }
            )

            TimeSlotGroup(
                sectionTitle = "Evening",
                timeList = listOf("05:00 PM", "06:00 PM"),
                selectedTime = viewModel.selectedTime,
                onSelect = { viewModel.selectedTime = it }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (viewModel.selectedTime.isEmpty()) {
                        android.widget.Toast.makeText(context, "Please select a time slot first!", android.widget.Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigate("exact_time_picker")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Next: Select time →", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- EXACT TIME PICKER WITH CLOCK DIAL ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectExactTimeScreen(navController: NavController, viewModel: BookingViewModel) {
    val scrollState = rememberScrollState()
    val doctorName = viewModel.selectedDoctorName
    val doctorSpecialty = viewModel.selectedDoctorSpecialty
    val initialLetter = doctorName.replace("Dr. ", "").take(1).uppercase()

    val timePickerState = rememberTimePickerState(
        initialHour = 10,
        initialMinute = 0,
        is24Hour = false
    )

    // Sync selected clock time to ViewModel continuously
    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        val formattedHour = if (timePickerState.hour % 12 == 0) 12 else timePickerState.hour % 12
        val period = if (timePickerState.hour >= 12) "PM" else "AM"
        val minuteStr = String.format("%02d", timePickerState.minute)
        viewModel.selectedTime = "$formattedHour:$minuteStr $period"
    }

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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(modifier = Modifier.size(60.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFE3F2FD)) {
                        Box(contentAlignment = Alignment.Center) { Text(initialLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 20.sp) }
                    }
                    Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
                    Text("$doctorSpecialty • 12 yrs Exp", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Choose time", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Next available slot", color = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Color(0xFFE2E8F0)), color = Color.White) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Default.DateRange, null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
                        Text(
                            text = " " + viewModel.selectedDate.ifEmpty { "Mar 26" },
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Morning  09:00 AM - 12:00 PM",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2196F3),
                modifier = Modifier.fillMaxWidth()
            )

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
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            selectorColor = Color(0xFF2196F3),
                            periodSelectorSelectedContainerColor = Color(0xFFE3F2FD),
                            periodSelectorSelectedContentColor = Color(0xFF2196F3)
                        )
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { navController.popBackStack() }) { Text("CANCEL", color = Color.Gray) }
                        TextButton(onClick = {
                            val formattedHour = if (timePickerState.hour % 12 == 0) 12 else timePickerState.hour % 12
                            val period = if (timePickerState.hour >= 12) "PM" else "AM"
                            val minuteStr = String.format("%02d", timePickerState.minute)
                            viewModel.selectedTime = "$formattedHour:$minuteStr $period"
                        }) { Text("OK", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            AppButton("Book Appointment →") {
                val formattedHour = if (timePickerState.hour % 12 == 0) 12 else timePickerState.hour % 12
                val period = if (timePickerState.hour >= 12) "PM" else "AM"
                val minuteStr = String.format("%02d", timePickerState.minute)
                viewModel.selectedTime = "$formattedHour:$minuteStr $period"

                navController.navigate("patient_form")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- FINAL CONFIRMATION SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalConfirmationScreen(
    navController: NavController,
    viewModel: BookingViewModel,
    mainViewModel: MainViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    val scrollState = rememberScrollState()
    val doctorName = viewModel.selectedDoctorName
    val doctorSpecialty = viewModel.selectedDoctorSpecialty
    val initialLetter = doctorName.replace("Dr. ", "").take(1).uppercase()

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
                        Box(contentAlignment = Alignment.Center) { Text(initialLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 20.sp) }
                    }
                    Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
                    Text("⭐ 4.9", color = Color(0xFFFFA000), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("$doctorSpecialty • 12 Years Experience", color = Color.Gray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Appointment confirmed with $doctorName.", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val assignedToken = "#" + (10..99).random()
                    viewModel.tokenNumber = assignedToken

                    mainViewModel.addAppointmentWithQueueDetails(
                        doctorName = viewModel.selectedDoctorName,
                        patientName = viewModel.patientName.ifEmpty { "Hitesh Chandode" },
                        date = viewModel.selectedDate,
                        timeSlot = viewModel.selectedTime,
                        tokenNumber = assignedToken,
                        doctorSpecialty = viewModel.selectedDoctorSpecialty,
                        consultType = viewModel.consultType,
                        patientComplaint = viewModel.patientComplaint.ifEmpty { "Routine Health Checkup" }
                    )
                    navController.navigate("appointment_details")
                },
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

// --- HELPER TIME SLOT GROUP ---
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
                    .clickable { onSelect(time) },
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

// --- OTHER EXISTING SCREENS ---
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
            Text("User Profile Screen", fontSize = 20.sp, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotUnavailableScreen(navController: NavController, viewModel: BookingViewModel) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Unable to book", fontWeight = FontWeight.Bold) }) },
        bottomBar = { BookingBottomBar(navController, "doctor_list") }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Slot Unavailable", fontSize = 18.sp, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDateScreen(navController: NavController, viewModel: BookingViewModel) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Plan appointment", fontWeight = FontWeight.Bold) }) },
        bottomBar = { BookingBottomBar(navController, "doctor_list") }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Plan Appointment Screen", fontSize = 18.sp, color = Color.Gray)
        }
    }
}
