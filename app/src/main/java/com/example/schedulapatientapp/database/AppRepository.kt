package com.example.schedulapatientapp.database

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {

    // --- DOCTOR DATA OPERATIONS ---
    val allDoctors: Flow<List<DoctorEntity>> = appDao.getAllDoctors()

    suspend fun insertDoctor(doctor: DoctorEntity) {
        appDao.insertDoctor(doctor)
    }

    // --- APPOINTMENT DATA OPERATIONS ---
    val allAppointments: Flow<List<AppointmentEntity>> = appDao.getAllAppointments()

    suspend fun insertAppointment(appointment: AppointmentEntity) {
        appDao.insertAppointment(appointment)
    }

    // --- PRE-POPULATE DATABASE METHOD ---
    // Automatically adds initial doctors if the database is empty
    suspend fun seedInitialData() {
        val initialDoctors = listOf(
            DoctorEntity(
                name = "Dr. Lavangi",
                specialty = "Gynecologist",
                experience = "15 Years",
                badge = "Gold Medalist"
            ),
            DoctorEntity(
                name = "Dr. Kumar",
                specialty = "General Physician",
                experience = "12 Years",
                badge = "Senior Consultant"
            )
        )

        initialDoctors.forEach { doctor ->
            appDao.insertDoctor(doctor)
        }
    }
}