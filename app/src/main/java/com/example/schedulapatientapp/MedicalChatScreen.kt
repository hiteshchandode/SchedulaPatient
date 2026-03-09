package com.example.schedulapatientapp



import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


//  PAGE: PATIENT CHAT
//  Strictly follows the UI layout with doctor alphabet avatar.


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalChatScreen(navController: NavController) {
    var chatInputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Patient chat", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) { Icon(Icons.Default.MoreVert, contentDescription = "Options") }
                }
            )
        },
        bottomBar = { ChatBottomNavigation(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // --- DOCTOR INFO HEADER ---
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Alphabet Avatar instead of Image
                            Surface(
                                modifier = Modifier.size(60.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFE3F2FD)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("K", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 24.sp)
                                }
                            }
                            Text("Dr. Kumar", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Cardiologist", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AccessTime, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                    Text(" 12 yrs experience", color = Color.Gray, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(color = Color(0xFF00BCD4), shape = RoundedCornerShape(20.dp)) {
                                    Text("🥇 GOLD MEDALIST", color = Color.White, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // --- PATIENT SUMMARY BOX ---
                item {
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("PATIENT DETAILS", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("Rudra, male, Stomach pain", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Icon(Icons.Default.ChatBubble, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(20.dp))
                        }
                    }
                }

                item {
                    Text("Today, 10:23 AM", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }

                // --- DOCTOR MESSAGE WITH NUMBERED LIST ---
                item {
                    Row(verticalAlignment = Alignment.Top) {
                        // Small blue medical icon
                        Icon(
                            Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp).background(Color(0xFF2196F3), CircleShape).padding(6.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("I'm sorry to hear you're not feeling well. Here are some immediate things you can try:", fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(12.dp))

                                val suggestions = listOf(
                                    "Place a warm compress or heating pad on your stomach.",
                                    "Try to rest and relax as much as possible.",
                                    "Over-the-counter antacids may help reduce any acid buildup.",
                                    "If possible, try to eat bland foods like rice, bananas, or toast.",
                                    "Drink lots of water and try to stay"
                                )

                                suggestions.forEachIndexed { index, text ->
                                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                        Text("${index + 1}", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(text, fontSize = 14.sp, color = Color.DarkGray)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- BOTTOM MESSAGE INPUT ---
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, tonalElevation = 4.dp) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {




                    // Replace the red OutlinedTextField with this:
                    OutlinedTextField(
                        value = chatInputText,
                        onValueChange = { chatInputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message...", fontSize = 14.sp) },
                        trailingIcon = {
                            Icon(Icons.Default.AttachFile, contentDescription = null, tint = Color.Gray)
                        },
                        shape = RoundedCornerShape(24.dp),
                        // This simplified version avoids the common "red line" error in older Material 3 versions
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF2196F3),
                            unfocusedIndicatorColor = Color(0xFFE2E8F0)
                        )
                    )


                    Spacer(modifier = Modifier.width(8.dp))
                    // Blue Send Button
                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(48.dp).background(Color(0xFF2196F3), CircleShape)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }
    }
}

// UNIQUE BOTTOM NAVIGATION
@Composable
fun ChatBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("doctor_list") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, null) },
            label = { Text("Payments", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("records") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, null) },
            label = { Text("My Appt", fontSize = 10.sp) },
            selected = true,
            onClick = { navController.navigate("my_appointments") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF2196F3), selectedTextColor = Color(0xFF2196F3))
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile", fontSize = 10.sp) },
            selected = false,
            onClick = { }
        )
    }
}