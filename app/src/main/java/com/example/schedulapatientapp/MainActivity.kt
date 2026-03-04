
package com.example.schedulapatientapp

import com.example.schedulapatientapp.TimeSlotScreen
import com.example.schedulapatientapp.PatientDetailsScreen
import com.example.schedulapatientapp.ConfirmationScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel // ERROR FIX: Import for viewModel()
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.schedulapatientapp.ui.theme.SchedulaPatientAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchedulaPatientAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Yahan hum ViewModel initialize kar rahe hain
                    // Isse data saari screens ke beech share hoga
                    val bookingViewModel: BookingViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "login") {
                        // Page 1: Login
                        composable("login") {
                            LoginScreen(navController = navController)
                        }

                        // Page 2: Search Doctor
                        composable("doctor_list") {
                            SearchScreen(navcontroller = navController)
                        }

                        // Page 3: Doctor Profile
                        composable("doctor_profile") {
                            DoctorProfileScreen(navController = navController)
                        }

                        // Page 4: Booking Step 1 (Date Selection)
                        composable("booking_step1") {
                            BookingStepOne(navController = navController, viewModel = bookingViewModel)
                        }

                        // Page 5: Time Selection
                        composable("booking_step2") {
                            TimeSlotScreen(navController = navController, viewModel = bookingViewModel)
                        }
                        //page:6 Time seletion by clock
                        composable("exact_time_picker") {
                            SelectExactTimeScreen(navController = navController, viewModel = bookingViewModel)
                        }

                        //page:7 confirmation screen
                        composable("final_confirmation") {
                            FinalConfirmationScreen(navController = navController, viewModel = bookingViewModel)
                        }

                        //page:8 unable to book slot unavailable
                        composable("slot_unavailable") {
                            SlotUnavailableScreen(navController, bookingViewModel)
                        }

                        // Page 10: Patient Details
                        composable("patient_details") {
                            PatientDetailsScreen(navController = navController, viewModel = bookingViewModel)
                        }

                        // Page 7: Final Confirmation
                        composable("confirmation") {
                            ConfirmationScreen(navController = navController, viewModel = bookingViewModel)
                        }

                        composable("records") {
                            RecordsScreen(navController = navController, viewModel = bookingViewModel)
                        }
                        //goes for profile section
                        composable("profile") {
                            ProfileScreen(navController)
                        }

                    }
                }
            }
        }
    }
}

