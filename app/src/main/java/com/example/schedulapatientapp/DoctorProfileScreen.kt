package com.example.schedulapatientapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(
    navController: NavController,
    bookingViewModel: BookingViewModel = viewModel()
) {
    val doctorName = bookingViewModel.selectedDoctorName
    val doctorSpecialty = bookingViewModel.selectedDoctorSpecialty
    val initialLetter = doctorName.replace("Dr. ", "").take(1).uppercase()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Doctor Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },

        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    // 1. "Book Appointment" Button on TOP
                    Button(
                        onClick = { navController.navigate("booking_step1") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Text(
                            text = "Book Appointment",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 2. Navigation Bottom Bar at the VERY BOTTOM
                    BookingBottomBar(navController, "doctor_list")
                }
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
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color(0xFF2196F3).copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(initialLetter, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
                    }

                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(doctorName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(doctorSpecialty, color = Color.Gray)
                        Text("12 yrs Experience", color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatItem(label = "Patients", value = "1.5k+")
                StatItem(label = "Rating", value = "4.9")
                StatItem(label = "Reviews", value = "250+")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Services", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Fetch specialty-specific services
            val servicesList = getServicesForSpecialty(doctorSpecialty)

            servicesList.forEach { serviceName ->
                ServiceItem(serviceName)
            }


//            ServiceItem("Consultation & Diagnosis")
//            ServiceItem("Diagnostic Tests")
//            ServiceItem("Disease Management")

            Spacer(modifier = Modifier.height(24.dp))

            Text("About Doctor", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = "$doctorSpecialty department delivers expert care with years of experience in diagnosis, treatment, and prevention.",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
// --- HELPER COMPONENTS ---

@Composable
fun StatItem(label: String, value: String) {
    Surface(
        modifier = Modifier.width(100.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ServiceItem(name: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name)
            Text(">", color = Color.Gray)
        }
    }
}


// Helper function covering all 9 doctor specialties dynamically
fun getServicesForSpecialty(specialty: String): List<String> {
    return when (specialty.lowercase().trim()) {
        "cardiologist", "cardiology" -> listOf(
            "ECG & Heart Monitoring",
            "Cardiovascular Checkup",
            "Hypertension & BP Control"
        )
        "surgeon", "surgery" -> listOf(
            "Pre-Surgical Consultation",
            "Post-Op Recovery & Care",
            "Minor Surgical Procedures"
        )
        "dermatologist", "dermatology" -> listOf(
            "Skin & Acne Treatment",
            "Hair & Scalp Care",
            "Cosmetic Consultation"
        )
        "pediatrician", "pediatrics" -> listOf(
            "Child Vaccination & Care",
            "Growth Monitoring",
            "Pediatric Consultation"
        )
        "orthopedic", "orthopedics" -> listOf(
            "Joint & Bone Care",
            "Fracture Management",
            "Physiotherapy Advice"
        )
        "neurologist", "neurology" -> listOf(
            "Nerve & Brain Consultation",
            "Migraine & Headache Care",
            "Neurological Evaluation"
        )
        "ent", "ear nose throat" -> listOf(
            "Hearing & Ear Checkup",
            "Sinus & Nasal Treatment",
            "Throat Infection Care"
        )
        "gynecologist", "gynecology" -> listOf(
            "Maternal & Prenatal Care",
            "Routine Gynec Checkup",
            "Hormonal Wellness"
        )
        "ophthalmologist", "eye specialist" -> listOf(
            "Vision & Eye Testing",
            "Cataract Evaluation",
            "Glaucoma Screening"
        )
        else -> listOf( // Default fallback for General or unlisted specialties
            "General Health Checkup",
            "Fever & Infection Care",
            "Routine Preventive Care"
        )
    }
}