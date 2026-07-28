package com.example.schedulapatientapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// Data model for chat messages
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String, // "patient" or "doctor"
    val text: String,
    val time: String = "Just now"
)

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

    // CHAT MANAGEMENT STATE
    private val _chatHistories = mutableStateMapOf<String, SnapshotStateList<ChatMessage>>()

    // Fetch or create a separate chat list for the selected doctor
    fun getChatForCurrentDoctor(): SnapshotStateList<ChatMessage> {
        val doctorKey = selectedDoctorName.ifEmpty { "DefaultDoctor" }
        return _chatHistories.getOrPut(doctorKey) {
            mutableStateListOf(
                ChatMessage(
                    sender = "doctor",
                    text = "Hello! I am $doctorKey. How can I assist you regarding your health today?",
                    time = "10:00 AM"
                )
            )
        }
    }

    // Send user message and simulate doctor auto-reply after 1.5 seconds
    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val currentChat = getChatForCurrentDoctor()
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

        // 1. Add Patient Message
        currentChat.add(ChatMessage(sender = "patient", text = userText, time = currentTime))

        // 2. Trigger Doctor Reply
        viewModelScope.launch {
            delay(1500)
            val doctorName = selectedDoctorName.ifEmpty { "Doctor" }
            val doctorReply = generateDoctorReply(userText, doctorName)
            currentChat.add(ChatMessage(sender = "doctor", text = doctorReply, time = currentTime))
        }
    }

    // Smart contextual reply generation logic
    private fun generateDoctorReply(query: String, doctorName: String): String {
        val lower = query.lowercase()
        return when {
            lower.contains("pain") || lower.contains("fever") || lower.contains("sick") ->
                "I understand. Please stay hydrated and rest well. I have made a note of your symptoms for our appointment."
            lower.contains("hello") || lower.contains("hi") || lower.contains("hey") ->
                "Hello! Please describe your symptoms or any health issues you are currently facing."
            lower.contains("time") || lower.contains("appointment") || lower.contains("when") ->
                "Your appointment has been registered. Please arrive 10 minutes prior to your token time."
            else ->
                "Thank you for sharing. $doctorName has received your note and will discuss this during your consultation."
        }
    }
}































//package com.example.schedulapatientapp
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//
//class BookingViewModel : ViewModel() {
//
//    // Dynamic Selected Doctor Details
//    var selectedDoctorName by mutableStateOf("Dr. Kumar")
//    var selectedDoctorSpecialty by mutableStateOf("Cardiologist")
//
//    // Page 4 Data
//    var selectedDate by mutableStateOf("5 March 2026")
//    var consultType by mutableStateOf("In-Person Consultation")
//
//    // Page 5 Data
//    var selectedTime by mutableStateOf("10:15 AM")
//
//    // Page 10 Data
//    var patientName by mutableStateOf("Hitesh Chandode")
//    var patientAge by mutableStateOf("24")
//    var patientWeight by mutableStateOf("68 kg")
//    var patientComplaint by mutableStateOf("Fever")
//
//    // Token
//    var tokenNumber by mutableStateOf("#14")
//}
