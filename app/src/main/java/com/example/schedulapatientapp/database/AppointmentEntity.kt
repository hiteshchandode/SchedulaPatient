package com.example.schedulapatientapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val doctorName: String,
    val patientName: String,
    val date: String,
    val timeSlot: String,
    val tokenNumber: String,
    val status: String, // "Upcoming", "Past", or "Cancelled"

    // --- QUEUE TRACKER FIELDS ---
    val doctorSpecialty: String = "General",
    val consultType: String = "In-Person Consultation",
    val patientComplaint: String = "General Checkup",
    val currentServingToken: Int = 1,

    // --- PAYMENT FIELDS ---
    val paymentMode: String = "Reception", // "Online" or "Reception"
    val paymentStatus: String = "Pending"  // "Paid" or "Pending"
)
















































//package com.example.schedulapatientapp.database
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "appointments")
//data class AppointmentEntity(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val doctorName: String,
//    val patientName: String,
//    val date: String,
//    val timeSlot: String,
//    val tokenNumber: String,
//    val status: String, // "Upcoming", "Past", or "Cancelled"
//
//    // --- QUEUE TRACKER FIELDS (Default values keep existing code safe) ---
//    val doctorSpecialty: String = "General",
//    val consultType: String = "In-Person Consultation",
//    val patientComplaint: String = "General Checkup",
//    val currentServingToken: Int = 1
//)
//





























//package com.example.schedulapatientapp.database
//
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "appointments")
//data class AppointmentEntity(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val doctorName: String,
//    val patientName: String,
//    val date: String,
//    val timeSlot: String,
//    val tokenNumber: String,
//    val status: String // "Upcoming", "Past", or "Cancelled"
//)