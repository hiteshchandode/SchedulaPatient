package com.example.schedulapatientapp

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. THE BLUEPRINTS
data class Doctor(val name: String, val specialty: String, val color: Color)

// 2. THE DATA (9 Doctors)
val doctors = listOf(
    Doctor("Dr. Kumar", "Cardiologist", Color(0xFFF44336)),
    Doctor("Dr. Lavangi", "Neurologist", Color(0xFF9C27B0)),
    Doctor("Dr. Elango", "Orthopedic", Color(0xFF3F51B5)),
    Doctor("Dr. Sarah", "Pediatrician", Color(0xFF00BCD4)),
    Doctor("Dr. James", "General", Color(0xFF4CAF50)),
    Doctor("Dr. Emily", "Dermatologist", Color(0xFFFFEB3B)),
    Doctor("Dr. Robert", "Surgeon", Color(0xFFFF9800)),
    Doctor("Dr. Linda", "Psychiatrist", Color(0xFF795548)),
    Doctor("Dr. Michael", "Dentist", Color(0xFF607D8B))
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDoctorScreen() {
    // 1. Scaffold handles the "Top Bar" area so content doesn't hit the clock
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Search Doctor", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back click */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle search click */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { innerPadding ->
        // 2. Use innerPadding to push content down below the header
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Your Search Bar
            OutlinedTextField(
                value = "", onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by name...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Top Specialists", fontWeight = FontWeight.Bold)

            // Your Grid of 9 Doctors
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(400.dp),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(doctors) { doctor ->
                    DoctorItem(doctor)
                }
            }

            // Clinic Section
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Nearby Clinics", fontWeight = FontWeight.Bold)
                Text("Map View", color = Color.Blue)
            }

            ClinicItem("City Care Clinic", "2.4km • Open now", Icons.Default.LocationOn)
        }
    }
}

// 4. THE SMALL COMPONENTS (Doctor circles and Clinic cards)
@Composable
fun DoctorItem(doctor: Doctor) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier.size(80.dp).background(doctor.color.copy(alpha = 0.2f), CircleShape).border(2.dp, doctor.color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = doctor.name.replace("Dr. ", "").take(1).uppercase(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = doctor.color)
        }
        Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
        Text(doctor.specialty, color = Color.Gray, fontSize = 10.sp)
    }
}

@Composable
fun ClinicItem(name: String, info: String, icon: ImageVector) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.Blue, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(name, fontWeight = FontWeight.Bold)
                Text(info, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}





