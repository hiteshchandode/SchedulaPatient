package com.example.schedulapatientapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedulapatientapp.database.AppDatabase
import com.example.schedulapatientapp.database.AppRepository
import com.example.schedulapatientapp.database.AppointmentEntity
import com.example.schedulapatientapp.database.DoctorEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    // Live reactive state streams from Room
    val doctorsList: StateFlow<List<DoctorEntity>>
    val appointmentsList: StateFlow<List<AppointmentEntity>>

    // Live stream for the latest booked appointment (for Token & Queue Tracker)
    val latestAppointment: StateFlow<AppointmentEntity?>

    init {
        val dao = AppDatabase.getDatabase(application).appDao()
        repository = AppRepository(dao)

        // Seed initial doctor data in the background
        viewModelScope.launch {
            repository.seedInitialData()
        }

        // Convert Flow into StateFlow so Compose UI can collect it cleanly
        doctorsList = repository.allDoctors.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        appointmentsList = repository.allAppointments.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Stream for live token queue tracking
        latestAppointment = dao.getLatestAppointmentFlow().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    }

    // --- NEW: Full Appointment Save with Payment Mode & Payment Status ---
    fun saveFullAppointment(
        doctorName: String,
        doctorSpecialty: String,
        patientName: String,
        patientAge: String,
        patientComplaint: String,
        date: String,
        timeSlot: String,
        tokenNumber: String,
        paymentMode: String,
        paymentStatus: String
    ) {
        viewModelScope.launch {
            val numericToken = tokenNumber.replace("#", "").toIntOrNull() ?: 14
            val initialServingToken = (numericToken - 3).coerceAtLeast(1)

            val newAppointment = AppointmentEntity(
                doctorName = doctorName,
                doctorSpecialty = doctorSpecialty,
                patientName = patientName,
                date = date,
                timeSlot = timeSlot,
                tokenNumber = tokenNumber,
                status = "Upcoming",
                patientComplaint = patientComplaint,
                currentServingToken = initialServingToken,
                paymentMode = paymentMode,
                paymentStatus = paymentStatus
            )
            repository.insertAppointment(newAppointment)

            // Start live queue progression simulation
            simulateLiveQueue(numericToken)
        }
    }

    // --- NEW: Update Payment Status when user pays online later ---
    fun updatePayment(appointmentId: Int, paymentStatus: String, paymentMode: String) {
        viewModelScope.launch {
            val dao = AppDatabase.getDatabase(getApplication()).appDao()
            dao.updatePaymentDetails(appointmentId, paymentStatus, paymentMode)
        }
    }

    // Original addAppointment function (kept intact for existing UI calls)
    fun addAppointment(
        doctorName: String,
        patientName: String,
        date: String,
        timeSlot: String,
        tokenNumber: String
    ) {
        viewModelScope.launch {
            val numericToken = tokenNumber.replace("#", "").toIntOrNull() ?: 14
            val initialServingToken = (numericToken - 3).coerceAtLeast(1)

            val newAppointment = AppointmentEntity(
                doctorName = doctorName,
                patientName = patientName,
                date = date,
                timeSlot = timeSlot,
                tokenNumber = tokenNumber,
                status = "Upcoming",
                currentServingToken = initialServingToken
            )
            repository.insertAppointment(newAppointment)

            // Start live queue progression simulation
            simulateLiveQueue(numericToken)
        }
    }

    // Overloaded addAppointment function accepting full queue parameters
    fun addAppointmentWithQueueDetails(
        doctorName: String,
        patientName: String,
        date: String,
        timeSlot: String,
        tokenNumber: String,
        doctorSpecialty: String,
        consultType: String,
        patientComplaint: String
    ) {
        viewModelScope.launch {
            val numericToken = tokenNumber.replace("#", "").toIntOrNull() ?: 14
            val initialServingToken = (numericToken - 3).coerceAtLeast(1)

            val newAppointment = AppointmentEntity(
                doctorName = doctorName,
                patientName = patientName,
                date = date,
                timeSlot = timeSlot,
                tokenNumber = tokenNumber,
                status = "Upcoming",
                doctorSpecialty = doctorSpecialty,
                consultType = consultType,
                patientComplaint = patientComplaint,
                currentServingToken = initialServingToken
            )
            repository.insertAppointment(newAppointment)

            // Start live queue progression simulation
            simulateLiveQueue(numericToken)
        }
    }

    // Simulates clinic queue progression in real-time
    private fun simulateLiveQueue(patientToken: Int) {
        viewModelScope.launch {
            val dao = AppDatabase.getDatabase(getApplication()).appDao()
            var currentToken = (patientToken - 3).coerceAtLeast(1)

            while (currentToken < patientToken) {
                delay(10000) // Advances current serving token every 10 seconds
                currentToken++

                val lastAppt = latestAppointment.value
                if (lastAppt != null) {
                    dao.updateServingToken(lastAppt.id, currentToken)
                    if (currentToken == patientToken) {
                        dao.updateStatus(lastAppt.id, "In Progress")
                    }
                }
            }
        }
    }
}
























































