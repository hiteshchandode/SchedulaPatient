package com.example.schedulapatientapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
    // 1. STATE: User Input Memory
    var name by remember { mutableStateOf("") }
    var ageSex by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var complaint by remember { mutableStateOf("") }

    // 2. DROP-DOWN STATE
    var expanded by remember { mutableStateOf(false) }
    var selectedVisitType by remember { mutableStateOf("First time") }
    val options = listOf("First time", "Report", "Follow-up")

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
            // DOCTOR SUMMARY CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(modifier = Modifier.size(50.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFFE3F2FD)) {
                        Box(contentAlignment = Alignment.Center) { Text("L", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Dr. Kumar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Cardiologist", color = Color(0xFF2196F3), fontSize = 12.sp)
                    }
                }
            }

            Text("Patient Information", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))

            // INPUT SECTION
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MyCustomInputField(label = "NAME", value = name, placeholder = "Enter name", onValueChange = { name = it })
                    MyCustomInputField(label = "AGE / SEX", value = ageSex, placeholder = "e.g. 28, Female", onValueChange = { ageSex = it })

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("WEIGHT", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(20.dp)) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                                BasicTextField(value = weight, onValueChange = { weight = it }, modifier = Modifier.width(40.dp))
                                Text(" kg", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }

                    MyCustomInputField(label = "COMPLAINT", value = complaint, placeholder = "Describe your pain", onValueChange = { complaint = it }, hasEditIcon = true)
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
                                DropdownMenuItem(text = { Text(selection) }, onClick = { selectedVisitType = selection; expanded = false })
                            }
                        }
                    }
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFF2196F3)),
                        color = Color.Transparent
                    ) {
                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
                            Text(" Family appointment", color = Color(0xFF2196F3))
                        }
                    }
                }
            }

            // PAYMENT NOTE
            Spacer(modifier = Modifier.height(20.dp))
            Surface(modifier = Modifier.fillMaxWidth(), color = Color(0xFFE3F2FD), shape = RoundedCornerShape(8.dp)) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Icon(Icons.Default.Info, null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pay upfront to reduce waiting time by paying consulting fee.", fontSize = 11.sp, color = Color(0xFF1E3A8A))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("final_confirmation") },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Make payment →", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// THE HELPER COMPONENT (RENAMED TO AVOID IMPORT CONFLICTS)
@Composable
fun MyCustomInputField(label: String, value: String, placeholder: String, onValueChange: (String) -> Unit, hasEditIcon: Boolean = false) {
    Column {
        Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Box(contentAlignment = Alignment.CenterStart) {
            if (value.isEmpty()) Text(placeholder, color = Color.LightGray, fontSize = 16.sp)
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
}

@Composable
fun UpdatedBottomBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        // 1. SEARCH TAB
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search", fontSize = 10.sp) },
            selected = false,
            onClick = { /* navigate to search */ }
        )

        // 2. PAYMENTS TAB (The one we just added!)
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Payments") },
            label = { Text("Payments", fontSize = 10.sp) },
            selected = false,
            onClick = { /* navigate to payments */ }
        )

        // 3. MY APPOINTMENT TAB (Selected by default here)
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "My Appt") },
            label = { Text("My Appt", fontSize = 10.sp) },
            selected = true,
            onClick = { /* navigate to appointments */ }
        )

        // 4. PROFILE TAB
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 10.sp) },
            selected = false,
            onClick = { /* navigate to profile */ }
        )
    }
}