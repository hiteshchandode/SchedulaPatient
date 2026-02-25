package com.example.schedulapatientapp

import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.navigation.NavHostController
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp




fun getGreetingText(): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Good Morning ☀️"
        in 12..16 -> "Good Afternoon 🌤️"
        in 17..20 -> "Good Evening 🌆"
        else -> "Good Night 🌙"
    }
}



// 1. THE BLUEPRINTS
data class Doctor(val name: String, val specialty: String, val color: Color)



// 2. THE DATA (9 Doctors)
val doctors = listOf(
    Doctor("Dr. Kumar", "Cardiologist", Color(0xFFF44336)),
    Doctor("Dr. Devi shetty", "Neurologist", Color(0xFF9C27B0)),
    Doctor("Dr. Sudhir Gupta", "Orthopedic", Color(0xFF3F51B5)),
    Doctor("Dr. Sarah", "Pediatrician", Color(0xFF00BCD4)),
    Doctor("Dr. Arvind Kumar", "General", Color(0xFF4CAF50)),
    Doctor("Dr. Nitin Sood", "Dermatologist", Color(0xFFFFEB3B)),
    Doctor("Dr. Atul Goel", "Surgeon", Color(0xFFFF9800)),
    Doctor("Dr. S.K. Gupta", "Psychiatrist", Color(0xFF795548)),
    Doctor("Dr. Prathap Reddy", "Dentist", Color(0xFF607D8B))
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navcontroller: NavHostController) {
    // 1. The Scaffold acts as the container for Top, Bottom, and Body
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Search Doctor", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navcontroller.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        bottomBar = {
            // 2. This is the Bottom Bar with your 4 tools
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, null) },
                    label = { Text("Find Doctor") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, null) },
                    label = { Text("Records") },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, null) },
                    label = { Text("Appointment") },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { }
                )
            }
        }
    )



    { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // 2.  Greeting (Climate)
            Text(
                text = getGreetingText(),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )

            // 3.  Search Bar
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by name...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4. The "Top Specialists" Title
            Text(
                text = "Top Specialists",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 5.  doctor grid code

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.height(400.dp),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(doctors) { doctor ->
                DoctorItem(doctor)
            }
        }

            Spacer(modifier = Modifier.height(20.dp))

//            // 6. Nearby Clinics Title
//            Text(
//                text = "Nearby Clinics",
//                fontWeight = FontWeight.Bold,
//                fontSize = 18.sp
//            )
//
//           // Clinic Section
//                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                        Text("Nearby Clinics", fontWeight = FontWeight.Bold)
//                        Text("Map View", color = Color.Blue)
//                    }
//
//            ClinicItem("City Care Clinic", "2.4km • Open now", Icons.Default.LocationOn)
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





