package com.example.schedulapatientapp


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchDoctorScreen() {
    // 1. Sample Data
    val doctors = listOf(
        Doctor("Dr. Kumar", "Cardiologist", ),
        Doctor("Dr. Lavangi", "Neurologist", ),
        Doctor("Dr. Elango", "Orthopedic", )
    )

    // 2. Main Layout (Scrollable)
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {

        Text("Search Doctor", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by name or specialty...") },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Top Specialists Header
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Top Specialists", fontWeight = FontWeight.Bold)
            Text("See All", color = Color.Blue)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid of Doctors (3 columns)
        // For simplicity as a beginner, we use a Row with Columns
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            doctors.forEach { doctor ->
                DoctorItem(doctor)
            }
        }

        // Add the Blue Banner at the bottom
        Spacer(modifier = Modifier.height(24.dp))
        FullBodyCheckupBanner()
    }
}

@Composable
fun DoctorItem(doctor: Doctor) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.size(100.dp)
        ) {
          //  Image(
            //    painter = painterResource(id = doctor.imageId),
            //    contentDescription = null,
            //    contentScale = ContentScale.Crop
            //)
        }
        Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(doctor.specialty, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun FullBodyCheckupBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF42A5F5)), // Blue color
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Full Body Checkup", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Includes 50+ tests. Get 20% off!", color = Color.White)
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                Text("Book Now", color = Color.Blue)
            }
        }
    }
}