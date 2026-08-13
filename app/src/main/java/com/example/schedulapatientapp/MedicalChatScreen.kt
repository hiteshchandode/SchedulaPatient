package com.example.schedulapatientapp

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalChatScreen(
    navController: NavController,
    viewModel: BookingViewModel,
    mainViewModel: MainViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    var chatInputText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    // Attachment Dialog State
    var showAttachmentMenu by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Dynamic Data from ViewModel
    val doctorName = viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" }
    val doctorSpecialty = viewModel.selectedDoctorSpecialty.ifEmpty { "Cardiologist" }
    val initialLetter = doctorName.replace("Dr. ", "").take(1).uppercase()

    val patientName = viewModel.patientName.ifEmpty { "Hitesh Chandode" }
    val patientAge = viewModel.patientAge.ifEmpty { "24" }
    val patientComplaint = viewModel.patientComplaint.ifEmpty { "Stomach pain" }

    // Live Messages List from ViewModel
    val chatMessages = viewModel.getChatForCurrentDoctor()

    // Pre-defined quick patient messages
    val quickMessages = listOf(
        "When should I take medicine?",
        "Is fasting required for tests?",
        "I sent my latest report",
        "Please check my symptoms"
    )

    // Activity Launchers for Gallery & Camera
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.sendImageMessage(it.toString())
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && tempCameraUri != null) {
            viewModel.sendImageMessage(tempCameraUri.toString())
        }
    }

    fun launchCamera() {
        val file = File(context.cacheDir, "temp_photo_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        tempCameraUri = uri
        cameraLauncher.launch(uri)
    }

    // Auto-scroll on new message
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

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
                    IconButton(onClick = { navController.navigate("appointment_details") }) {
                        Icon(Icons.Default.Info, contentDescription = "View Details", tint = Color(0xFF2196F3))
                    }
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
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // --- DOCTOR INFO HEADER ---
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                modifier = Modifier.size(60.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFE3F2FD)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(initialLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 24.sp)
                                }
                            }
                            Text(doctorName, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(doctorSpecialty, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                                Text("$patientName, $patientAge yrs, $patientComplaint", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Icon(Icons.Default.ChatBubble, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(20.dp))
                        }
                    }
                }

                item {
                    Text("Today", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }

                // --- DYNAMIC INTERACTIVE CHAT MESSAGES ---
                items(chatMessages) { message ->
                    val isPatient = message.sender == "patient"
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (isPatient) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = if (isPatient) Arrangement.End else Arrangement.Start
                        ) {
                            if (!isPatient) {
                                Surface(
                                    modifier = Modifier.size(32.dp),
                                    shape = CircleShape,
                                    color = Color(0xFFE3F2FD)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(initialLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }

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
                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                    // Render Attached Image
                                    if (message.imageUri != null) {
                                        AsyncImage(
                                            model = message.imageUri,
                                            contentDescription = "Attachment",
                                            modifier = Modifier
                                                .widthIn(max = 200.dp)
                                                .heightIn(max = 200.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }

                                    // Render Text
                                    if (message.text.isNotEmpty()) {
                                        Text(
                                            text = message.text,
                                            color = if (isPatient) Color.White else Color(0xFF1E293B),
                                            fontSize = 14.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = message.time,
                                        color = if (isPatient) Color.White.copy(alpha = 0.7f) else Color.Gray,
                                        fontSize = 10.sp,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- PRE-DEFINED QUICK PATIENT MESSAGES ROW ---
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickMessages) { msg ->
                    SuggestionChip(
                        onClick = {
                            viewModel.sendMessage(msg)
                            coroutineScope.launch {
                                if (chatMessages.isNotEmpty()) {
                                    listState.animateScrollToItem(chatMessages.size - 1)
                                }
                            }
                        },
                        label = { Text(msg, fontSize = 12.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFFE3F2FD),
                            labelColor = Color(0xFF2196F3)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFBBDEFB)),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            // --- ATTACHMENT MENU DIALOG ---
            if (showAttachmentMenu) {
                AlertDialog(
                    onDismissRequest = { showAttachmentMenu = false },
                    title = { Text("Send Media", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            TextButton(
                                onClick = {
                                    showAttachmentMenu = false
                                    launchCamera()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color(0xFF2196F3))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Take Photo from Camera", color = Color.Black)
                            }
                            TextButton(
                                onClick = {
                                    showAttachmentMenu = false
                                    galleryLauncher.launch("image/*")
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = Color(0xFF2196F3))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Choose from Gallery", color = Color.Black)
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { showAttachmentMenu = false }) {
                            Text("Cancel", color = Color.Red)
                        }
                    }
                )
            }

            // --- BOTTOM MESSAGE INPUT BAR ---
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, tonalElevation = 4.dp) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = chatInputText,
                        onValueChange = { chatInputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message...", fontSize = 14.sp) },
                        trailingIcon = {
                            IconButton(onClick = { showAttachmentMenu = true }) {
                                Icon(Icons.Default.AttachFile, contentDescription = "Attach File", tint = Color(0xFF2196F3))
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF2196F3),
                            unfocusedIndicatorColor = Color(0xFFE2E8F0)
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (chatInputText.isNotBlank()) {
                                viewModel.sendMessage(chatInputText)
                                chatInputText = ""
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
            onClick = { navController.navigate("payments") }
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
            onClick = { navController.navigate("profile") }
        )
    }
}


























































//package com.example.schedulapatientapp
//
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
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
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.content.FileProvider
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import kotlinx.coroutines.launch
//import java.io.File
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MedicalChatScreen(navController: NavController, viewModel: BookingViewModel) {
//    var chatInputText by remember { mutableStateOf("") }
//    val coroutineScope = rememberCoroutineScope()
//    val listState = rememberLazyListState()
//    val context = LocalContext.current
//
//    // Attachment Dialog State
//    var showAttachmentMenu by remember { mutableStateOf(false) }
//    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
//
//    // Dynamic Data from ViewModel
//    val doctorName = viewModel.selectedDoctorName.ifEmpty { "Dr. Kumar" }
//    val doctorSpecialty = viewModel.selectedDoctorSpecialty.ifEmpty { "Cardiologist" }
//    val initialLetter = doctorName.replace("Dr. ", "").take(1).uppercase()
//
//    val patientName = viewModel.patientName.ifEmpty { "Rudra" }
//    val patientAge = viewModel.patientAge.ifEmpty { "28" }
//    val patientComplaint = viewModel.patientComplaint.ifEmpty { "Stomach pain" }
//
//    // Live Messages List
//    val chatMessages = viewModel.getChatForCurrentDoctor()
//
//    // Pre-defined quick patient messages
//    val quickMessages = listOf(
//        "When should I take medicine?",
//        "Is fasting required for tests?",
//        "I sent my latest report",
//        "Please check my symptoms"
//    )
//
//    // Activity Launchers for Gallery & Camera
//    val galleryLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let {
//            viewModel.sendImageMessage(it.toString())
//        }
//    }
//
//    val cameraLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicture()
//    ) { success: Boolean ->
//        if (success && tempCameraUri != null) {
//            viewModel.sendImageMessage(tempCameraUri.toString())
//        }
//    }
//
//    fun launchCamera() {
//        val file = File(context.cacheDir, "temp_photo_${System.currentTimeMillis()}.jpg")
//        val uri = FileProvider.getUriForFile(
//            context,
//            "${context.packageName}.provider",
//            file
//        )
//        tempCameraUri = uri
//        cameraLauncher.launch(uri)
//    }
//
//    // Auto-scroll on new message
//    LaunchedEffect(chatMessages.size) {
//        if (chatMessages.isNotEmpty()) {
//            listState.animateScrollToItem(chatMessages.size - 1)
//        }
//    }
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
//                    IconButton(onClick = { navController.navigate("appointment_details") }) {
//                        Icon(Icons.Default.Info, contentDescription = "View Details", tint = Color(0xFF2196F3))
//                    }
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
//                state = listState,
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 16.dp),
//                verticalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                item { Spacer(modifier = Modifier.height(8.dp)) }
//
//                // --- DOCTOR INFO HEADER ---
//                item {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Surface(
//                                modifier = Modifier.size(60.dp),
//                                shape = RoundedCornerShape(12.dp),
//                                color = Color(0xFFE3F2FD)
//                            ) {
//                                Box(contentAlignment = Alignment.Center) {
//                                    Text(initialLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 24.sp)
//                                }
//                            }
//                            Text(doctorName, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
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
//                                Text(doctorSpecialty, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
//                                Text("$patientName, $patientAge yrs, $patientComplaint", fontWeight = FontWeight.Bold, fontSize = 13.sp)
//                            }
//                            Icon(Icons.Default.ChatBubble, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(20.dp))
//                        }
//                    }
//                }
//
//                item {
//                    Text("Today", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
//                }
//
//                // --- DYNAMIC INTERACTIVE CHAT MESSAGES ---
//                items(chatMessages) { message ->
//                    val isPatient = message.sender == "patient"
//                    Box(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentAlignment = if (isPatient) Alignment.CenterEnd else Alignment.CenterStart
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.Top,
//                            horizontalArrangement = if (isPatient) Arrangement.End else Arrangement.Start
//                        ) {
//                            if (!isPatient) {
//                                Surface(
//                                    modifier = Modifier.size(32.dp),
//                                    shape = CircleShape,
//                                    color = Color(0xFFE3F2FD)
//                                ) {
//                                    Box(contentAlignment = Alignment.Center) {
//                                        Text(initialLetter, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 14.sp)
//                                    }
//                                }
//                                Spacer(modifier = Modifier.width(8.dp))
//                            }
//
//                            Surface(
//                                shape = RoundedCornerShape(
//                                    topStart = 16.dp,
//                                    topEnd = 16.dp,
//                                    bottomStart = if (isPatient) 16.dp else 0.dp,
//                                    bottomEnd = if (isPatient) 0.dp else 16.dp
//                                ),
//                                color = if (isPatient) Color(0xFF2196F3) else Color.White,
//                                shadowElevation = 1.dp
//                            ) {
//                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
//                                    // Render Attached Image
//                                    if (message.imageUri != null) {
//                                        AsyncImage(
//                                            model = message.imageUri,
//                                            contentDescription = "Attachment",
//                                            modifier = Modifier
//                                                .widthIn(max = 200.dp)
//                                                .heightIn(max = 200.dp)
//                                                .clip(RoundedCornerShape(8.dp)),
//                                            contentScale = ContentScale.Crop
//                                        )
//                                        Spacer(modifier = Modifier.height(4.dp))
//                                    }
//
//                                    // Render Text
//                                    if (message.text.isNotEmpty()) {
//                                        Text(
//                                            text = message.text,
//                                            color = if (isPatient) Color.White else Color(0xFF1E293B),
//                                            fontSize = 14.sp
//                                        )
//                                    }
//
//                                    Spacer(modifier = Modifier.height(2.dp))
//                                    Text(
//                                        text = message.time,
//                                        color = if (isPatient) Color.White.copy(alpha = 0.7f) else Color.Gray,
//                                        fontSize = 10.sp,
//                                        textAlign = TextAlign.End,
//                                        modifier = Modifier.align(Alignment.End)
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            // --- PRE-DEFINED QUICK PATIENT MESSAGES ROW ---
//            LazyRow(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 12.dp, vertical = 6.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(quickMessages) { msg ->
//                    SuggestionChip(
//                        onClick = {
//                            viewModel.sendMessage(msg)
//                            coroutineScope.launch {
//                                if (chatMessages.isNotEmpty()) {
//                                    listState.animateScrollToItem(chatMessages.size - 1)
//                                }
//                            }
//                        },
//                        label = { Text(msg, fontSize = 12.sp) },
//                        colors = SuggestionChipDefaults.suggestionChipColors(
//                            containerColor = Color(0xFFE3F2FD),
//                            labelColor = Color(0xFF2196F3)
//                        ),
//                        border = BorderStroke(1.dp, Color(0xFFBBDEFB)),
//                        shape = RoundedCornerShape(20.dp)
//                    )
//                }
//            }
//
//            // --- ATTACHMENT MENU DIALOG ---
//            if (showAttachmentMenu) {
//                AlertDialog(
//                    onDismissRequest = { showAttachmentMenu = false },
//                    title = { Text("Send Media", fontWeight = FontWeight.Bold) },
//                    text = {
//                        Column {
//                            TextButton(
//                                onClick = {
//                                    showAttachmentMenu = false
//                                    launchCamera()
//                                },
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color(0xFF2196F3))
//                                Spacer(modifier = Modifier.width(12.dp))
//                                Text("Take Photo from Camera", color = Color.Black)
//                            }
//                            TextButton(
//                                onClick = {
//                                    showAttachmentMenu = false
//                                    galleryLauncher.launch("image/*")
//                                },
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = Color(0xFF2196F3))
//                                Spacer(modifier = Modifier.width(12.dp))
//                                Text("Choose from Gallery", color = Color.Black)
//                            }
//                        }
//                    },
//                    confirmButton = {},
//                    dismissButton = {
//                        TextButton(onClick = { showAttachmentMenu = false }) {
//                            Text("Cancel", color = Color.Red)
//                        }
//                    }
//                )
//            }
//
//            // --- BOTTOM MESSAGE INPUT BAR ---
//            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, tonalElevation = 4.dp) {
//                Row(
//                    modifier = Modifier.padding(12.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    OutlinedTextField(
//                        value = chatInputText,
//                        onValueChange = { chatInputText = it },
//                        modifier = Modifier.weight(1f),
//                        placeholder = { Text("Type a message...", fontSize = 14.sp) },
//                        trailingIcon = {
//                            IconButton(onClick = { showAttachmentMenu = true }) {
//                                Icon(Icons.Default.AttachFile, contentDescription = "Attach File", tint = Color(0xFF2196F3))
//                            }
//                        },
//                        shape = RoundedCornerShape(24.dp),
//                        singleLine = true,
//                        colors = TextFieldDefaults.colors(
//                            focusedContainerColor = Color.White,
//                            unfocusedContainerColor = Color.White,
//                            focusedIndicatorColor = Color(0xFF2196F3),
//                            unfocusedIndicatorColor = Color(0xFFE2E8F0)
//                        )
//                    )
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    IconButton(
//                        onClick = {
//                            if (chatInputText.isNotBlank()) {
//                                viewModel.sendMessage(chatInputText)
//                                chatInputText = ""
//                                coroutineScope.launch {
//                                    if (chatMessages.isNotEmpty()) {
//                                        listState.animateScrollToItem(chatMessages.size - 1)
//                                    }
//                                }
//                            }
//                        },
//                        modifier = Modifier
//                            .size(48.dp)
//                            .background(Color(0xFF2196F3), CircleShape)
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
//
//
//
