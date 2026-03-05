package com.example.schedulapatientapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientFormScreen(navController: NavController, viewModel: BookingViewModel) {
    // Memory for user inputs
    var name by remember { mutableStateOf("Meena") }
    var ageSex by remember { mutableStateOf("28, Female") }
    var weight by remember { mutableStateOf("55") }
    var complaint by remember { mutableStateOf("Stomach pain") }
    var visitType by remember { mutableStateOf("First time") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Patient details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, null) } }
            )
        },
        // UPDATED BOTTOM BAR: Using "Payments" instead of "Records"
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
            // 1. DOCTOR SUMMARY CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Circle with "L" instead of image
                    Surface(modifier = Modifier.size(50.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFE3F2FD)) {
                        Box(contentAlignment = Alignment.Center) { Text("L", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Dr. Lavangi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Gynecologist", color = Color(0xFF2196F3), fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(color = Color(0xFFE0F7FA), shape = RoundedCornerShape(4.dp)) {
                            Text("🥇 Gold Medalist", color = Color(0xFF00ACC1), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp)
                        }
                    }
                }
            }

            Text("Patient Information", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))

            // 2. PROFESSIONAL INPUT CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InputField(label = "NAME", value = name, onValueChange = { name = it })
                    InputField(label = "AGE / SEX", value = ageSex, onValueChange = { ageSex = it })

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("WEIGHT", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(20.dp)) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                BasicTextField(value = weight, onValueChange = { weight = it }, modifier = Modifier.width(20.dp))
                                Text(" kg", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }

                    InputField(label = "COMPLAINT", value = complaint, onValueChange = { complaint = it }, hasEditIcon = true)
                }
            }

            // 3. VISIT OPTIONS
            Text("Visit Options", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 24.dp, bottom = 12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Visit Type", color = Color.Gray, fontSize = 11.sp)
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(visitType, modifier = Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowDown, null)
                        }
                    }

                    // Family Appointment Dash Button
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, Color(0xFF2196F3)) // Note: Dash effect requires Canvas, keeping solid for simplicity
                    ) {
                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
                            Text(" Family appointment", color = Color(0xFF2196F3), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // 4. PAYMENT INFO NOTE
            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Pay upfront to reduce waiting time & visiting time by paying the consulting fee upfront.",
                        fontSize = 11.sp,
                        color = Color(0xFF1E3A8A),
                        lineHeight = 16.sp
                    )
                }
            }

            // 5. MAKE PAYMENT BUTTON
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("final_confirmation") },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Make payment →", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- HELPER COMPONENT FOR INPUTS ---
@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, hasEditIcon: Boolean = false) {
    Column {
        Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                modifier = Modifier.weight(1f).padding(vertical = 4.dp)
            )
            if (hasEditIcon) Icon(Icons.Default.Edit, null, tint = Color(0xFF2196F3), modifier = Modifier.size(16.dp))
        }
    }
}

// --- UPDATED BOTTOM BAR (With Payments Icon) ---
@Composable
fun UpdatedBottomBar(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search", fontSize = 10.sp) },
            selected = false,
            onClick = {}
        )
        // New Payment Tab
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, null) },
            label = { Text("Payments", fontSize = 10.sp) },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, null) },
            label = { Text("My Appt", fontSize = 10.sp) },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile", fontSize = 10.sp) },
            selected = false,
            onClick = {}
        )
    }
}