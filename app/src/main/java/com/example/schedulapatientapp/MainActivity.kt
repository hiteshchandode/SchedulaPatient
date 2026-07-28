package com.example.schedulapatientapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.schedulapatientapp.ui.theme.SchedulaPatientAppTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

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
                    val bookingViewModel: BookingViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController = navController) }

                        composable("doctor_list") {
                            SearchScreen(
                                navcontroller = navController,
                                bookingViewModel = bookingViewModel,
                                mainViewModel = mainViewModel
                            )
                        }

                        composable("doctor_profile") {
                            DoctorProfileScreen(
                                navController = navController,
                                bookingViewModel = bookingViewModel
                            )
                        }

                        composable("booking_step1") {
                            BookingStepOne(navController = navController, viewModel = bookingViewModel)
                        }

                        composable("booking_step2") {
                            TimeSlotScreen(navController = navController, viewModel = bookingViewModel)
                        }

                        composable("exact_time_picker") {
                            SelectExactTimeScreen(navController = navController, viewModel = bookingViewModel)
                        }

                        composable("final_confirmation") {
                            FinalConfirmationScreen(navController = navController, viewModel = bookingViewModel)
                        }

                        composable("slot_unavailable") {
                            SlotUnavailableScreen(navController, bookingViewModel)
                        }

                        composable("plan_date") {
                            PlanDateScreen(navController = navController, viewModel = bookingViewModel)
                        }

                        composable("patient_form") {
                            PatientFormScreen(navController = navController, viewModel = bookingViewModel)
                        }

                        composable("medical_chat") {
                            MedicalChatScreen(navController = navController, viewModel = bookingViewModel)
                        }

                        composable("my_appointments") {
                            MyAppointmentsScreen(navController = navController, mainViewModel = mainViewModel)
                        }

                        composable("appointment_details") { AppointmentDetailsScreen(navController = navController, viewModel = bookingViewModel) }
                        composable("appointment_cancel") { AppointmentCancelScreen(navController) }
                        composable("reschedule_appointment") { RescheduleAppointmentScreen(navController) }
                        composable("consulting_feedback") { ConsultingFeedbackScreen(navController) }
                        composable("appointment_reminders") { AppointmentRemindersScreen(navController) }
                        composable("reschedule_by_doctor") { RescheduleByDoctorScreen(navController) }
                        composable("patient_reengagement") { PatientReengagementScreen(navController) }
                        composable("seamless_appointment") { SeamlessAppointmentScreen(navController) }
                        composable("co_patient_collaboration") { CoPatientCollaborationScreen(navController) }
                        composable("customer_support") { CustomerSupportScreen(navController) }
                        composable("friends_and_family") { FriendsAndFamilyScreen(navController) }
                        composable("google_review") { GoogleReviewScreen(navController) }
                        composable("confirmation") { ConfirmationScreen(navController = navController, viewModel = bookingViewModel) }
                        composable("records") { RecordsScreen(navController = navController, viewModel = bookingViewModel) }
                        composable("profile") { ProfileScreen(navController) }
                    }
                }
            }
        }
    }
}



























