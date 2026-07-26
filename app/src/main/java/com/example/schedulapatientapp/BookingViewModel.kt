package com.example.schedulapatientapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BookingViewModel : ViewModel() {

    // Dynamic Selected Doctor Details
    var selectedDoctorName by mutableStateOf("Dr. Kumar")
    var selectedDoctorSpecialty by mutableStateOf("Cardiologist")

    // Page 4 Data
    var selectedDate by mutableStateOf("5 March 2026")
    var consultType by mutableStateOf("In-Person Consultation")

    // Page 5 Data
    var selectedTime by mutableStateOf("10:15 AM")

    // Page 10 Data
    var patientName by mutableStateOf("Hitesh Chandode")
    var patientAge by mutableStateOf("24")
    var patientWeight by mutableStateOf("68 kg")
    var patientComplaint by mutableStateOf("Fever")

    // Token
    var tokenNumber by mutableStateOf("#14")
}
