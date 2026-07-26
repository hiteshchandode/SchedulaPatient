package com.example.schedulapatientapp


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedulapatientapp.database.AppDatabase
import com.example.schedulapatientapp.database.AppRepository
import com.example.schedulapatientapp.database.AppointmentEntity
import com.example.schedulapatientapp.database.DoctorEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    // Live reactive state streams from Room
    val doctorsList: StateFlow<List<DoctorEntity>>
    val appointmentsList: StateFlow<List<AppointmentEntity>>

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
    }

    // Function to book a new appointment directly into Room
    fun addAppointment(
        doctorName: String,
        patientName: String,
        date: String,
        timeSlot: String,
        tokenNumber: String
    ) {
        viewModelScope.launch {
            val newAppointment = AppointmentEntity(
                doctorName = doctorName,
                patientName = patientName,
                date = date,
                timeSlot = timeSlot,
                tokenNumber = tokenNumber,
                status = "Upcoming"
            )
            repository.insertAppointment(newAppointment)
        }
    }
}