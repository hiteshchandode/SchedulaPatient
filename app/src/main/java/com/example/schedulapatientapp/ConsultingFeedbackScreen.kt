package com.example.schedulapatientapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultingFeedbackScreen(navController: NavController) {
    // Independent states to track the ratings for the 3 distinct card groups
    var doctorRating by remember { mutableStateOf(4) }
    var hospitalRating by remember { mutableStateOf(3) }
    var waitingRating by remember { mutableStateOf(2) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Consulting Feedback", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1E293B)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(38.dp)
                            .background(Color(0xFFF8FAFC), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF64748B)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { FeedbackBottomNavigation(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC)) // Light aesthetic backdrop matches layout context
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 1. DOCTOR INFORMATION CARD ---
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Image placeholder container box matching previous designs
                    Surface(
                        modifier = Modifier.size(70.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFCBD5E1)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("L", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text("Dr. Lavangi", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("🩺 Gynecologist", fontSize = 13.sp, color = Color(0xFF64748B))
                        Text("🧰 15 yrs Experience", fontSize = 13.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(color = Color(0xFF00B4D8), shape = CircleShape) {
                            Text(
                                "Gold Medalist",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- 2. CONSULTING TIME STATIC INFORMATION STRIP ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Consulting time", fontSize = 14.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                    Text("Oct 7, 8 AM", fontSize = 14.sp, color = Color(0xFF1E293B), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. THE 3 INTERACTIVE RATING BAR BLOCK UNITS ---
            RatingCardGroup(title = "Consulting feedback", currentRating = doctorRating, onRatingSelected = { doctorRating = it })
            Spacer(modifier = Modifier.height(12.dp))

            RatingCardGroup(title = "Hospital/Clinic feedback", currentRating = hospitalRating, onRatingSelected = { hospitalRating = it })
            Spacer(modifier = Modifier.height(12.dp))

            RatingCardGroup(title = "Waiting time", currentRating = waitingRating, onRatingSelected = { waitingRating = it })

            Spacer(modifier = Modifier.height(32.dp))

            // --- 4. SUBMIT ACTION BUTTON BUTTON CONTAINER ---
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Submit Feedback", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// RATING COMPONENT FOR THE REUSABLE CARDS
@Composable
fun RatingCardGroup(title: String, currentRating: Int, onRatingSelected: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                for (i in 1..5) {
                    val isStarred = i <= currentRating
                    Icon(
                        imageVector = if (isStarred) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = "Star $i",
                        tint = if (isStarred) Color(0xFFFFC107) else Color(0xFFCBD5E1),
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 6.dp)
                            .clickable { onRatingSelected(i) }
                    )
                }
            }
        }
    }
}

// BOTTOM NAVIGATION IMPLEMENTATION MATCHING CURRENT DESIGNS
@Composable
fun FeedbackBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(icon = { Icon(Icons.Default.PersonSearch, null) }, label = { Text("Find a Doctor") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Folder, null) }, label = { Text("My Records") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Outlined.CalendarMonth, null) }, label = { Text("My Appt") }, selected = true, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") }, selected = false, onClick = { })
    }
}