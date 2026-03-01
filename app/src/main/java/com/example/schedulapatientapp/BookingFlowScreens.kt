package com.example.schedulapatientapp



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// --- Shared Professional Components ---
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

// --- PAGE 4: DATE SELECTION ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingStepOne(navController: NavController, viewModel: BookingViewModel) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Booking Screen 1", fontWeight = FontWeight.Bold) }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Choose Date", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(modifier = Modifier.padding(vertical = 12.dp)) {
                listOf("1st Oct", "2nd Oct", "3rd Oct").forEach { date ->
                    val isSelected = viewModel.selectedDate == date
                    Surface(
                        modifier = Modifier.padding(end = 8.dp).clickable { viewModel.selectedDate = date },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) Color(0xFF2196F3) else Color(0xFFF1F5F9),
                        contentColor = if (isSelected) Color.White else Color.Black
                    ) {
                        Text(date, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Consulting Type", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = viewModel.consultType == "Regular", onClick = { viewModel.consultType = "Regular" })
                Text("Regular")
                Spacer(modifier = Modifier.width(24.dp))
                RadioButton(selected = viewModel.consultType == "Online", onClick = { viewModel.consultType = "Online" })
                Text("Online")
            }

            Spacer(modifier = Modifier.weight(1f))
            AppButton("Book Appointment") { navController.navigate("booking_step2") }
        }
    }
}

// --- PAGE 5: TIME SLOT ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSlotScreen(navController: NavController, viewModel: BookingViewModel) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Select Time", fontWeight = FontWeight.Bold) }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
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

// --- PAGE 10: PATIENT FORM ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(navController: NavController, viewModel: BookingViewModel) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Patient Details", fontWeight = FontWeight.Bold) }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            OutlinedTextField(value = viewModel.patientName, onValueChange = { viewModel.patientName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = viewModel.patientAge, onValueChange = { viewModel.patientAge = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = viewModel.patientComplaint, onValueChange = { viewModel.patientComplaint = it }, label = { Text("Complaint") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), minLines = 3)

            Spacer(modifier = Modifier.weight(1f))
            AppButton("Confirm Appointment") {  navController.navigate("confirmation") }
        }
    }
}

// --- PAGE 7: SUCCESS SCREEN ---
@Composable
fun ConfirmationScreen(navController: NavController, viewModel: BookingViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Appointment Confirmed!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(20.dp))
        Text("Your Token Number", color = Color.Gray)
        Text(viewModel.tokenNumber, fontSize = 60.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2196F3))

        Spacer(modifier = Modifier.height(32.dp))
        AppButton("Back to Search") {
            navController.navigate("doctor_list") { popUpTo("doctor_list") { inclusive = true } }
        }
    }
}