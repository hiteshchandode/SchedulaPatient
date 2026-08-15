package com.example.schedulapatientapp

// =========================================================================
// >>> 1. ALL NECESSARY IMPORTS (NO UNRESOLVED REFERENCES) >>>
// =========================================================================
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.schedulapatientapp.database.AppointmentEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentRemindersScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    val context = LocalContext.current

    // =========================================================================
    // >>> 2. DYNAMIC DATABASE APPOINTMENTS COLLECTION >>>
    // =========================================================================
    val appointmentsFromRoom by mainViewModel.appointmentsList.collectAsState()
    val activeUpcomingAppointments = appointmentsFromRoom.filter { it.status != "Cancelled" }

    // Dynamic Doctor Banner Info (Pulls the latest booked doctor or fallback)
    val topDoctorName = activeUpcomingAppointments.firstOrNull()?.doctorName ?: "Dr. Lavangi"
    val topDoctorInitial = topDoctorName.replace("Dr. ", "").take(1).ifEmpty { "L" }

    // Interactive Toggle States
    var isPushEnabled by remember { mutableStateOf(true) }
    var isSmsEnabled by remember { mutableStateOf(false) }
    val selectedTiming by remember { mutableStateOf("1 Hour Before") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Appointment Reminders",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(38.dp)
                            .background(Color(0xFFF8FAFC), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF64748B)
                        )
                    }
                },
                actions = {
                    Box(modifier = Modifier.padding(end = 12.dp), contentAlignment = Alignment.TopEnd) {
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Reminders are actively synced", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFFF0FDF4), CircleShape)
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = Color(0xFF2196F3), modifier = Modifier.size(20.dp))
                        }
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color(0xFFF97316), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val count = if (activeUpcomingAppointments.isNotEmpty()) activeUpcomingAppointments.size else 2
                            Text("$count", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { RemindersBottomNavigation(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 1. DYNAMIC DOCTOR CONTEXT CARD ---
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(70.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF2196F3)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(topDoctorInitial, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(topDoctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("🩺 Specialist Consultant", fontSize = 13.sp, color = Color(0xFF64748B))
                        Text("🧰 15+ yrs Experience", fontSize = 13.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(color = Color(0xFF00B4D8), shape = CircleShape) {
                            Text(
                                "Gold Medalist",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. REMINDER CONFIGURATION & ALERT PREFERENCES ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Alert Timing", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                        }

                        Surface(
                            color = Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = selectedTiming,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2196F3),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Push Notification", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF334155))
                            Text("Receive popup reminder on phone", fontSize = 11.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = isPushEnabled,
                            onCheckedChange = { isPushEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF2196F3))
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF1F5F9))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("SMS Notification", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF334155))
                            Text("Direct text message to registered mobile", fontSize = 11.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = isSmsEnabled,
                            onCheckedChange = { isSmsEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF2196F3))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. SUB-SECTION TITLE WITH SCHEDULED BADGE ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Upcoming Reminders", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                Surface(color = Color(0xFFE0F2FE), shape = CircleShape) {
                    val count = if (activeUpcomingAppointments.isNotEmpty()) activeUpcomingAppointments.size else 2
                    Text(
                        "$count Scheduled",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color(0xFF0284C7),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // =========================================================================
            // >>> 4. DYNAMIC ROOM DB REMINDERS LIST (CLEAN, ZERO ERROR) >>>
            // =========================================================================
            if (activeUpcomingAppointments.isNotEmpty()) {
                activeUpcomingAppointments.forEachIndexed { index, appt ->
                    val isFirst = (index == 0)
                    ReminderItemCard(
                        reminderNumber = "#${index + 1}",
                        doctorName = appt.doctorName,
                        patientName = appt.patientName,
                        dateLabel = appt.date,
                        timeLabel = appt.timeSlot,
                        statusTag = if (isFirst) "Upcoming" else "Scheduled",
                        statusColor = if (isFirst) Color(0xFFEA580C) else Color(0xFF16A34A),
                        statusBgColor = if (isFirst) Color(0xFFFFF7ED) else Color(0xFFF0FDF4),
                        onClick = { navController.navigate("appointment_details") }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                // Fallback default mockup cards when database is empty
                ReminderItemCard(
                    reminderNumber = "#1",
                    doctorName = "Dr. Lavangi",
                    patientName = "Muthukumar",
                    dateLabel = "Today",
                    timeLabel = "10:00 AM",
                    statusTag = "Today",
                    statusColor = Color(0xFFEA580C),
                    statusBgColor = Color(0xFFFFF7ED),
                    onClick = { navController.navigate("appointment_details") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                ReminderItemCard(
                    reminderNumber = "#2",
                    doctorName = "Dr. Suresh Patil",
                    patientName = "Yourself",
                    dateLabel = "Tomorrow",
                    timeLabel = "11:00 AM",
                    statusTag = "Tomorrow",
                    statusColor = Color(0xFF16A34A),
                    statusBgColor = Color(0xFFF0FDF4),
                    onClick = { navController.navigate("appointment_details") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Clean, reusable card that formats styled text internally without compiler errors
@Composable
fun ReminderItemCard(
    reminderNumber: String,
    doctorName: String,
    patientName: String,
    dateLabel: String,
    timeLabel: String,
    statusTag: String,
    statusColor: Color,
    statusBgColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Left Blue accent stripe
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF2196F3), RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )

            Column(modifier = Modifier.padding(16.dp).padding(start = 6.dp)) {
                // Top tag row with unread blue dot indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(color = Color(0xFFF1F5F9), shape = CircleShape) {
                        Text(
                            "Reminder $reminderNumber",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = Color(0xFF475569),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF2196F3), CircleShape))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Informational text body block safely built
                Text(
                    text = buildAnnotatedString {
                        append("You have an appointment with ")
                        withStyle(SpanStyle(color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)) {
                            append("$doctorName ")
                        }
                        append("on ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("$dateLabel at $timeLabel ")
                        }
                        append("for ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(patientName)
                        }
                        append(".")
                    },
                    fontSize = 14.sp,
                    color = Color(0xFF334155),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom row details indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CalendarMonth, null, tint = Color(0xFF2196F3), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(dateLabel, fontSize = 13.sp, color = Color(0xFF64748B))

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(Icons.Outlined.AccessTime, null, tint = Color(0xFF2196F3), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(timeLabel, fontSize = 13.sp, color = Color(0xFF64748B))
                    }

                    Surface(color = statusBgColor, shape = CircleShape) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (statusTag == "Today") "⚠️ $statusTag" else "📅 $statusTag",
                                color = statusColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// Standard 4-Tab Bottom Navigation Bar
@Composable
fun RemindersBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("doctor_list") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Payments") },
            label = { Text("Payments", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("payments") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "My Appt") },
            label = { Text("My Appt", fontSize = 10.sp) },
            selected = true,
            onClick = { navController.navigate("my_appointments") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2196F3),
                selectedTextColor = Color(0xFF2196F3)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("profile") }
        )
    }
}
























































//package com.example.schedulapatientapp
//
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBackIosNew
//import androidx.compose.material.icons.filled.Folder
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.PersonSearch
//import androidx.compose.material.icons.outlined.AccessTime
//import androidx.compose.material.icons.outlined.CalendarMonth
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppointmentRemindersScreen(navController: NavController) {
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = {
//                    Text(
//                        "Appointment Reminders",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp,
//                        color = Color(0xFF1E293B),
//                        modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
//                    )
//                },
//                navigationIcon = {
//                    IconButton(
//                        onClick = { navController.popBackStack() },
//                        modifier = Modifier
//                            .padding(start = 8.dp)
//                            .size(38.dp)
//                            .background(Color(0xFFF8FAFC), CircleShape)
//                    ) {
//                        Icon(
//                            Icons.Default.ArrowBackIosNew,
//                            contentDescription = "Back",
//                            modifier = Modifier.size(14.dp),
//                            tint = Color(0xFF64748B)
//                        )
//                    }
//                },
//                actions = {
//                    // Badge notification bell matching the design header
//                    Box(modifier = Modifier.padding(end = 12.dp), contentAlignment = Alignment.TopEnd) {
//                        IconButton(
//                            onClick = { },
//                            modifier = Modifier
//                                .size(38.dp)
//                                .background(Color(0xFFF0FDF4), CircleShape)
//                        ) {
//                            Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = Color(0xFF2196F3), modifier = Modifier.size(20.dp))
//                        }
//                        Box(
//                            modifier = Modifier
//                                .size(16.dp)
//                                .background(Color(0xFFF97316), CircleShape),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text("2", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
//                        }
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
//            )
//        },
//        bottomBar = { RemindersBottomNavigation(navController) }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(Color(0xFFF8FAFC))
//                .verticalScroll(rememberScrollState())
//                .padding(horizontal = 16.dp, vertical = 8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            // --- 1. DOCTOR CONTEXT CARD ---
//            Card(
//                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth().padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Surface(
//                        modifier = Modifier.size(70.dp),
//                        shape = RoundedCornerShape(12.dp),
//                        color = Color(0xFFCBD5E1)
//                    ) {
//                        Box(contentAlignment = Alignment.Center) {
//                            Text("L", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
//                        }
//                    }
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Column {
//                        Text("Dr. Lavangi", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Text("🩺 Gynecologist", fontSize = 13.sp, color = Color(0xFF64748B))
//                        Text("🧰 15 yrs Experience", fontSize = 13.sp, color = Color(0xFF64748B))
//                        Spacer(modifier = Modifier.height(6.dp))
//                        Surface(color = Color(0xFF00B4D8), shape = CircleShape) {
//                            Text(
//                                "Gold Medalist",
//                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//                                color = Color.White,
//                                fontSize = 11.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // --- 2. SUB-SECTION TITLE WITH UNREAD BADGE ---
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text("Upcoming Reminders", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
//                Surface(color = Color(0xFFE0F2FE), shape = CircleShape) {
//                    Text(
//                        "2 Unread",
//                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//                        color = Color(0xFF0284C7),
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // --- 3. REMINDER LIST ROWS (MATCHING PDF LOGIC DATA) ---
//
//            // Reminder Item #1[cite: 1]
//            ReminderItemCard(
//                reminderNumber = "#1",
//                text = buildAnnotatedString {
//                    append("You have an appointment with ")
//                    withStyle(style = SpanStyle(color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)) { append("Dr. Lavangi ") }
//                    append("today at ")
//                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("10:00 AM ") }
//                    append("for ")
//                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Muthukumar.") }
//                },
//                dateLabel = "Today",
//                timeLabel = "10:00 AM",
//                statusTag = "Today",
//                statusColor = Color(0xFFEA580C),
//                statusBgColor = Color(0xFFFFF7ED)
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Reminder Item #2[cite: 1]
//            ReminderItemCard(
//                reminderNumber = "#2",
//                text = buildAnnotatedString {
//                    append("You have an appointment with ")
//                    withStyle(style = SpanStyle(color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)) { append("Dr. Kumar ") }
//                    append("tomorrow at ")
//                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("11:00 AM ") }
//                    append("for ")
//                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("yourself.") }
//                },
//                dateLabel = "Tomorrow",
//                timeLabel = "11:00 AM",
//                statusTag = "Tomorrow",
//                statusColor = Color(0xFF16A34A),
//                statusBgColor = Color(0xFFF0FDF4)
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//    }
//}
//
//@Composable
//fun ReminderItemCard(
//    reminderNumber: String,
//    text: androidx.compose.ui.text.AnnotatedString,
//    dateLabel: String,
//    timeLabel: String,
//    statusTag: String,
//    statusColor: Color,
//    statusBgColor: Color
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
//    ) {
//        Box(modifier = Modifier.fillMaxWidth()) {
//            // Left Blue accent stripe
//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterStart)
//                    .width(4.dp)
//                    .fillMaxHeight()
//                    .background(Color(0xFF2196F3), RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
//            )
//
//            Column(modifier = Modifier.padding(16.dp).padding(start = 6.dp)) {
//                // Top tag row with unread blue dot indicators
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Surface(color = Color(0xFFF1F5F9), shape = CircleShape) {
//                        Text(
//                            "Reminder $reminderNumber",
//                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//                            color = Color(0xFF475569),
//                            fontSize = 12.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF2196F3), CircleShape))
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Informational text body block
//                Text(text = text, fontSize = 14.sp, color = Color(0xFF334155), lineHeight = 20.sp)
//
//                Spacer(modifier = Modifier.height(14.dp))
//
//                // Bottom row details indicators
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(Icons.Outlined.CalendarMonth, null, tint = Color(0xFF2196F3), modifier = Modifier.size(16.dp))
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(dateLabel, fontSize = 13.sp, color = Color(0xFF64748B))
//
//                        Spacer(modifier = Modifier.width(12.dp))
//
//                        Icon(Icons.Outlined.AccessTime, null, tint = Color(0xFF2196F3), modifier = Modifier.size(16.dp))
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(timeLabel, fontSize = 13.sp, color = Color(0xFF64748B))
//                    }
//
//                    Surface(color = statusBgColor, shape = CircleShape) {
//                        Row(
//                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Text(
//                                if (statusTag == "Today") "⚠️ $statusTag" else "📅 $statusTag",
//                                color = statusColor,
//                                fontSize = 11.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun RemindersBottomNavigation(navController: NavController) {
//    NavigationBar(containerColor = Color.White) {
//        NavigationBarItem(icon = { Icon(Icons.Default.PersonSearch, null) }, label = { Text("Find a Doctor") }, selected = false, onClick = { })
//        NavigationBarItem(icon = { Icon(Icons.Default.Folder, null) }, label = { Text("My Records") }, selected = false, onClick = { })
//        NavigationBarItem(icon = { Icon(Icons.Outlined.CalendarMonth, null) }, label = { Text("My Appt") }, selected = true, onClick = { })
//        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") }, selected = false, onClick = { navController.navigate("profile") })
//    }
//}