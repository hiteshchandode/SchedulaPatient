package com.example.schedulapatientapp


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appointment details", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    // This is the Back Arrow you requested
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { DetailsBottomNavigation(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF8FAFC))
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- 1. DOCTOR INFO CARD (With "L" Avatar) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Alphabet Avatar
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE3F2FD)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("L", fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color(0xFF2196F3))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Dr. Lavangi", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Gynaecologist\n15 yrs. exp\nGold Medalist", fontSize = 14.sp, color = Color.DarkGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 2. APPOINTMENT TIME & TOKEN ---
            Text(
                text = "Consulting time: Oct 7, 8 PM  Token: #47",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. PATIENT DETAILS & ACTION BUTTONS ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                // Left Side: Patient Info Column
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("name : Meena", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text("age : 28", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text("Sex : Female", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text("Weight : 55", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text("Complaint : Stomach pain", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right Side: Action Buttons Column
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedActionBtn("Reschedule")

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(modifier = Modifier.weight(1f)) { OutlinedActionBtn("Waiting") }
                        Box(modifier = Modifier.weight(1f)) { OutlinedActionBtn("Consulted") }
                    }

                    OutlinedActionBtn("Unable to meet")
                    OutlinedActionBtn("Cancel")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 4. LIVE TRACKING SECTION ---
            Text("Live tracking", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "15 patients consulting. Expected\nconsulting time 8:20 PM.",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- 5. PAYMENT SECTION ---
            Text(
                "Reduce your waiting time & visiting time by\npaying the consulting fee upfront",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { /* Handle Payment */ },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF2196F3))
            ) {
                Text("Make payment", color = Color(0xFF2196F3))
            }

            Spacer(modifier = Modifier.height(80.dp)) // Space so bottom bar doesn't cover content
        }
    }
}

// A helper function to keep the action buttons looking clean in the code
@Composable
fun OutlinedActionBtn(text: String) {
    OutlinedButton(
        onClick = { },
        modifier = Modifier.fillMaxWidth().height(36.dp),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Text(text, fontSize = 11.sp, color = Color.Black)
    }
}

// BOTTOM BAR (Matches your existing app logic)
@Composable
fun DetailsBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("doctor_list") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, null) },
            label = { Text("Records", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("records") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, null) },
            label = { Text("My Appt", fontSize = 10.sp) },
            selected = true, // Highlighted because we are in the appointments flow
            onClick = { navController.navigate("my_appointments") },
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