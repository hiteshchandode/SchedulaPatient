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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.schedulapatientapp.database.AppointmentEntity

//  Main appointments screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    // LIVE DATA & SCREEN STATE
    // Collect reactive appointment list from Room Database
    val appointmentsFromRoom by mainViewModel.appointmentsList.collectAsState()

    // State tracking the currently active tab: "Upcoming", "Past", or "Cancelled"
    var selectedTab by remember { mutableStateOf("Upcoming") }

    // Filter appointments dynamically according to the selected tab
    val displayedAppointments = remember(appointmentsFromRoom, selectedTab) {
        when (selectedTab) {
            "Upcoming" -> appointmentsFromRoom.filter { it.status != "Cancelled" && it.status != "Completed" }
            "Past" -> appointmentsFromRoom.filter { it.status == "Completed" || it.status != "Cancelled" }
            "Cancelled" -> appointmentsFromRoom.filter { it.status == "Cancelled" }
            else -> appointmentsFromRoom
        }
    }

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
                    // [UPDATED] Notification bell navigates directly to AppointmentRemindersScreen
                    IconButton(onClick = { navController.navigate("appointment_reminders") }) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = { AppointmentsBottomNavigation(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navigate to doctor list to book a new appointment
                    navController.navigate("doctor_list")
                },
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

            // [UPDATED] Interactive tab selector

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabButton(
                    text = "Upcoming",
                    isSelected = selectedTab == "Upcoming",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = "Upcoming" }
                )
                TabButton(
                    text = "Past",
                    isSelected = selectedTab == "Past",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = "Past" }
                )
                TabButton(
                    text = "Cancelled",
                    isSelected = selectedTab == "Cancelled",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = "Cancelled" }
                )
            }

            // Appointments list

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    DateHeader(
                        label = selectedTab.uppercase(),
                        date = when (selectedTab) {
                            "Upcoming" -> "Active Bookings"
                            "Past" -> "Completed Visits"
                            else -> "Cancelled Visits"
                        }
                    )
                }

                if (displayedAppointments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (selectedTab) {
                                    "Upcoming" -> "No upcoming appointments found."
                                    "Past" -> "No past consultations yet."
                                    else -> "No cancelled appointments."
                                },
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(displayedAppointments) { appointment ->
                        AppointmentCard(
                            appointment = appointment,
                            selectedTab = selectedTab, // Passed to switch action buttons dynamically
                            navController = navController,
                            onViewClick = { navController.navigate("appointment_details") }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}


// 3. Interactive tab button component

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit // Added click callback
) {
    Surface(
        modifier = modifier
            .height(40.dp)
            .clickable { onClick() }, // Enables clicking between tabs
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

// 4. SECTION DATE HEADER

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

// 5. DYNAMIC APPOINTMENT CARD COMPONENT

@Composable
fun AppointmentCard(
    appointment: AppointmentEntity,
    selectedTab: String, // Tracks current tab to display relevant contextual buttons
    navController: NavController,
    onViewClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Avatar, Doctor Name, Date & Time Slot
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Alphabet Avatar
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = Color(0xFFE3F2FD)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = appointment.doctorName.replace("Dr. ", "").take(1).uppercase(),
                            color = Color(0xFF2196F3),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(appointment.doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(14.dp), tint = Color(0xFF2196F3))
                        Text(" ${appointment.patientName}", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Default.AccessTime, null, modifier = Modifier.size(14.dp), tint = Color(0xFF2196F3))
                        Text(" ${appointment.date} • ${appointment.timeSlot}", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // [UPDATED] CONTEXTUAL ACTION BUTTONS (LIFECYCLE LOGIC)

            when (selectedTab) {
                // PHASE 1: UPCOMING (Before visit -> Reschedule or View Details)
                "Upcoming" -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = { navController.navigate("reschedule_by_doctor") },
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

                // PHASE 2: PAST (After visit -> View Summary or Leave Feedback & Rating)
                "Past" -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = onViewClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Text("Summary", color = Color(0xFF64748B))
                        }
                        Button(
                            onClick = { navController.navigate("consulting_feedback") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFFFFC107)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Rate Visit", color = Color.White)
                        }
                    }
                }

                // PHASE 3: CANCELLED (Re-book action)
                "Cancelled" -> {
                    Button(
                        onClick = { navController.navigate("doctor_list") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Text("Book Again", color = Color.White)
                    }
                }
            }
        }
    }
}

// 6. BOTTOM NAVIGATION BAR

@Composable
fun AppointmentsBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("doctor_list") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, null) },
            label = { Text("Payments", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("payments") }
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
            onClick = { navController.navigate("profile") }
        )
    }
}



