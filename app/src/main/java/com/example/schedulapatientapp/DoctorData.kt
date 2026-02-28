package com.example.schedulapatientapp



import androidx.compose.ui.graphics.Color

// 1. We changed 'Clinic' to 'Doctor' so the code can find it
data class Doctor(
    val name: String,
    val specialty: String,
    val color: Color,
    val rating: Double = 4.9,          // Added default value
    val reviews: Int = 120,            // Added default value
    val experienceYears: Int = 10,     // Added default value
    val achievement: String = "Gold Medalist" // Added default value
)


//data class Clinic(
//    val id: Int,
//    val name: String,
//    val specialty: String,
//    val distance: String,
//    val rating: Double,
//    val experienceYears: Int,
//    val achievement: String, // e.g., "Gold Medalist"
//    val availability: String = "Available",
//    val reviews: Int
//)



// 2. Updated the list to match the new Doctor data
val Doctors = listOf(
    Doctor("Kumar", "Cardiologist", Color(0xFFF44336), 4.8, 150, 12, "Best Surgeon"),
    Doctor("Devi Shetty", "Neurologist", Color(0xFF9C27B0), 4.9, 200, 20, "Gold Medalist"),
    Doctor("Sudhir Gupta", "Orthopedic", Color(0xFF3F51B5), 4.7, 80, 15, "Top Specialist"),
    Doctor("Sarah", "Pediatrician", Color(0xFF00BCD4), 4.9, 95, 8, "Child Care Expert"),
    Doctor("Arvind Kumar", "General", Color(0xFF4CAF50), 4.5, 300, 25, "Senior Doctor"),
    Doctor("Nitin Sood", "Dermatologist", Color(0xFFFFEB3B), 4.6, 110, 10, "Skin Specialist"),
    Doctor("Atul Goel", "Surgeon", Color(0xFFFF9800), 4.8, 140, 18, "Award Winner"),
    Doctor("S. K. Gupta", "Psychiatrist", Color(0xFF795548), 4.9, 60, 22, "Top Consultant"),
    Doctor("Prathap Reddy", "Dentist", Color(0xFF607D8B), 4.7, 180, 14, "Best Dentist")
)














//import androidx.compose.ui.graphics.Color
//

data class Clinic(
    val id: Int,
    val name: String,
    val specialty: String,
    val distance: String,
    val rating: Double,
    val experienceYears: Int,
    val achievement: String, // e.g., "Gold Medalist"
    val availability: String = "Available",
    val reviews: Int
)
//
//val Doctors = listOf(
//    Doctor("Dr. Kumar", "Cardiologist", Color(0xFFF44336)),
//    Doctor("Dr. Devi Shetty", "Neurologist", Color(0xFF9C27B0)),
//    Doctor("Dr. Sudhir Gupta", "Orthopedic", Color(0xFF3F51B5)),
//    Doctor("Dr. Sarah", "Pediatrician", Color(0xFF00BCD4)),
//    Doctor("Dr. Arvind Kumar", "General", Color(0xFF4CAF50)),
//    Doctor("Dr. Nitin Sood", "Dermatologist", Color(0xFFFFEB3B)),
//    Doctor("Dr. Atul Goel", "Surgeon", Color(0xFFFF9800)),
//    Doctor("Dr. S. K. Gupta", "Psychiatrist", Color(0xFF795548)),
//    Doctor("Dr. Prathap Reddy", "Dentist", Color(0xFF607D8B))
//)