//package com.example.schedulapatientapp
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.schedulapatientapp.database.AppDatabase
//import com.example.schedulapatientapp.database.AppRepository
//import com.example.schedulapatientapp.database.AppointmentEntity
//import com.example.schedulapatientapp.database.DoctorEntity
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//
//class MainViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository: AppRepository
//
//    // Live reactive state streams from Room
//    val doctorsList: StateFlow<List<DoctorEntity>>
//    val appointmentsList: StateFlow<List<AppointmentEntity>>
//
//    // Live stream for the latest booked appointment (for Token & Queue Tracker)
//    val latestAppointment: StateFlow<AppointmentEntity?>
//
//    init {
//        val dao = AppDatabase.getDatabase(application).appDao()
//        repository = AppRepository(dao)
//
//        // Seed initial doctor data in the background
//        viewModelScope.launch {
//            repository.seedInitialData()
//        }
//
//        // Convert Flow into StateFlow so Compose UI can collect it cleanly
//        doctorsList = repository.allDoctors.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )
//
//        appointmentsList = repository.allAppointments.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )
//
//        // Stream for live token queue tracking
//        latestAppointment = dao.getLatestAppointmentFlow().stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = null
//        )
//    }
//
//    // Original addAppointment function (kept intact for existing UI calls)
//    fun addAppointment(
//        doctorName: String,
//        patientName: String,
//        date: String,
//        timeSlot: String,
//        tokenNumber: String
//    ) {
//        viewModelScope.launch {
//            val numericToken = tokenNumber.replace("#", "").toIntOrNull() ?: 14
//            val initialServingToken = (numericToken - 3).coerceAtLeast(1)
//
//            val newAppointment = AppointmentEntity(
//                doctorName = doctorName,
//                patientName = patientName,
//                date = date,
//                timeSlot = timeSlot,
//                tokenNumber = tokenNumber,
//                status = "Upcoming",
//                currentServingToken = initialServingToken
//            )
//            repository.insertAppointment(newAppointment)
//
//            // Start live queue progression simulation
//            simulateLiveQueue(numericToken)
//        }
//    }
//
//    // Overloaded addAppointment function accepting full queue parameters
//    fun addAppointmentWithQueueDetails(
//        doctorName: String,
//        patientName: String,
//        date: String,
//        timeSlot: String,
//        tokenNumber: String,
//        doctorSpecialty: String,
//        consultType: String,
//        patientComplaint: String
//    ) {
//        viewModelScope.launch {
//            val numericToken = tokenNumber.replace("#", "").toIntOrNull() ?: 14
//            val initialServingToken = (numericToken - 3).coerceAtLeast(1)
//
//            val newAppointment = AppointmentEntity(
//                doctorName = doctorName,
//                patientName = patientName,
//                date = date,
//                timeSlot = timeSlot,
//                tokenNumber = tokenNumber,
//                status = "Upcoming",
//                doctorSpecialty = doctorSpecialty,
//                consultType = consultType,
//                patientComplaint = patientComplaint,
//                currentServingToken = initialServingToken
//            )
//            repository.insertAppointment(newAppointment)
//
//            // Start live queue progression simulation
//            simulateLiveQueue(numericToken)
//        }
//    }
//
//    // Simulates clinic queue progression in real-time
//    private fun simulateLiveQueue(patientToken: Int) {
//        viewModelScope.launch {
//            val dao = AppDatabase.getDatabase(getApplication()).appDao()
//            var currentToken = (patientToken - 3).coerceAtLeast(1)
//
//            while (currentToken < patientToken) {
//                delay(10000) // Advances current serving token every 10 seconds
//                currentToken++
//
//                val latest = dao.getLatestAppointmentFlow()
//                // Update room database token serving count
//                val lastAppt = latestAppointment.value
//                if (lastAppt != null) {
//                    dao.updateServingToken(lastAppt.id, currentToken)
//                    if (currentToken == patientToken) {
//                        dao.updateStatus(lastAppt.id, "In Progress")
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//
