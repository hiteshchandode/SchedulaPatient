package com.example.schedulapatientapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun RescheduleAppointmentScreen(navController: NavController) {
    var selectedSlot by remember { mutableStateOf("10:00 AM – 11:00 AM") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Reschedule Appointment", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1E293B)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(38.dp)
                            .background(Color(0xFFF1F5F9), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF64748B)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Menu logic */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFF1E293B))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { RescheduleBottomNavigation(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 1. DOCTOR CARD (Page 16 Layout) ---
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFCBD5E1)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("L", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Dr. Lavangi", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MedicalServices, null, tint = Color(0xFF2196F3), modifier = Modifier.size(14.dp))
                        Text(" Gynecologist", fontSize = 13.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, null, tint = Color(0xFF2196F3), modifier = Modifier.size(14.dp))
                        Text(" 15 yrs experience", fontSize = 13.sp, color = Color(0xFF64748B))
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(color = Color(0xFFE0F7FA), shape = CircleShape) {
                        Text(
                            "Gold Medalist",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            color = Color(0xFF00ACC1),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. CURRENT TIME CARD ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(color = Color(0xFFE3F2FD), shape = RoundedCornerShape(12.dp), modifier = Modifier.size(44.dp)) {
                        Icon(Icons.Default.CalendarMonth, null, tint = Color(0xFF2196F3), modifier = Modifier.padding(10.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Current Consulting Time", fontSize = 12.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
                        Text("Oct 7, 2023 • 8:00 AM", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- 3. SELECT NEW SLOT HEADER ---
            Text(
                "Select New Slot",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1E293B)
            )

            // MORNING SECTION
            SlotHeader(Icons.Default.WbSunny, "MORNING", Color(0xFFEA580C))
            Spacer(modifier = Modifier.height(4.dp))
            TimeSlotButton("10:00 AM – 11:00 AM", selectedSlot) { selectedSlot = it }
            TimeSlotButton("11:00 AM – 12:00 PM", selectedSlot) { selectedSlot = it }

            Spacer(modifier = Modifier.height(12.dp))

            // EVENING SECTION
            SlotHeader(Icons.Default.WbTwilight, "EVENING", Color(0xFF4338CA))
            Spacer(modifier = Modifier.height(4.dp))
            TimeSlotButton("06:00 PM – 07:00 PM", selectedSlot) { selectedSlot = it }
            TimeSlotButton("07:00 PM – 08:00 PM", selectedSlot) { selectedSlot = it }

            Spacer(modifier = Modifier.height(28.dp))

            // --- 4. CONFIRM BUTTON ---
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Confirm Reschedule", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SlotHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = color)
    }
}

@Composable
fun TimeSlotButton(time: String, selectedTime: String, onSelect: (String) -> Unit) {
    val isSelected = time == selectedTime

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(48.dp)
            .clickable { onSelect(time) }
            .then(
                if (!isSelected) Modifier.border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2196F3) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = time,
                color = if (isSelected) Color.White else Color(0xFF334155),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun RescheduleBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.CalendarToday, null) }, label = { Text("My Appt") }, selected = true, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Folder, null) }, label = { Text("Records") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") }, selected = false, onClick = { })
    }
}






























//package com.example.schedulapatientapp
//
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RescheduleAppointmentScreen(navController: NavController) {
//    // State to track which time slot is selected
//    var selectedSlot by remember { mutableStateOf("10:00 AM – 11:00 AM") }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Reschedule Appointment", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { /* Menu logic */ }) {
//                        Icon(Icons.Default.Menu, contentDescription = "Menu")
//                    }
//                }
//            )
//        },
//        bottomBar = { RescheduleBottomNavigation(navController) }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(Color.White)
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // --- 1. DOCTOR INFO SECTION ---
//            Surface(
//                modifier = Modifier.size(80.dp),
//                shape = RoundedCornerShape(16.dp),
//                color = Color(0xFFE0E0E0)
//            ) {
//                Box(contentAlignment = Alignment.Center) {
//                    Text("L", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
//                }
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            Text("Dr. Lavangi", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(Icons.Default.MedicalServices, null, tint = Color(0xFF2196F3), modifier = Modifier.size(14.dp))
//                Text(" Gynecologist", fontSize = 14.sp, color = Color.Gray)
//            }
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(Icons.Default.History, null, tint = Color(0xFF2196F3), modifier = Modifier.size(14.dp))
//                Text(" 15 yrs experience", fontSize = 14.sp, color = Color.Gray)
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            Surface(color = Color(0xFFE0F7FA), shape = RoundedCornerShape(12.dp)) {
//                Text(
//                    "Gold Medalist",
//                    color = Color(0xFF00BCD4),
//                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//                    fontSize = 12.sp, fontWeight = FontWeight.Bold
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // --- 2. CURRENT TIME CARD ---
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F9FF))
//            ) {
//                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
//                    Surface(color = Color(0xFFD1E4FF), shape = CircleShape, modifier = Modifier.size(44.dp)) {
//                        Icon(Icons.Default.CalendarMonth, null, tint = Color(0xFF2196F3), modifier = Modifier.padding(10.dp))
//                    }
//                    Spacer(modifier = Modifier.width(12.dp))
//                    Column {
//                        Text("Current Consulting Time", fontSize = 12.sp, color = Color.Gray)
//                        Text("Oct 7, 2023 • 8:00 AM", fontWeight = FontWeight.Bold, fontSize = 15.sp)
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // --- 3. SELECT NEW SLOT ---
//            Text(
//                "Select New Slot",
//                modifier = Modifier.fillMaxWidth(),
//                fontWeight = FontWeight.Bold,
//                fontSize = 18.sp
//            )
//
//            // MORNING SECTION
//            SlotHeader(Icons.Default.LightMode, "MORNING")
//            TimeSlotButton("10:00 AM – 11:00 AM", selectedSlot) { selectedSlot = it }
//            TimeSlotButton("11:00 AM – 12:00 PM", selectedSlot) { selectedSlot = it }
//
//            // EVENING SECTION
//            SlotHeader(Icons.Default.Nightlight, "EVENING")
//            TimeSlotButton("06:00 PM – 07:00 PM", selectedSlot) { selectedSlot = it }
//            TimeSlotButton("07:00 PM – 08:00 PM", selectedSlot) { selectedSlot = it }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // --- 4. CONFIRM BUTTON ---
//            Button(
//                onClick = { navController.popBackStack() },
//                modifier = Modifier.fillMaxWidth().height(56.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
//            ) {
//                Text("Confirm Reschedule", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
//
//@Composable
//fun SlotHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
//    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
//        Icon(icon, null, tint = Color(0xFFFFB74D), modifier = Modifier.size(20.dp))
//        Text(" $title", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
//    }
//}
//
//@Composable
//fun TimeSlotButton(time: String, selectedTime: String, onSelect: (String) -> Unit) {
//    val isSelected = time == selectedTime
//    val bgColor = if (isSelected) Color(0xFF2196F3) else Color.White
//    val textColor = if (isSelected) Color.White else Color.Black
//    val borderColor = if (isSelected) Color.Transparent else Color(0xFFEEEEEE)
//
//    Surface(
//        onClick = { onSelect(time) },
//        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(50.dp),
//        shape = RoundedCornerShape(10.dp),
//        color = bgColor,
//        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
//        shadowElevation = 1.dp
//    ) {
//        Box(contentAlignment = Alignment.Center) {
//            Text(time, color = textColor, fontWeight = FontWeight.Bold)
//        }
//    }
//}
//
//@Composable
//fun RescheduleBottomNavigation(navController: NavController) {
//    NavigationBar(containerColor = Color.White) {
//        // As per your order: 1. Find Doctor (Search), 2. Records, 3. Appointment (My Appt), 4. Profile
//        NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") }, selected = false, onClick = { })
//        NavigationBarItem(icon = { Icon(Icons.Default.CalendarToday, null) }, label = { Text("My Appt") }, selected = true, onClick = { })
//        NavigationBarItem(icon = { Icon(Icons.Default.Folder, null) }, label = { Text("Records") }, selected = false, onClick = { })
//        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") }, selected = false, onClick = { })
//    }
//}