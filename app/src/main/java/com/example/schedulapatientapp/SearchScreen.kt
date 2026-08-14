package com.example.schedulapatientapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.schedulapatientapp.database.DoctorEntity

fun getGreetingText(): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Good Morning ☀️"
        in 12..16 -> "Good Afternoon 🌤️"
        in 17..20 -> "Good Evening 🌆"
        else -> "Good Night 🌙"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navcontroller: NavHostController,
    bookingViewModel: BookingViewModel = viewModel(), // Connected BookingViewModel
    mainViewModel: MainViewModel = viewModel()
) {
    val doctorsFromRoom by mainViewModel.doctorsList.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }

    val filteredDoctors = doctorsFromRoom.filter { doctor ->
        doctor.name.contains(searchQuery, ignoreCase = true) ||
                doctor.specialty.contains(searchQuery, ignoreCase = true)
    }.distinctBy { it.name }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Search Doctor", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navcontroller.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { navcontroller.navigate("appointment_reminders") },
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(
                            onClick = { navcontroller.navigate("appointment_reminders") },
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFFF0FDF4), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Alerts",
                                tint = Color(0xFF2196F3),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color(0xFFF97316), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("2", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, null) },
                    label = { Text("Find Doctor") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, null) },
                    label = { Text("Records") },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navcontroller.navigate("patient_reengagement")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, null) },
                    label = { Text("Appointment") },
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        navcontroller.navigate("reschedule_by_doctor")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Profile") },
                    selected = selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                        navcontroller.navigate("profile")
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = getGreetingText(),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newValue -> searchQuery = newValue },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by name or specialty...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Top Specialists",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(400.dp),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(filteredDoctors) { doctor ->
                    DoctorItem(
                        doctor = doctor,
                        navController = navcontroller,
                        bookingViewModel = bookingViewModel
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun DoctorItem(
    doctor: DoctorEntity,
    navController: NavHostController,
    bookingViewModel: BookingViewModel
) {
    val themeColor = Color(0xFF2196F3)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                // SAVE SELECTED DOCTOR DETAILS TO SHARED VIEWMODEL
                bookingViewModel.selectedDoctorName = doctor.name
                bookingViewModel.selectedDoctorSpecialty = doctor.specialty

                navController.navigate("doctor_profile")
            }
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(themeColor.copy(alpha = 0.15f), CircleShape)
                .border(2.dp, themeColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = doctor.name.replace("Dr. ", "").take(1).uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = themeColor
            )
        }
        Text(
            text = doctor.name,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            maxLines = 1
        )
        Text(
            text = doctor.specialty,
            color = Color.Gray,
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}


