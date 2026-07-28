package com.example.schedulapatientapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalChatScreen(navController: NavController, viewModel: BookingViewModel) {
    val doctorName = viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" }
    val doctorSpecialty = viewModel.selectedDoctorSpecialty.ifEmpty { "General Medical Care" }
    val initialLetter = doctorName.replace("Dr. ", "").take(1).uppercase()

    val chatMessages = viewModel.getChatForCurrentDoctor()
    var typedMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto scroll to latest message when message count changes
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color(0xFFE3F2FD)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(initialLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(doctorSpecialty, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
        ) {
            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(chatMessages) { message ->
                    val isPatient = message.sender == "patient"
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (isPatient) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isPatient) 16.dp else 0.dp,
                                bottomEnd = if (isPatient) 0.dp else 16.dp
                            ),
                            color = if (isPatient) Color(0xFF2196F3) else Color.White,
                            shadowElevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                                Text(
                                    text = message.text,
                                    color = if (isPatient) Color.White else Color(0xFF1E293B),
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = message.time,
                                    color = if (isPatient) Color.White.copy(alpha = 0.7f) else Color.Gray,
                                    fontSize = 10.sp,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                                    //alignment = Alignment.End,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Input Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = typedMessage,
                        onValueChange = { typedMessage = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type your message...", color = Color.LightGray) },
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF1F5F9),
                            focusedContainerColor = Color(0xFFF1F5F9),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF2196F3)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (typedMessage.isNotBlank()) {
                                viewModel.sendMessage(typedMessage)
                                typedMessage = ""
                                coroutineScope.launch {
                                    if (chatMessages.isNotEmpty()) {
                                        listState.animateScrollToItem(chatMessages.size - 1)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF2196F3), CircleShape)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }
    }
}






