//package com.example.schedulapatientapp
//
//
//import com.example.schedulapatientapp.TimeSlotScreen
//import com.example.schedulapatientapp.PatientFormScreen
//import com.example.schedulapatientapp.ConfirmationScreen
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.compose.viewModel // ERROR FIX: Import for viewModel()
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.schedulapatientapp.ui.theme.SchedulaPatientAppTheme
//import androidx.activity.viewModels
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//
//class MainActivity : ComponentActivity() {
//
//    // Initialize ViewModel at Activity level
//    private val mainViewModel: MainViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            SchedulaPatientAppTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val navController = rememberNavController()
//
//                    // Yahan hum ViewModel initialize kar rahe hain
//                    // Isse data saari screens ke beech share hoga
//                    val bookingViewModel: BookingViewModel = viewModel()
//
//                    // Observe dynamic database states from Room
//                    val doctorsList by mainViewModel.doctorsList.collectAsState()
//                    val appointmentsList by mainViewModel.appointmentsList.collectAsState()
//
//                    NavHost(navController = navController, startDestination = "login") {
//                        // Page 1: Login
//                        composable("login") {
//                            LoginScreen(navController = navController)
//                        }
//
//                        // Page 2: Search Doctor
//                        composable("doctor_list") {
//                            SearchScreen(navcontroller = navController)
//                        }
//
//                        // Page 3: Doctor Profile
//                        composable("doctor_profile") {
//                            DoctorProfileScreen(navController = navController)
//                        }
//
//                        // Page 4: Booking Step 1 (Date Selection)
//                        composable("booking_step1") {
//                            BookingStepOne(navController = navController, viewModel = bookingViewModel)
//                        }
//
//                        // Page 5: Time Selection
//                        composable("booking_step2") {
//                            TimeSlotScreen(navController = navController, viewModel = bookingViewModel)
//                        }
//                        //page:6 Time seletion by clock
//                        composable("exact_time_picker") {
//                            SelectExactTimeScreen(navController = navController, viewModel = bookingViewModel)
//                        }
//
//                        //page:7 confirmation screen
//                        composable("final_confirmation") {
//                            FinalConfirmationScreen(navController = navController, viewModel = bookingViewModel)
//                        }
//
//                        //page:8 unable to book slot unavailable
//                        composable("slot_unavailable") {
//                            SlotUnavailableScreen(navController, bookingViewModel)
//                        }
//
//                        //page:9 plan appotitment
//                        composable("plan_date") {
//                            PlanDateScreen(navController = navController, viewModel = bookingViewModel)
//                        }
//
//                        // Page 10: Patient Details
//                        composable("patient_form") {
//                            PatientFormScreen(navController = navController, viewModel = bookingViewModel)
//                        }
//
//                        // Page 11: medical chat screen
//                        composable("medical_chat") {
//                            MedicalChatScreen(navController = navController)
//                        }
//
//                        //for my Appt. in bottom bar
//                        composable("my_appointments") {
//                            MyAppointmentsScreen(navController = navController)
//                        }
//
//                        //page 14: Appot.Screen screen route
//                        composable("appointment_details") { AppointmentDetailsScreen(navController) }
//
//                            // 1. Existing screens...
//                            composable("my_appointments") { MyAppointmentsScreen(navController) }
//                            composable("appointment_details") { AppointmentDetailsScreen(navController) }
//
//                            // 2. ADD THIS: Route for the Cancel Screen
//                            composable("appointment_cancel") {
//                                AppointmentCancelScreen(navController)
//                            }
//
//                        //page 15: Reschedule appoitment
//                        composable("reschedule_appointment") {
//                            RescheduleAppointmentScreen(navController)
//                        }
//
//                        //page 16: Appointment reschduled is merge with Book appointment
//
//                        //page 17: Consulting feedback
//                        composable("consulting_feedback") {
//                            ConsultingFeedbackScreen(navController)
//                        }
//
//                        //page 18: Appointment reminder
//                        composable("appointment_reminders") {
//                            AppointmentRemindersScreen(navController)
//                        }
//
//                        //page 19: Appointment reschudul by doctor
//                        composable("reschedule_by_doctor") {
//                            RescheduleByDoctorScreen(navController)
//                        }
//
//                        //page 20: Patient Reengagement screen is here
//                        composable("patient_reengagement") {
//                            PatientReengagementScreen(navController)
//                        }
//
//                        //page 21: Seamless appointment
//                        composable("seamless_appointment") {
//                            SeamlessAppointmentScreen(navController)
//                        }
//
//                        //page 22: Co patient collaboration
//                        composable("co_patient_collaboration") {
//                            CoPatientCollaborationScreen(navController)
//                        }
//
//                        //page 23: Help & supprot
//                        composable("customer_support") {
//                            CustomerSupportScreen(navController)
//                        }
//
//                        //page 24: family and friends
//                        composable("friends_and_family") {
//                            FriendsAndFamilyScreen(navController)
//                        }
//
//                        //page 25: Google review lastScreen
//                        composable("google_review") {
//                            GoogleReviewScreen(navController)
//                        }
//
//
//
//                          // Page 7: Final Confirmation
//                          composable("confirmation") {
//                              ConfirmationScreen(navController = navController, viewModel = bookingViewModel)
//                          }
//
//                        composable("records") {
//                            RecordsScreen(navController = navController, viewModel = bookingViewModel)
//                        }
//                        //goes for profile section
//                        composable("profile") {
//                            ProfileScreen(navController)
//                        }
//
//                    }
//                }
//            }
//        }
//    }
//}

