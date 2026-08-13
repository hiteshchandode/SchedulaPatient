package com.example.schedulapatientapp

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientFormScreen(
    navController: NavController,
    viewModel: BookingViewModel,
    mainViewModel: MainViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    val context = LocalContext.current

    // Doctor details from ViewModel
    val doctorName = viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" }
    val doctorSpecialty = viewModel.selectedDoctorSpecialty.ifEmpty { "General" }
    val displayDate = viewModel.selectedDate.ifEmpty { "5 March 2026" }
    val displayTime = viewModel.selectedTime.ifEmpty { "10:00 AM" }
    val tokenNumber = viewModel.tokenNumber.ifEmpty { "#14" }
    val initialLetter = doctorName.replace("Dr. ", "").take(1).uppercase()

    // 1. STATE: Bound to ViewModel State
    var name by remember { mutableStateOf(viewModel.patientName) }
    var ageSex by remember { mutableStateOf(viewModel.patientAge) }
    var weight by remember { mutableStateOf("") }
    var complaint by remember { mutableStateOf(viewModel.patientComplaint) }

    // 2. DROP-DOWN STATE
    var expanded by remember { mutableStateOf(false) }
    var selectedVisitType by remember { mutableStateOf("First time") }
    val options = listOf("First time", "Report", "Follow-up")

    // 3. PAYMENT DIALOG STATE
    var showPaymentDialog by remember { mutableStateOf(false) }

    // Helper to save appointment to Room DB and proceed
    fun confirmAndSaveBooking(paymentMode: String, paymentStatus: String) {
        viewModel.paymentMode = paymentMode

        mainViewModel.saveFullAppointment(
            doctorName = doctorName,
            doctorSpecialty = doctorSpecialty,
            patientName = name,
            patientAge = ageSex,
            patientComplaint = complaint,
            date = displayDate,
            timeSlot = displayTime,
            tokenNumber = tokenNumber,
            paymentMode = paymentMode,
            paymentStatus = paymentStatus
        )

        showPaymentDialog = false
        Toast.makeText(context, "Appointment Booked Successfully!", Toast.LENGTH_SHORT).show()
        navController.navigate("appointment_details")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Patient details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { UpdatedBottomBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // DOCTOR SUMMARY CARD (DYNAMIC DOCTOR DETAILS)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE3F2FD)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(initialLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(doctorSpecialty, color = Color(0xFF2196F3), fontSize = 12.sp)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                }
            }

            Text("Patient Information", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))

            // INPUT SECTION WITH CIRCULAR AVATAR LOGOS
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MyCustomInputField(
                        icon = Icons.Default.Person,
                        label = "NAME",
                        value = name,
                        placeholder = "Enter name",
                        onValueChange = {
                            name = it
                            viewModel.patientName = it
                        }
                    )

                    Divider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                    MyCustomInputField(
                        icon = Icons.Default.CalendarToday,
                        label = "AGE / SEX",
                        value = ageSex,
                        placeholder = "e.g. 24, Male",
                        onValueChange = {
                            ageSex = it
                            viewModel.patientAge = it
                        }
                    )

                    Divider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                    // WEIGHT WITH CIRCULAR AVATAR
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color(0xFFE3F2FD)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = Color(0xFF2196F3),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("WEIGHT", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(20.dp)) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    BasicTextField(
                                        value = weight,
                                        onValueChange = { weight = it },
                                        textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                                        modifier = Modifier.width(36.dp)
                                    )
                                    Text("kg", color = Color.Gray, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    Divider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                    MyCustomInputField(
                        icon = Icons.Default.EventNote,
                        label = "COMPLAINT",
                        value = complaint,
                        placeholder = "Describe your symptom",
                        onValueChange = {
                            complaint = it
                            viewModel.patientComplaint = it
                        },
                        hasEditIcon = true
                    )
                }
            }

            // VISIT OPTIONS
            Text("Visit Options", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 24.dp, bottom = 12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Visit Type", color = Color.Gray, fontSize = 11.sp)
                    Box {
                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { expanded = true },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(selectedVisitType, modifier = Modifier.weight(1f))
                                Icon(Icons.Default.KeyboardArrowDown, null)
                            }
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            options.forEach { selection ->
                                DropdownMenuItem(
                                    text = { Text(selection) },
                                    onClick = {
                                        selectedVisitType = selection
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable { /* Handle family appointment */ },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFF2196F3)),
                        color = Color.Transparent
                    ) {
                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PersonAdd, null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Family appointment", color = Color(0xFF2196F3), fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // PAYMENT NOTE
            Spacer(modifier = Modifier.height(20.dp))
            Surface(modifier = Modifier.fillMaxWidth(), color = Color(0xFFE3F2FD), shape = RoundedCornerShape(8.dp)) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pay upfront to reduce waiting time by paying consulting fee.", fontSize = 11.sp, color = Color(0xFF1E3A8A))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SINGLE PROCEED TO BOOK BUTTON
            Button(
                onClick = {
                    when {
                        name.isBlank() -> {
                            Toast.makeText(context, "Please enter patient name!", Toast.LENGTH_SHORT).show()
                        }
                        ageSex.isBlank() -> {
                            Toast.makeText(context, "Please enter age and sex!", Toast.LENGTH_SHORT).show()
                        }
                        complaint.isBlank() -> {
                            Toast.makeText(context, "Please enter patient complaint!", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            showPaymentDialog = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Proceed to Book →", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        // --- PAYMENT SELECTION DIALOG ---
        if (showPaymentDialog) {
            AlertDialog(
                onDismissRequest = { showPaymentDialog = false },
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White,
                title = {
                    Text(
                        text = "Choose Payment Method",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "How would you like to pay for your appointment?",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        // Option 1: Pay Online
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { confirmAndSaveBooking(paymentMode = "Online", paymentStatus = "Paid") },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color(0xFF2196F3))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Pay Online Now", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                                    Text("Fastest • Priority token tracking", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        }

                        // Option 2: Pay at Reception
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { confirmAndSaveBooking(paymentMode = "Reception", paymentStatus = "Pending") },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Storefront, contentDescription = null, tint = Color(0xFF2196F3))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Pay at Hospital Reception", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                                    Text("Pay cash/UPI at counter before visit", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showPaymentDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}

// HELPER INPUT FIELD COMPONENT WITH CIRCULAR ICON BADGE
@Composable
fun MyCustomInputField(
    icon: ImageVector,
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    hasEditIcon: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // CIRCULAR AVATAR ICON
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = Color(0xFFE3F2FD)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // INPUT FIELD TEXT
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) Text(placeholder, color = Color.LightGray, fontSize = 15.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                        modifier = Modifier.weight(1f).padding(vertical = 2.dp)
                    )
                    if (hasEditIcon) {
                        Icon(Icons.Default.Edit, null, tint = Color(0xFF2196F3), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

// BOTTOM BAR COMPONENT
@Composable
fun UpdatedBottomBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
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
            onClick = { navController.navigate("my_appointments") }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 10.sp) },
            selected = false,
            onClick = { /* navigate to profile */ }
        )
    }
}
















































//package com.example.schedulapatientapp
//
//import android.widget.Toast
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PatientFormScreen(navController: NavController, viewModel: BookingViewModel) {
//    val context = LocalContext.current
//
//    // Doctor details from ViewModel
//    val doctorName = viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" }
//    val doctorSpecialty = viewModel.selectedDoctorSpecialty.ifEmpty { "General" }
//    val initialLetter = doctorName.replace("Dr. ", "").take(1).uppercase()
//
//    // 1. STATE: Bound to ViewModel State
//    var name by remember { mutableStateOf(viewModel.patientName) }
//    var ageSex by remember { mutableStateOf(viewModel.patientAge) }
//    var weight by remember { mutableStateOf("") }
//    var complaint by remember { mutableStateOf(viewModel.patientComplaint) }
//
//    // 2. DROP-DOWN STATE
//    var expanded by remember { mutableStateOf(false) }
//    var selectedVisitType by remember { mutableStateOf("First time") }
//    val options = listOf("First time", "Report", "Follow-up")
//
//    // 3. PAYMENT DIALOG STATE
//    var showPaymentDialog by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Patient details", fontWeight = FontWeight.Bold) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        },
//        bottomBar = { UpdatedBottomBar(navController) }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .background(Color(0xFFF8FAFC))
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp)
//        ) {
//            // DOCTOR SUMMARY CARD (DYNAMIC DOCTOR DETAILS)
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Row(
//                    modifier = Modifier.padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Surface(
//                        modifier = Modifier.size(50.dp),
//                        shape = RoundedCornerShape(12.dp),
//                        color = Color(0xFFE3F2FD)
//                    ) {
//                        Box(contentAlignment = Alignment.Center) {
//                            Text(initialLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
//                        }
//                    }
//                    Spacer(modifier = Modifier.width(12.dp))
//                    Column(modifier = Modifier.weight(1f)) {
//                        Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                        Text(doctorSpecialty, color = Color(0xFF2196F3), fontSize = 12.sp)
//                    }
//                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
//                }
//            }
//
//            Text("Patient Information", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))
//
//            // INPUT SECTION WITH CIRCULAR AVATAR LOGOS
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    MyCustomInputField(
//                        icon = Icons.Default.Person,
//                        label = "NAME",
//                        value = name,
//                        placeholder = "Enter name",
//                        onValueChange = {
//                            name = it
//                            viewModel.patientName = it
//                        }
//                    )
//
//                    Divider(color = Color(0xFFF1F5F9), thickness = 1.dp)
//
//                    MyCustomInputField(
//                        icon = Icons.Default.CalendarToday,
//                        label = "AGE / SEX",
//                        value = ageSex,
//                        placeholder = "e.g. 24, Male",
//                        onValueChange = {
//                            ageSex = it
//                            viewModel.patientAge = it
//                        }
//                    )
//
//                    Divider(color = Color(0xFFF1F5F9), thickness = 1.dp)
//
//                    // WEIGHT WITH CIRCULAR AVATAR
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Surface(
//                            modifier = Modifier.size(40.dp),
//                            shape = CircleShape,
//                            color = Color(0xFFE3F2FD)
//                        ) {
//                            Box(contentAlignment = Alignment.Center) {
//                                Icon(
//                                    imageVector = Icons.Default.ShoppingBag,
//                                    contentDescription = null,
//                                    tint = Color(0xFF2196F3),
//                                    modifier = Modifier.size(20.dp)
//                                )
//                            }
//                        }
//                        Spacer(modifier = Modifier.width(12.dp))
//                        Column {
//                            Text("WEIGHT", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
//                            Spacer(modifier = Modifier.height(2.dp))
//                            Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(20.dp)) {
//                                Row(
//                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    BasicTextField(
//                                        value = weight,
//                                        onValueChange = { weight = it },
//                                        textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
//                                        modifier = Modifier.width(36.dp)
//                                    )
//                                    Text("kg", color = Color.Gray, fontSize = 12.sp)
//                                }
//                            }
//                        }
//                    }
//
//                    Divider(color = Color(0xFFF1F5F9), thickness = 1.dp)
//
//                    MyCustomInputField(
//                        icon = Icons.Default.EventNote,
//                        label = "COMPLAINT",
//                        value = complaint,
//                        placeholder = "Describe your symptom",
//                        onValueChange = {
//                            complaint = it
//                            viewModel.patientComplaint = it
//                        },
//                        hasEditIcon = true
//                    )
//                }
//            }
//
//            // VISIT OPTIONS
//            Text("Visit Options", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 24.dp, bottom = 12.dp))
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text("Visit Type", color = Color.Gray, fontSize = 11.sp)
//                    Box {
//                        OutlinedCard(
//                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { expanded = true },
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
//                                Text(selectedVisitType, modifier = Modifier.weight(1f))
//                                Icon(Icons.Default.KeyboardArrowDown, null)
//                            }
//                        }
//                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                            options.forEach { selection ->
//                                DropdownMenuItem(
//                                    text = { Text(selection) },
//                                    onClick = {
//                                        selectedVisitType = selection
//                                        expanded = false
//                                    }
//                                )
//                            }
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(48.dp)
//                            .clickable { /* Handle family appointment */ },
//                        shape = RoundedCornerShape(12.dp),
//                        border = BorderStroke(1.dp, Color(0xFF2196F3)),
//                        color = Color.Transparent
//                    ) {
//                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
//                            Icon(Icons.Default.PersonAdd, null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
//                            Spacer(modifier = Modifier.width(6.dp))
//                            Text("Family appointment", color = Color(0xFF2196F3), fontWeight = FontWeight.SemiBold)
//                        }
//                    }
//                }
//            }
//
//            // PAYMENT NOTE
//            Spacer(modifier = Modifier.height(20.dp))
//            Surface(modifier = Modifier.fillMaxWidth(), color = Color(0xFFE3F2FD), shape = RoundedCornerShape(8.dp)) {
//                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
//                    Icon(Icons.Default.Info, null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Pay upfront to reduce waiting time by paying consulting fee.", fontSize = 11.sp, color = Color(0xFF1E3A8A))
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // SINGLE PROCEED TO BOOK BUTTON
//            Button(
//                onClick = {
//                    when {
//                        name.isBlank() -> {
//                            Toast.makeText(context, "Please enter patient name!", Toast.LENGTH_SHORT).show()
//                        }
//                        ageSex.isBlank() -> {
//                            Toast.makeText(context, "Please enter age and sex!", Toast.LENGTH_SHORT).show()
//                        }
//                        complaint.isBlank() -> {
//                            Toast.makeText(context, "Please enter patient complaint!", Toast.LENGTH_SHORT).show()
//                        }
//                        else -> {
//                            showPaymentDialog = true
//                        }
//                    }
//                },
//                modifier = Modifier.fillMaxWidth().height(52.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
//            ) {
//                Text("Proceed to Book →", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//            }
//        }
//
//        // --- PAYMENT SELECTION DIALOG ---
//        if (showPaymentDialog) {
//            AlertDialog(
//                onDismissRequest = { showPaymentDialog = false },
//                shape = RoundedCornerShape(20.dp),
//                containerColor = Color.White,
//                title = {
//                    Text(
//                        text = "Choose Payment Method",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 18.sp
//                    )
//                },
//                text = {
//                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                        Text(
//                            text = "How would you like to pay for your appointment?",
//                            fontSize = 13.sp,
//                            color = Color.Gray
//                        )
//
//                        // Option 1: Pay Online
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    viewModel.paymentMode = "Online"
//                                    showPaymentDialog = false
//                                    navController.navigate("appointment_details")
//                                },
//                            shape = RoundedCornerShape(12.dp),
//                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
//                        ) {
//                            Row(
//                                modifier = Modifier.padding(14.dp),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color(0xFF2196F3))
//                                Spacer(modifier = Modifier.width(12.dp))
//                                Column {
//                                    Text("Pay Online Now", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E3A8A))
//                                    Text("Fastest • Priority token tracking", fontSize = 11.sp, color = Color(0xFF2196F3))
//                                }
//                            }
//                        }
//
//                        // Option 2: Pay at Reception
//                        OutlinedCard(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    viewModel.paymentMode = "Reception"
//                                    showPaymentDialog = false
//                                    navController.navigate("appointment_details")
//                                },
//                            shape = RoundedCornerShape(12.dp),
//                            border = BorderStroke(1.dp, Color(0xFF2196F3))
//                        ) {
//                            Row(
//                                modifier = Modifier.padding(14.dp),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Icon(Icons.Default.Storefront, contentDescription = null, tint = Color(0xFF2196F3))
//                                Spacer(modifier = Modifier.width(12.dp))
//                                Column {
//                                    Text("Pay at Hospital Reception", fontWeight = FontWeight.Bold, fontSize = 14.sp)
//                                    Text("Pay cash/UPI at counter before visit", fontSize = 11.sp, color = Color.Gray)
//                                }
//                            }
//                        }
//                    }
//                },
//                confirmButton = {},
//                dismissButton = {
//                    TextButton(onClick = { showPaymentDialog = false }) {
//                        Text("Cancel", color = Color.Gray)
//                    }
//                }
//            )
//        }
//    }
//}
//
//// HELPER INPUT FIELD COMPONENT WITH CIRCULAR ICON BADGE
//@Composable
//fun MyCustomInputField(
//    icon: ImageVector,
//    label: String,
//    value: String,
//    placeholder: String,
//    onValueChange: (String) -> Unit,
//    hasEditIcon: Boolean = false
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // CIRCULAR AVATAR ICON
//        Surface(
//            modifier = Modifier.size(40.dp),
//            shape = CircleShape,
//            color = Color(0xFFE3F2FD)
//        ) {
//            Box(contentAlignment = Alignment.Center) {
//                Icon(
//                    imageVector = icon,
//                    contentDescription = null,
//                    tint = Color(0xFF2196F3),
//                    modifier = Modifier.size(20.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.width(12.dp))
//
//        // INPUT FIELD TEXT
//        Column(modifier = Modifier.weight(1f)) {
//            Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
//            Box(contentAlignment = Alignment.CenterStart) {
//                if (value.isEmpty()) Text(placeholder, color = Color.LightGray, fontSize = 15.sp)
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    BasicTextField(
//                        value = value,
//                        onValueChange = onValueChange,
//                        textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
//                        modifier = Modifier.weight(1f).padding(vertical = 2.dp)
//                    )
//                    if (hasEditIcon) {
//                        Icon(Icons.Default.Edit, null, tint = Color(0xFF2196F3), modifier = Modifier.size(16.dp))
//                    }
//                }
//            }
//        }
//    }
//}
//
//// BOTTOM BAR COMPONENT
//@Composable
//fun UpdatedBottomBar(navController: NavController) {
//    NavigationBar(
//        containerColor = Color.White,
//        tonalElevation = 8.dp
//    ) {
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
//            label = { Text("Search", fontSize = 10.sp) },
//            selected = false,
//            onClick = { navController.navigate("doctor_list") }
//        )
//
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Payments") },
//            label = { Text("Payments", fontSize = 10.sp) },
//            selected = false,
//            onClick = { /* navigate to payments */ }
//        )
//
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.DateRange, contentDescription = "My Appt") },
//            label = { Text("My Appt", fontSize = 10.sp) },
//            selected = true,
//            onClick = { navController.navigate("my_appointments") }
//        )
//
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
//            label = { Text("Profile", fontSize = 10.sp) },
//            selected = false,
//            onClick = { /* navigate to profile */ }
//        )
//    }
//}
//
//
//
