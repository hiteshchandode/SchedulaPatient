package com.example.schedulapatientapp

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailsScreen(
    navController: NavController,
    viewModel: BookingViewModel,
    mainViewModel: MainViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    val context = LocalContext.current
    val liveAppointment by mainViewModel.latestAppointment.collectAsState()

    // Determine payment state dynamically from Room DB or fallback to ViewModel
    val dbPaymentMode = liveAppointment?.paymentMode ?: viewModel.paymentMode
    val dbPaymentStatus = liveAppointment?.paymentStatus ?: if (dbPaymentMode == "Online") "Paid" else "Pending"

    var isPaidOnline by remember(dbPaymentStatus, dbPaymentMode) {
        mutableStateOf(dbPaymentStatus == "Paid" || dbPaymentMode == "Online")
    }
    var showPaymentProcessDialog by remember { mutableStateOf(false) }

    // Toggle segment state (Waiting / Consulted)
    var selectedSegment by remember { mutableStateOf("Waiting") }

    // Accordion state for More Options
    var isMoreOptionsExpanded by remember { mutableStateOf(true) }

    // --- DYNAMIC LIVE TOKEN & QUEUE CALCULATIONS ---
    val userTokenStr = liveAppointment?.tokenNumber ?: viewModel.tokenNumber.ifEmpty { "#14" }
    val userTokenInt = userTokenStr.replace("#", "").toIntOrNull() ?: 14

    // Live serving token directly from Room DB
    val servingTokenInt = liveAppointment?.currentServingToken ?: (userTokenInt - 3).coerceAtLeast(1)
    val patientsAhead = (userTokenInt - servingTokenInt).coerceAtLeast(0)
    val estimatedWaitMins = patientsAhead * 5

    val doctorName = liveAppointment?.doctorName ?: viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" }
    val doctorSpecialty = liveAppointment?.doctorSpecialty ?: viewModel.selectedDoctorSpecialty.ifEmpty { "General" }
    val displayDate = liveAppointment?.date ?: viewModel.selectedDate.ifEmpty { "5 March 2026" }
    val displayTime = liveAppointment?.timeSlot ?: viewModel.selectedTime.ifEmpty { "10:00 AM" }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Appointment details", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { DetailsBottomNavigation(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF4F6F9))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // --- 1. DOCTOR INFO CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = Color(0xFF2196F3)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = doctorName.replace("Dr. ", "").take(1).uppercase(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF1E293B))
                        Text(doctorSpecialty, color = Color(0xFF2196F3), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    "GOLD MEDALIST",
                                    color = Color(0xFFD97706),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("12 yrs exp", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // --- 2. CONSULTING TIME & TOKEN CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("CONSULTING TIME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("$displayDate • $displayTime", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                        }
                        Surface(
                            color = Color(0xFFE0F2FE),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Token $userTokenStr",
                                color = Color(0xFF0284C7),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Status", fontSize = 13.sp, color = Color.Gray)
                        Text(
                            text = if (isPaidOnline) "Paid Online" else "Pay at Reception",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isPaidOnline) Color(0xFF16A34A) else Color(0xFF2563EB)
                        )
                    }
                }
            }

            // --- 3. PATIENT DETAILS GRID CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("NAME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                liveAppointment?.patientName ?: viewModel.patientName.ifEmpty { "Hitesh Chandode" },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1E293B)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("AGE / SEX", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                viewModel.patientAge.ifEmpty { "24 / Male" },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1E293B)
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("WEIGHT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("68 kg", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("COMPLAINT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                liveAppointment?.patientComplaint ?: viewModel.patientComplaint.ifEmpty { "Fever" },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1E293B)
                            )
                        }
                    }
                }
            }

            // --- 4. CYAN LIVE TRACKING CARD (REAL-TIME ROOM DB UPDATES) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(Color(0xFF00ACC1), CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("LIVE TRACKING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00796B))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text("#$servingTokenInt", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF00796B))
                            Text("Serving now", fontSize = 11.sp, color = Color(0xFF00897B))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (patientsAhead == 0) "Your Turn Now!" else "$patientsAhead patients ahead",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF00796B)
                            )
                            Text(
                                text = if (patientsAhead == 0) "Please enter room" else "Est. wait: $estimatedWaitMins mins",
                                fontSize = 11.sp,
                                color = Color(0xFF00897B)
                            )
                        }
                    }
                }
            }

            // --- 5. CHAT WITH DOCTOR BUTTON CARD ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("medical_chat") },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Chat with Doctor", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B), modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                }
            }

            // --- 6. RESCHEDULE APPOINTMENT BUTTON ---
            Button(
                onClick = { navController.navigate("reschedule_appointment") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Reschedule Appointment", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
            }

            // --- 7. SEGMENTED TOGGLE (WAITING / CONSULTED) ---
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedSegment == "Waiting") Color.White else Color.Transparent)
                            .clickable { selectedSegment = "Waiting" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Waiting",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (selectedSegment == "Waiting") Color(0xFF2196F3) else Color.Gray
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedSegment == "Consulted") Color.White else Color.Transparent)
                            .clickable {
                                selectedSegment = "Consulted"
                                navController.navigate("consulting_feedback")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Consulted",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (selectedSegment == "Consulted") Color(0xFF2196F3) else Color.Gray
                        )
                    }
                }
            }

            // --- 8. MORE OPTIONS ACCORDION ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isMoreOptionsExpanded = !isMoreOptionsExpanded }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("More options", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                    Icon(
                        imageVector = if (isMoreOptionsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }

                AnimatedVisibility(visible = isMoreOptionsExpanded) {
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Handle unable to meet */ }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.EventBusy, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Unable to meet", fontSize = 14.sp, color = Color(0xFF475569), fontWeight = FontWeight.Medium)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("appointment_cancel") }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.Cancel, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Cancel Appointment", fontSize = 14.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // --- 9. MAKE PAYMENT BUTTON ---
            Button(
                onClick = {
                    if (isPaidOnline) {
                        Toast.makeText(context, "Appointment is already paid!", Toast.LENGTH_SHORT).show()
                    } else {
                        showPaymentProcessDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPaidOnline) Color(0xFF16A34A) else Color(0xFF2196F3)
                )
            ) {
                Icon(
                    imageVector = if (isPaidOnline) Icons.Default.CheckCircle else Icons.Default.Payment,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isPaidOnline) "Payment Completed ✓" else "Make Payment (₹500)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- ONLINE PAYMENT SIMULATION DIALOG ---
        if (showPaymentProcessDialog) {
            AlertDialog(
                onDismissRequest = { showPaymentProcessDialog = false },
                shape = RoundedCornerShape(16.dp),
                containerColor = Color.White,
                title = { Text("Complete Online Payment", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Doctor Consultation Fee: ₹500", fontWeight = FontWeight.Medium)
                        Text("Select payment option (UPI / Credit Card / NetBanking)", fontSize = 12.sp, color = Color.Gray)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE))
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color(0xFF0284C7))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("GPay / PhonePe / Paytm UPI", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            isPaidOnline = true
                            viewModel.paymentMode = "Online"

                            // Save payment status update into Room DB
                            liveAppointment?.let { appt ->
                                mainViewModel.updatePayment(appt.id, paymentStatus = "Paid", paymentMode = "Online")
                            }

                            showPaymentProcessDialog = false
                            Toast.makeText(context, "Payment Successful! ₹500 Paid.", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Text("Pay ₹500 Now")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPaymentProcessDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}

// --- HELPER BOTTOM NAVIGATION COMPONENT ---
@Composable
fun DetailsBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("doctor_list") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Payments, null) },
            label = { Text("Payments", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("payments") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.CalendarToday, null) },
            label = { Text("My Appt", fontSize = 10.sp) },
            selected = true,
            onClick = { navController.navigate("my_appointments") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile", fontSize = 10.sp) },
            selected = false,
            onClick = { }
        )
    }
}


















































