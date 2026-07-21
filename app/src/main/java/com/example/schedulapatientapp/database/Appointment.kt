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
    val status: String // "Upcoming", "Past", or "Cancelled"
)