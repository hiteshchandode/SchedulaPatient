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
            DoctorEntity(name = "Dr. Borole", specialty = "Cardiologist", experience = "10 Years", badge = "Senior Specialist"),
            DoctorEntity(name = "Dr. Rajesh dabhi", specialty = "Neurologist", experience = "18 Years", badge = "Gold Medalist"),
            DoctorEntity(name = "Dr. Sudhir Gupta", specialty = "Orthopedic", experience = "14 Years", badge = "Top Rated"),
            DoctorEntity(name = "Dr. Sunil Patil", specialty = "Pediatrician", experience = "11 Years", badge = "Child Specialist"),
            DoctorEntity(name = "Dr. Suresh Patil", specialty = "General", experience = "10 Years", badge = "Senior Consultant"),
            DoctorEntity(name = "Dr. Nitin Sood", specialty = "Dermatologist", experience = "11 Years", badge = "Skin Specialist"),
            DoctorEntity(name = "Dr. Kabir Singh", specialty = "Surgeon", experience = "16 Years", badge = "Master Surgeon"),
            DoctorEntity(name = "Dr. Choudhary", specialty = "Psychiatrist", experience = "8 Years", badge = "Mental Health Expert"),
            DoctorEntity(name = "Dr. Agarwal", specialty = "Dentist", experience = "12 Years", badge = "Dental Surgeon" )

        )

        initialDoctors.forEach { doctor ->
            appDao.insertDoctor(doctor)
        }
    }
}