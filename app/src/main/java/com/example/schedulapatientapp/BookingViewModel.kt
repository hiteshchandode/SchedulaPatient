package com.example.schedulapatientapp



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BookingViewModel : ViewModel() {
    // Page 4 Data
    var selectedDate by mutableStateOf("1st Oct")
    var consultType by mutableStateOf("Regular")

    // Page 5 Data
    var selectedTime by mutableStateOf("10:00 AM")

    // Page 10 Data
    var patientName by mutableStateOf("")
    var patientAge by mutableStateOf("")
    var patientWeight by mutableStateOf("")
    var patientComplaint by mutableStateOf("")

    // Page 7 (Token)
    val tokenNumber = "7/25"
}