//package com.example.schedulapatientapp
//
//
//
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//
//
////  PAGE: PATIENT CHAT
////  Strictly follows the UI layout with doctor alphabet avatar.
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MedicalChatScreen(navController: NavController) {
//    var chatInputText by remember { mutableStateOf("") }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Patient chat", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { navController.navigate("appointment_details")}) { Icon(Icons.Default.Info, contentDescription = "View Details",tint = Color(0xFF2196F3)) }
//                }
//            )
//        },
//        bottomBar = { ChatBottomNavigation(navController) }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(Color(0xFFF8FAFC))
//        ) {
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 16.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                item { Spacer(modifier = Modifier.height(8.dp)) }
//
//                // --- DOCTOR INFO HEADER ---
//                item {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            // Alphabet Avatar instead of Image
//                            Surface(
//                                modifier = Modifier.size(60.dp),
//                                shape = RoundedCornerShape(12.dp),
//                                color = Color(0xFFE3F2FD)
//                            ) {
//                                Box(contentAlignment = Alignment.Center) {
//                                    Text("K", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 24.sp)
//                                }
//                            }
//                            Text("Dr. Kumar", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
//                        }
//
//                        Spacer(modifier = Modifier.width(12.dp))
//
//                        Card(
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = CardDefaults.cardColors(containerColor = Color.White),
//                            shape = RoundedCornerShape(16.dp)
//                        ) {
//                            Column(modifier = Modifier.padding(16.dp)) {
//                                Text("Cardiologist", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    Icon(Icons.Default.AccessTime, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
//                                    Text(" 12 yrs experience", color = Color.Gray, fontSize = 12.sp)
//                                }
//                                Spacer(modifier = Modifier.height(8.dp))
//                                Surface(color = Color(0xFF00BCD4), shape = RoundedCornerShape(20.dp)) {
//                                    Text("🥇 GOLD MEDALIST", color = Color.White, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
//                                }
//                            }
//                        }
//                    }
//                }
//
//                // --- PATIENT SUMMARY BOX ---
//                item {
//                    Surface(
//                        color = Color(0xFFF1F5F9),
//                        shape = RoundedCornerShape(12.dp),
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
//                            Column(modifier = Modifier.weight(1f)) {
//                                Text("PATIENT DETAILS", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
//                                Text("Rudra, male, Stomach pain", fontWeight = FontWeight.Bold, fontSize = 13.sp)
//                            }
//                            Icon(Icons.Default.ChatBubble, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(20.dp))
//                        }
//                    }
//                }
//
//                item {
//                    Text("Today, 10:23 AM", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
//                }
//
//                // --- DOCTOR MESSAGE WITH NUMBERED LIST ---
//                item {
//                    Row(verticalAlignment = Alignment.Top) {
//                        // Small blue medical icon
//                        Icon(
//                            Icons.Default.MedicalServices,
//                            contentDescription = null,
//                            tint = Color.White,
//                            modifier = Modifier.size(32.dp).background(Color(0xFF2196F3), CircleShape).padding(6.dp)
//                        )
//                        Spacer(modifier = Modifier.width(12.dp))
//                        Card(
//                            colors = CardDefaults.cardColors(containerColor = Color.White),
//                            shape = RoundedCornerShape(16.dp)
//                        ) {
//                            Column(modifier = Modifier.padding(16.dp)) {
//                                Text("I'm sorry to hear you're not feeling well. Here are some immediate things you can try:", fontSize = 14.sp)
//                                Spacer(modifier = Modifier.height(12.dp))
//
//                                val suggestions = listOf(
//                                    "Place a warm compress or heating pad on your stomach.",
//                                    "Try to rest and relax as much as possible.",
//                                    "Over-the-counter antacids may help reduce any acid buildup.",
//                                    "If possible, try to eat bland foods like rice, bananas, or toast.",
//                                    "Drink lots of water and try to stay"
//                                )
//
//                                suggestions.forEachIndexed { index, text ->
//                                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
//                                        Text("${index + 1}", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 14.sp)
//                                        Spacer(modifier = Modifier.width(12.dp))
//                                        Text(text, fontSize = 14.sp, color = Color.DarkGray)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            // --- BOTTOM MESSAGE INPUT ---
//            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, tonalElevation = 4.dp) {
//                Row(
//                    modifier = Modifier.padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//
//
//
//                    // Replace the red OutlinedTextField with this:
//                    OutlinedTextField(
//                        value = chatInputText,
//                        onValueChange = { chatInputText = it },
//                        modifier = Modifier.weight(1f),
//                        placeholder = { Text("Type a message...", fontSize = 14.sp) },
//                        trailingIcon = {
//                            Icon(Icons.Default.AttachFile, contentDescription = null, tint = Color.Gray)
//                        },
//                        shape = RoundedCornerShape(24.dp),
//                        // This simplified version avoids the common "red line" error in older Material 3 versions
//                        singleLine = true,
//                        colors = TextFieldDefaults.colors(
//                            focusedContainerColor = Color.White,
//                            unfocusedContainerColor = Color.White,
//                            focusedIndicatorColor = Color(0xFF2196F3),
//                            unfocusedIndicatorColor = Color(0xFFE2E8F0)
//                        )
//                    )
//
//
//                    Spacer(modifier = Modifier.width(8.dp))
//                    // Blue Send Button
//                    IconButton(
//                        onClick = { },
//                        modifier = Modifier.size(48.dp).background(Color(0xFF2196F3), CircleShape)
//                    ) {
//                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
//                    }
//                }
//            }
//        }
//    }
//}
//
//// UNIQUE BOTTOM NAVIGATION
//@Composable
//fun ChatBottomNavigation(navController: NavController) {
//    NavigationBar(containerColor = Color.White) {
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Search, null) },
//            label = { Text("Search", fontSize = 10.sp) },
//            selected = false,
//            onClick = { navController.navigate("doctor_list") }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.AccountBalanceWallet, null) },
//            label = { Text("Payments", fontSize = 10.sp) },
//            selected = false,
//            onClick = { navController.navigate("records") }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.DateRange, null) },
//            label = { Text("My Appt", fontSize = 10.sp) },
//            selected = true,
//            onClick = { navController.navigate("my_appointments") },
//            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF2196F3), selectedTextColor = Color(0xFF2196F3))
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Person, null) },
//            label = { Text("Profile", fontSize = 10.sp) },
//            selected = false,
//            onClick = { }
//        )
//    }
//}