package com.example.schedulapatientapp



// This holds the "blueprint" for each doctor and clinic
data class Doctor(
    val name: String,
    val specialty: String,
    //val imageId: Int // Reference to your image in drawable
)

data class Clinic(
    val name: String,
    val distance: String,
    val rating: String,
    val reviews: String
)