//package com.example.schedulapatientapp
//
//import android.widget.Toast
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material.icons.outlined.Cancel
//import androidx.compose.material.icons.outlined.EventBusy
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppointmentDetailsScreen(
//    navController: NavController,
//    viewModel: BookingViewModel,
//    mainViewModel: MainViewModel = viewModel(
//        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
//            LocalContext.current.applicationContext as android.app.Application
//        )
//    )
//) {
//    val context = LocalContext.current
//    val liveAppointment by mainViewModel.latestAppointment.collectAsState()
//
//    // Payment state linked with ViewModel
//    var isPaidOnline by remember { mutableStateOf(viewModel.paymentMode == "Online") }
//    var showPaymentProcessDialog by remember { mutableStateOf(false) }
//
//    // Toggle segment state (Waiting / Consulted)
//    var selectedSegment by remember { mutableStateOf("Waiting") }
//
//    // Accordion state for More Options
//    var isMoreOptionsExpanded by remember { mutableStateOf(true) }
//
//    // Dynamic Live Token Values
//    val userTokenStr = liveAppointment?.tokenNumber ?: viewModel.tokenNumber.ifEmpty { "#14" }
//    val userTokenInt = userTokenStr.replace("#", "").toIntOrNull() ?: 14
//    val servingTokenInt = liveAppointment?.currentServingToken ?: (userTokenInt - 3).coerceAtLeast(1)
//    val patientsAhead = (userTokenInt - servingTokenInt).coerceAtLeast(0)
//
//    val doctorName = liveAppointment?.doctorName ?: viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" }
//    val doctorSpecialty = liveAppointment?.doctorSpecialty ?: viewModel.selectedDoctorSpecialty.ifEmpty { "General" }
//    val displayDate = liveAppointment?.date ?: viewModel.selectedDate.ifEmpty { "5 March 2026" }
//    val displayTime = liveAppointment?.timeSlot ?: viewModel.selectedTime.ifEmpty { "10:00 AM" }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Appointment details", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
//            )
//        },
//        bottomBar = { DetailsBottomNavigation(navController) }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(Color(0xFFF4F6F9))
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(14.dp)
//        ) {
//
//            // --- 1. DOCTOR INFO CARD ---
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(0.dp)
//            ) {
//                Row(
//                    modifier = Modifier.padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Surface(
//                        modifier = Modifier.size(56.dp),
//                        shape = CircleShape,
//                        color = Color(0xFF2196F3)
//                    ) {
//                        Box(contentAlignment = Alignment.Center) {
//                            Text(
//                                text = doctorName.replace("Dr. ", "").take(1).uppercase(),
//                                fontSize = 24.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = Color.White
//                            )
//                        }
//                    }
//                    Spacer(modifier = Modifier.width(14.dp))
//                    Column {
//                        Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF1E293B))
//                        Text(doctorSpecialty, color = Color(0xFF2196F3), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
//                        Spacer(modifier = Modifier.height(6.dp))
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Surface(
//                                color = Color(0xFFFEF3C7),
//                                shape = RoundedCornerShape(4.dp)
//                            ) {
//                                Text(
//                                    "GOLD MEDALIST",
//                                    color = Color(0xFFD97706),
//                                    fontSize = 9.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
//                                )
//                            }
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text("12 yrs exp", fontSize = 11.sp, color = Color.Gray)
//                        }
//                    }
//                }
//            }
//
//            // --- 2. CONSULTING TIME & TOKEN CARD ---
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(0.dp)
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Column {
//                            Text("CONSULTING TIME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
//                            Spacer(modifier = Modifier.height(2.dp))
//                            Text("$displayDate • $displayTime", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
//                        }
//                        Surface(
//                            color = Color(0xFFE0F2FE),
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Text(
//                                "Token $userTokenStr",
//                                color = Color(0xFF0284C7),
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 12.sp,
//                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
//                            )
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text("Status", fontSize = 13.sp, color = Color.Gray)
//                        Text(
//                            text = if (isPaidOnline) "Paid Online" else "Pay at Reception",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 13.sp,
//                            color = if (isPaidOnline) Color(0xFF16A34A) else Color(0xFF2563EB)
//                        )
//                    }
//                }
//            }
//
//            // --- 3. PATIENT DETAILS GRID CARD ---
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(0.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(14.dp)
//                ) {
//                    Row(modifier = Modifier.fillMaxWidth()) {
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text("NAME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
//                            Spacer(modifier = Modifier.height(2.dp))
//                            Text(
//                                liveAppointment?.patientName ?: viewModel.patientName.ifEmpty { "Hitesh Chandode" },
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 14.sp,
//                                color = Color(0xFF1E293B)
//                            )
//                        }
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text("AGE / SEX", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
//                            Spacer(modifier = Modifier.height(2.dp))
//                            Text(
//                                viewModel.patientAge.ifEmpty { "24 / Male" },
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 14.sp,
//                                color = Color(0xFF1E293B)
//                            )
//                        }
//                    }
//                    Row(modifier = Modifier.fillMaxWidth()) {
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text("WEIGHT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
//                            Spacer(modifier = Modifier.height(2.dp))
//                            Text("68 kg", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
//                        }
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text("COMPLAINT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
//                            Spacer(modifier = Modifier.height(2.dp))
//                            Text(
//                                liveAppointment?.patientComplaint ?: viewModel.patientComplaint.ifEmpty { "Fever" },
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 14.sp,
//                                color = Color(0xFF1E293B)
//                            )
//                        }
//                    }
//                }
//            }
//
//            // --- 4. CYAN LIVE TRACKING CARD ---
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(0.dp)
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Box(modifier = Modifier.size(8.dp).background(Color(0xFF00ACC1), CircleShape))
//                        Spacer(modifier = Modifier.width(6.dp))
//                        Text("LIVE TRACKING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00796B))
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.Bottom
//                    ) {
//                        Column {
//                            Text("#$servingTokenInt", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF00796B))
//                            Text("Serving now", fontSize = 11.sp, color = Color(0xFF00897B))
//                        }
//                        Column(horizontalAlignment = Alignment.End) {
//                            Text("$patientsAhead patients ahead", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF00796B))
//                            Text("Est. wait: 15 mins", fontSize = 11.sp, color = Color(0xFF00897B))
//                        }
//                    }
//                }
//            }
//
//            // --- 5. CHAT WITH DOCTOR BUTTON CARD ---
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { navController.navigate("medical_chat") },
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                shape = RoundedCornerShape(12.dp),
//                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
//                elevation = CardDefaults.cardElevation(0.dp)
//            ) {
//                Row(
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(18.dp))
//                    Spacer(modifier = Modifier.width(12.dp))
//                    Text("Chat with Doctor", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B), modifier = Modifier.weight(1f))
//                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
//                }
//            }
//
//            // --- 6. RESCHEDULE APPOINTMENT BUTTON ---
//            Button(
//                onClick = { navController.navigate("reschedule_appointment") },
//                modifier = Modifier.fillMaxWidth().height(48.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
//            ) {
//                Text("Reschedule Appointment", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
//            }
//
//            // --- 7. SEGMENTED TOGGLE (WAITING / CONSULTED) ---
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(44.dp),
//                color = Color(0xFFF1F5F9),
//                shape = RoundedCornerShape(10.dp)
//            ) {
//                Row(
//                    modifier = Modifier.padding(3.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxHeight()
//                            .clip(RoundedCornerShape(8.dp))
//                            .background(if (selectedSegment == "Waiting") Color.White else Color.Transparent)
//                            .clickable { selectedSegment = "Waiting" },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            "Waiting",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 13.sp,
//                            color = if (selectedSegment == "Waiting") Color(0xFF2196F3) else Color.Gray
//                        )
//                    }
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxHeight()
//                            .clip(RoundedCornerShape(8.dp))
//                            .background(if (selectedSegment == "Consulted") Color.White else Color.Transparent)
//                            .clickable {
//                                selectedSegment = "Consulted"
//                                navController.navigate("consulting_feedback")
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            "Consulted",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 13.sp,
//                            color = if (selectedSegment == "Consulted") Color(0xFF2196F3) else Color.Gray
//                        )
//                    }
//                }
//            }
//
//            // --- 8. MORE OPTIONS ACCORDION ---
//            Column(modifier = Modifier.fillMaxWidth()) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { isMoreOptionsExpanded = !isMoreOptionsExpanded }
//                        .padding(vertical = 8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text("More options", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
//                    Icon(
//                        imageVector = if (isMoreOptionsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
//                        contentDescription = null,
//                        tint = Color.Gray
//                    )
//                }
//
//                AnimatedVisibility(visible = isMoreOptionsExpanded) {
//                    Column(
//                        modifier = Modifier.padding(top = 8.dp),
//                        verticalArrangement = Arrangement.spacedBy(12.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable { /* Handle unable to meet */ }
//                                .padding(vertical = 4.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(Icons.Outlined.EventBusy, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
//                            Spacer(modifier = Modifier.width(12.dp))
//                            Text("Unable to meet", fontSize = 14.sp, color = Color(0xFF475569), fontWeight = FontWeight.Medium)
//                        }
//
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable { navController.navigate("appointment_cancel") }
//                                .padding(vertical = 4.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(Icons.Outlined.Cancel, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
//                            Spacer(modifier = Modifier.width(12.dp))
//                            Text("Cancel Appointment", fontSize = 14.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Medium)
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(6.dp))
//
//            // --- 9. MAKE PAYMENT BUTTON ---
//            Button(
//                onClick = {
//                    if (isPaidOnline) {
//                        Toast.makeText(context, "Appointment is already paid!", Toast.LENGTH_SHORT).show()
//                    } else {
//                        showPaymentProcessDialog = true
//                    }
//                },
//                modifier = Modifier.fillMaxWidth().height(50.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (isPaidOnline) Color(0xFF16A34A) else Color(0xFF2196F3)
//                )
//            ) {
//                Icon(
//                    imageVector = if (isPaidOnline) Icons.Default.CheckCircle else Icons.Default.Payment,
//                    contentDescription = null,
//                    tint = Color.White
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = if (isPaidOnline) "Payment Completed ✓" else "Make Payment (₹500)",
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 15.sp
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//
//        // --- ONLINE PAYMENT SIMULATION DIALOG ---
//        if (showPaymentProcessDialog) {
//            AlertDialog(
//                onDismissRequest = { showPaymentProcessDialog = false },
//                shape = RoundedCornerShape(16.dp),
//                containerColor = Color.White,
//                title = { Text("Complete Online Payment", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
//                text = {
//                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
//                        Text("Doctor Consultation Fee: ₹500", fontWeight = FontWeight.Medium)
//                        Text("Select payment option (UPI / Credit Card / NetBanking)", fontSize = 12.sp, color = Color.Gray)
//
//                        Card(
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE))
//                        ) {
//                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
//                                Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color(0xFF0284C7))
//                                Spacer(modifier = Modifier.width(10.dp))
//                                Text("GPay / PhonePe / Paytm UPI", fontSize = 13.sp, fontWeight = FontWeight.Bold)
//                            }
//                        }
//                    }
//                },
//                confirmButton = {
//                    Button(
//                        onClick = {
//                            isPaidOnline = true
//                            viewModel.paymentMode = "Online"
//                            showPaymentProcessDialog = false
//                            Toast.makeText(context, "Payment Successful! ₹500 Paid.", Toast.LENGTH_LONG).show()
//                        },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
//                    ) {
//                        Text("Pay ₹500 Now")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showPaymentProcessDialog = false }) {
//                        Text("Cancel", color = Color.Gray)
//                    }
//                }
//            )
//        }
//    }
//}
//
//// --- HELPER BOTTOM NAVIGATION COMPONENT ---
//@Composable
//fun DetailsBottomNavigation(navController: NavController) {
//    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Search, null) },
//            label = { Text("Search", fontSize = 10.sp) },
//            selected = false,
//            onClick = { navController.navigate("doctor_list") }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Payments, null) },
//            label = { Text("Payments", fontSize = 10.sp) },
//            selected = false,
//            onClick = { }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.CalendarToday, null) },
//            label = { Text("My Appt", fontSize = 10.sp) },
//            selected = true,
//            onClick = { navController.navigate("my_appointments") }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Person, null) },
//            label = { Text("Profile", fontSize = 10.sp) },
//            selected = false,
//            onClick = { }
//        )
//    }
//}
//
//
//
//
