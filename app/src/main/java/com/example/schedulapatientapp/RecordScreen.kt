package com.example.schedulapatientapp


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(navController: NavController, viewModel: BookingViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("My Records", fontWeight = FontWeight.Bold) })
        },
        bottomBar = { BookingBottomBar(navController,"records ") } // Wahi bottom bar yahan bhi dikhega
    ) { padding ->
        // Agar koi booking nahi hai toh message dikhayenge
        if (viewModel.patientName.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No Records Found", color = Color.Gray)
            }
        } else {
            // Agar data hai toh list dikhayenge
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Token: ${viewModel.tokenNumber}", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
                            Text("Patient: ${viewModel.patientName}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("Date: ${viewModel.selectedDate} | Time: ${viewModel.selectedTime}", color = Color.Gray)
                            Text("Type: ${viewModel.consultType}", color = Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}