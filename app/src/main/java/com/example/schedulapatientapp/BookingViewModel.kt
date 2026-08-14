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
    val text: String = "",
    val imageUri: String? = null,
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


    // Inside BookingViewModel class
    var paymentMode by mutableStateOf("Reception") // Default to "Reception" or "Online"

    // Token
    var tokenNumber by mutableStateOf("#14")

    // CHAT MANAGEMENT STATE
    private val _chatHistories = mutableStateMapOf<String, SnapshotStateList<ChatMessage>>()

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

    // Send attached image from camera or gallery
    fun sendImageMessage(uriString: String) {
        val currentChat = getChatForCurrentDoctor()
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

        currentChat.add(
            ChatMessage(
                sender = "patient",
                text = "Attached Image",
                imageUri = uriString,
                time = currentTime
            )
        )

        viewModelScope.launch {
            delay(1500)
            val doctorName = selectedDoctorName.ifEmpty { "Doctor" }
            currentChat.add(
                ChatMessage(
                    sender = "doctor",
                    text = "Thank you for sending the image/report. $doctorName will review it shortly.",
                    time = currentTime
                )
            )
        }
    }

    // Fetch or create a separate chat list with specialty-specific welcome message
    fun getChatForCurrentDoctor(): SnapshotStateList<ChatMessage> {
        val doctorKey = selectedDoctorName.ifEmpty { "DefaultDoctor" }
        val specialty = selectedDoctorSpecialty.ifEmpty { "General Physician" }

        return _chatHistories.getOrPut(doctorKey) {
            val initialGreeting = getSpecialtyGreeting(doctorKey, specialty)
            mutableStateListOf(
                ChatMessage(
                    sender = "doctor",
                    text = initialGreeting,
                    time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                )
            )
        }
    }

    // Dynamic initial greeting based on doctor specialty
    private fun getSpecialtyGreeting(doctorName: String, specialty: String): String {
        val lowerSpec = specialty.lowercase()
        return when {
            lowerSpec.contains("cardio") ->
                "Hello! I am $doctorName ($specialty). If you are experiencing chest discomfort, shortness of breath, or palpitations, please describe them."

            lowerSpec.contains("derm") || lowerSpec.contains("skin") ->
                "Hello! I am $doctorName ($specialty). You can share details or upload a photo of any skin rash or allergy you'd like me to examine."

            lowerSpec.contains("pedia") || lowerSpec.contains("child") ->
                "Hello! I am $doctorName ($specialty). How is your child feeling today? Please mention their age and fever/pain details."

            lowerSpec.contains("ortho") || lowerSpec.contains("bone") ->
                "Hello! I am $doctorName ($specialty). Are you facing joint stiffness, back pain, or swelling?"

            else ->
                "Hello! I am $doctorName ($specialty). How can I assist you regarding your health today?"
        }
    }

    // Tailored auto-reply based on BOTH Doctor Specialty AND Consult Type (In-Person, Video, Audio)
    private fun generateDoctorReply(query: String, doctorName: String): String {
        val lowerQuery = query.lowercase()
        val specialty = selectedDoctorSpecialty.lowercase()
        val type = consultType.lowercase()

        // 1. Consultation-Type Specific Instructions
        val consultInstruction = when {
            type.contains("video") ->
                "Since this is a Video Consultation, please join the video room 5 minutes before $selectedTime."

            type.contains("audio") || type.contains("phone") ->
                "Since this is an Audio Call, please keep your phone active around $selectedTime."

            else ->
                "Since this is an In-Person Consultation, please present your token ($tokenNumber) at the clinic counter."
        }

        // 2. Specialty Specific Guidance
        val specialtyAdvice = when {
            specialty.contains("cardio") -> when {
                lowerQuery.contains("chest") || lowerQuery.contains("pain") || lowerQuery.contains("breath") ->
                    "Avoid any physical exertion and keep your blood pressure reports ready."

                else ->
                    "I have recorded your cardiac symptoms for our discussion."
            }

            specialty.contains("derm") -> when {
                lowerQuery.contains("skin") || lowerQuery.contains("rash") || lowerQuery.contains("itch") ->
                    "Please do not apply heavy creams or scratch the area before the examination."

                else ->
                    "I have logged your skin concern details."
            }

            specialty.contains("ortho") -> when {
                lowerQuery.contains("joint") || lowerQuery.contains("bone") || lowerQuery.contains("swelling") ->
                    "Avoid heavy lifting and apply ice if there is active swelling."

                else ->
                    "Keep any recent X-rays or MRI reports handy."
            }

            else -> when {
                lowerQuery.contains("fever") || lowerQuery.contains("pain") ->
                    "Stay well hydrated and take adequate rest."

                else ->
                    "I have noted your update for our session."
            }
        }

        // Combine into a contextual doctor response
        return "Thank you for reaching out. $specialtyAdvice $consultInstruction"
    }
}




