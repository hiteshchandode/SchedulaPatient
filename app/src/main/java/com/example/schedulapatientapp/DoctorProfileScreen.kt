package com.example.schedulapatientapp



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class) // Add this at the top of the function
@Composable
fun DoctorProfileScreen(navController: NavController) {
    Scaffold(
        // 1. ADD THIS TOP BAR SECTION
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Doctor Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {

            Surface(
                modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
                color = Color.White,
                shadowElevation = (8.dp)
            ){}

            Button(
                onClick = {navController.navigate("booking_step1") },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Book Appointment", fontSize = 18.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // (Note: I removed the old "Top Header" Text from here)

            Spacer(modifier = Modifier.height(8.dp))

            // --- DOCTOR INFO CARD ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(80.dp).background(Color(0xFFCBD5E1), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("DL", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text("Dr. Kumar", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("cardiologist", color = Color.Gray)
                        Text("12 yrs Experience", color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                    }
                }
            }



            Spacer(modifier = Modifier.height(16.dp))

            // --- STATS ROW (3 Boxes) ---
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatItem(label = "Patients", value = "1.5k+")
                StatItem(label = "Rating", value = "4.9")
                StatItem(label = "Reviews", value = "250+")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- SERVICES SECTION ---
            Text("Services", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            ServiceItem("Consultation & Diagnosis")
            ServiceItem("Diagnostic Tests")
            ServiceItem("Disease Management")

            Spacer(modifier = Modifier.height(24.dp))

            // --- ABOUT SECTION ---
            Text("About Doctor", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Cardiology Department delivers expert heart care with 12 years’ experience in diagnosis, treatment, prevention.",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Surface(
        modifier = Modifier.width(100.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ServiceItem(name: String) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name)
            Text(">", color = Color.Gray) // Small arrow
        }
    }
}