package com.example.schedulapatientapp

import androidx.compose.ui.graphics.Color


data class Clinic(
    val name: String,
    val distance: String,
    val rating: String,
    val reviews: String
)

val Doctors = listOf(
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