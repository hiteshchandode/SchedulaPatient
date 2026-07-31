package com.example.schedulapatientapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // --- DOCTOR ACTIONS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctor(doctor: DoctorEntity)

    @Query("SELECT * FROM doctors")
    fun getAllDoctors(): Flow<List<DoctorEntity>>

    // --- APPOINTMENT ACTIONS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    @Query("SELECT * FROM appointments")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    // Get the latest booked appointment for live queue tracking
    @Query("SELECT * FROM appointments ORDER BY id DESC LIMIT 1")
    fun getLatestAppointmentFlow(): Flow<AppointmentEntity?>

    // Update serving token in real-time
    @Query("UPDATE appointments SET currentServingToken = :newToken WHERE id = :appointmentId")
    suspend fun updateServingToken(appointmentId: Int, newToken: Int)

    // Update appointment status (e.g. from "Upcoming" to "In Progress" or "Completed")
    @Query("UPDATE appointments SET status = :newStatus WHERE id = :appointmentId")
    suspend fun updateStatus(appointmentId: Int, newStatus: String)
}






























//package com.example.schedulapatientapp.database
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface AppDao {
//
//    // --- DOCTOR ACTIONS ---
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertDoctor(doctor: DoctorEntity)
//
//    @Query("SELECT * FROM doctors")
//    fun getAllDoctors(): Flow<List<DoctorEntity>>
//
//    // --- APPOINTMENT ACTIONS ---
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAppointment(appointment: AppointmentEntity)
//
//    @Query("SELECT * FROM appointments")
//    fun getAllAppointments(): Flow<List<AppointmentEntity>>
//}