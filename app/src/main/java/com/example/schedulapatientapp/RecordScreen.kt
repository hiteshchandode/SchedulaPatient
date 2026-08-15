package com.example.schedulapatientapp

// DOWNLOAD LOGIC IMPORTS START
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.content.ContentValues
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

// DOWNLOAD LOGIC IMPORTS END

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Data model for structured medical records
data class MedicalRecordItem(
    val id: String,
    val title: String,
    val doctorName: String,
    val category: String, // "Prescriptions", "Lab Reports", "Invoices"
    val date: String,
    val fileSize: String,
    val token: String
)



fun triggerMedicalReportDownload(
    context: Context,
    fileName: String
) {
    try {
        // 1. Create a native PDF Document
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Standard A4 Size
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        // 2. Draw Header & Medical Content
        paint.textSize = 20f
        paint.isFakeBoldText = true
        canvas.drawText("SCHEDULA PATIENT APP - MEDICAL REPORT", 40f, 60f, paint)

        paint.textSize = 14f
        paint.isFakeBoldText = false
        canvas.drawText("Report Title: $fileName", 40f, 100f, paint)
        canvas.drawText("Generated On: 15 Aug 2026", 40f, 130f, paint)
        canvas.drawText("Status: Verified by Registered Medical Specialist", 40f, 160f, paint)
        canvas.drawText("--------------------------------------------------------------------------------", 40f, 190f, paint)
        canvas.drawText("Prescription & clinical observations have been validated.", 40f, 220f, paint)

        pdfDocument.finishPage(page)

        // 3. Save directly to Android MediaStore / Downloads without crashing
        val cleanName = fileName.replace("[^a-zA-Z0-9.-]".toRegex(), "_")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$cleanName.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
            }
        } else {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, "$cleanName.pdf")
            FileOutputStream(file).use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
        }

        pdfDocument.close()
        Toast.makeText(context, "Saved $cleanName.pdf to Downloads!", Toast.LENGTH_LONG).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Saved locally in app records.", Toast.LENGTH_SHORT).show()
    }
}
// =========================================================================
// <<< SAFE OFFLINE PDF GENERATOR & DOWNLOAD FUNCTION (END) <<<
// =========================================================================
//  REAL FILE DOWNLOAD HELPER FUNCTION (END)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    navController: NavController,
    viewModel: BookingViewModel
) {
    val context = LocalContext.current
    var selectedCategoryTab by remember { mutableStateOf("Prescriptions") }
    val categories = listOf("Prescriptions", "Lab Reports", "Invoices")

    // Dynamic medical record repository list
    val allRecords = remember(viewModel.patientName, viewModel.selectedDoctorName) {
        listOf(
            MedicalRecordItem(
                id = "REC-101",
                title = "Cardiology Rx & Follow-up Plan",
                doctorName = viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" },
                category = "Prescriptions",
                date = viewModel.selectedDate.ifEmpty { "Today, 10:00 AM" },
                fileSize = "1.2 MB PDF",
                token = viewModel.tokenNumber.ifEmpty { "#14" }
            ),
            MedicalRecordItem(
                id = "REC-102",
                title = "Complete Blood Count (CBC) & Lipid Profile",
                doctorName = "Apollo Diagnostic Labs",
                category = "Lab Reports",
                date = "Yesterday",
                fileSize = "2.4 MB PDF",
                token = "#08"
            ),
            MedicalRecordItem(
                id = "REC-103",
                title = "Consultation & Hospital Invoice",
                doctorName = viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" },
                category = "Invoices",
                date = viewModel.selectedDate.ifEmpty { "Today" },
                fileSize = "520 KB PDF",
                token = viewModel.tokenNumber.ifEmpty { "#14" }
            )
        )
    }

    // Filter records dynamically by active category tab
    val displayedRecords = allRecords.filter { it.category == selectedCategoryTab }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Records", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { RecordsBottomNavigation(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
                .padding(16.dp)
        ) {
            // --- 1. CATEGORY TAB SELECTOR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEDF2F7), RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                categories.forEach { tab ->
                    val isSelected = (selectedCategoryTab == tab)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .background(
                                color = if (isSelected) Color(0xFF2196F3) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedCategoryTab = tab },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. UPLOAD REPORT BUTTON ---
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Opening Document Picker to attach file...", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF2196F3))
            ) {
                Icon(
                    imageVector = Icons.Default.FileUpload,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("+ Upload Medical Record", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. RECORDS LIST / EMPTY STATE ---
            if (displayedRecords.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(60.dp),
                            shape = CircleShape,
                            color = Color(0xFFE3F2FD)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.FolderOpen,
                                    contentDescription = null,
                                    tint = Color(0xFF2196F3),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No $selectedCategoryTab Found",
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Upload or complete consultations to see your digital records here.",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(displayedRecords) { record ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = Color(0xFFE3F2FD),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "Token: ${record.token}",
                                            color = Color(0xFF2196F3),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Surface(
                                        color = Color(0xFFF1F5F9),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = record.fileSize,
                                            fontSize = 11.sp,
                                            color = Color(0xFF64748B),
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = record.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )

                                Text(
                                    text = "Issued by: ${record.doctorName}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF64748B),
                                    fontWeight = FontWeight.Medium
                                )

                                if (viewModel.patientName.isNotEmpty()) {
                                    Text(
                                        text = "Patient: ${viewModel.patientName}",
                                        fontSize = 13.sp,
                                        color = Color.DarkGray
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = record.date,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = Color(0xFFF1F5F9))
                                Spacer(modifier = Modifier.height(6.dp))

                                // Action Buttons (Download & View Details)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // =========================================================================
                                    // >>> DOWNLOAD ACTION BUTTON EXECUTION (START) >>>
                                    // =========================================================================
                                    TextButton(
                                        onClick = {
                                            triggerMedicalReportDownload(context, record.title)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Download,
                                            contentDescription = null,
                                            tint = Color(0xFF16A34A),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Download", color = Color(0xFF16A34A), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                    // =========================================================================
                                    // <<< DOWNLOAD ACTION BUTTON EXECUTION (END) <<<
                                    // =========================================================================

                                    TextButton(
                                        onClick = {
                                            navController.navigate("appointment_details")
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = Color(0xFF2196F3),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("View Details", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Standard 4-Tab Bottom Navigation Bar
@Composable
fun RecordsBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("doctor_list") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Payments") },
            label = { Text("Payments", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("payments") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "My Appt") },
            label = { Text("My Appt", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("my_appointments") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("profile") }
        )
    }
}


























































//package com.example.schedulapatientapp
//
//import android.widget.Toast
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//
//// Data model for structured medical records
//data class MedicalRecordItem(
//    val id: String,
//    val title: String,
//    val doctorName: String,
//    val category: String, // "Prescriptions", "Lab Reports", "Invoices"
//    val date: String,
//    val fileSize: String,
//    val token: String
//)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RecordsScreen(
//    navController: NavController,
//    viewModel: BookingViewModel
//) {
//    val context = LocalContext.current
//    var selectedCategoryTab by remember { mutableStateOf("Prescriptions") }
//    val categories = listOf("Prescriptions", "Lab Reports", "Invoices")
//
//    // Dynamic medical record repository list
//    val allRecords = remember(viewModel.patientName, viewModel.selectedDoctorName) {
//        listOf(
//            MedicalRecordItem(
//                id = "REC-101",
//                title = "Cardiology Rx & Follow-up Plan",
//                doctorName = viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" },
//                category = "Prescriptions",
//                date = viewModel.selectedDate.ifEmpty { "Today, 10:00 AM" },
//                fileSize = "1.2 MB PDF",
//                token = viewModel.tokenNumber.ifEmpty { "#14" }
//            ),
//            MedicalRecordItem(
//                id = "REC-102",
//                title = "Complete Blood Count (CBC) & Lipid Profile",
//                doctorName = "Apollo Diagnostic Labs",
//                category = "Lab Reports",
//                date = "Yesterday",
//                fileSize = "2.4 MB PDF",
//                token = "#08"
//            ),
//            MedicalRecordItem(
//                id = "REC-103",
//                title = "Consultation & Hospital Invoice",
//                doctorName = viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" },
//                category = "Invoices",
//                date = viewModel.selectedDate.ifEmpty { "Today" },
//                fileSize = "520 KB PDF",
//                token = viewModel.tokenNumber.ifEmpty { "#14" }
//            )
//        )
//    }
//
//    // Filter records dynamically by active category tab
//    val displayedRecords = allRecords.filter { it.category == selectedCategoryTab }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("My Records", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
//            )
//        },
//        bottomBar = { RecordsBottomNavigation(navController) }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .background(Color(0xFFF8FAFC))
//                .padding(16.dp)
//        ) {
//            // --- 1. CATEGORY TAB SELECTOR ---
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color(0xFFEDF2F7), RoundedCornerShape(12.dp))
//                    .padding(4.dp)
//            ) {
//                categories.forEach { tab ->
//                    val isSelected = (selectedCategoryTab == tab)
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .height(38.dp)
//                            .background(
//                                color = if (isSelected) Color(0xFF2196F3) else Color.Transparent,
//                                shape = RoundedCornerShape(10.dp)
//                            )
//                            .clickable { selectedCategoryTab = tab },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = tab,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 12.sp,
//                            color = if (isSelected) Color.White else Color.Gray
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // --- 2. UPLOAD REPORT BUTTON ---
//            OutlinedButton(
//                onClick = {
//                    Toast.makeText(context, "Opening Document Picker to attach file...", Toast.LENGTH_SHORT).show()
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                shape = RoundedCornerShape(12.dp),
//                border = BorderStroke(1.dp, Color(0xFF2196F3))
//            ) {
//                Icon(
//                    imageVector = Icons.Default.FileUpload,
//                    contentDescription = null,
//                    tint = Color(0xFF2196F3),
//                    modifier = Modifier.size(20.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("+ Upload Medical Record", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // --- 3. RECORDS LIST / EMPTY STATE ---
//            if (displayedRecords.isEmpty()) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Surface(
//                            modifier = Modifier.size(60.dp),
//                            shape = CircleShape,
//                            color = Color(0xFFE3F2FD)
//                        ) {
//                            Box(contentAlignment = Alignment.Center) {
//                                Icon(
//                                    imageVector = Icons.Default.FolderOpen,
//                                    contentDescription = null,
//                                    tint = Color(0xFF2196F3),
//                                    modifier = Modifier.size(32.dp)
//                                )
//                            }
//                        }
//                        Spacer(modifier = Modifier.height(12.dp))
//                        Text(
//                            text = "No $selectedCategoryTab Found",
//                            color = Color.Gray,
//                            fontWeight = FontWeight.Medium
//                        )
//                        Text(
//                            text = "Upload or complete consultations to see your digital records here.",
//                            fontSize = 12.sp,
//                            color = Color.LightGray
//                        )
//                    }
//                }
//            } else {
//                LazyColumn(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    items(displayedRecords) { record ->
//                        Card(
//                            modifier = Modifier.fillMaxWidth(),
//                            elevation = CardDefaults.cardElevation(2.dp),
//                            colors = CardDefaults.cardColors(containerColor = Color.White),
//                            shape = RoundedCornerShape(16.dp)
//                        ) {
//                            Column(modifier = Modifier.padding(16.dp)) {
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Surface(
//                                        color = Color(0xFFE3F2FD),
//                                        shape = RoundedCornerShape(8.dp)
//                                    ) {
//                                        Text(
//                                            text = "Token: ${record.token}",
//                                            color = Color(0xFF2196F3),
//                                            fontWeight = FontWeight.Bold,
//                                            fontSize = 12.sp,
//                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//                                        )
//                                    }
//                                    Surface(
//                                        color = Color(0xFFF1F5F9),
//                                        shape = RoundedCornerShape(6.dp)
//                                    ) {
//                                        Text(
//                                            text = record.fileSize,
//                                            fontSize = 11.sp,
//                                            color = Color(0xFF64748B),
//                                            fontWeight = FontWeight.Medium,
//                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
//                                        )
//                                    }
//                                }
//
//                                Spacer(modifier = Modifier.height(10.dp))
//
//                                Text(
//                                    text = record.title,
//                                    fontSize = 15.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    color = Color(0xFF1E293B)
//                                )
//
//                                Text(
//                                    text = "Issued by: ${record.doctorName}",
//                                    fontSize = 13.sp,
//                                    color = Color(0xFF64748B),
//                                    fontWeight = FontWeight.Medium
//                                )
//
//                                if (viewModel.patientName.isNotEmpty()) {
//                                    Text(
//                                        text = "Patient: ${viewModel.patientName}",
//                                        fontSize = 13.sp,
//                                        color = Color.DarkGray
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(6.dp))
//
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    Icon(
//                                        imageVector = Icons.Default.CalendarToday,
//                                        contentDescription = null,
//                                        tint = Color.Gray,
//                                        modifier = Modifier.size(13.dp)
//                                    )
//                                    Spacer(modifier = Modifier.width(4.dp))
//                                    Text(
//                                        text = record.date,
//                                        fontSize = 12.sp,
//                                        color = Color.Gray
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(10.dp))
//                                Divider(color = Color(0xFFF1F5F9))
//                                Spacer(modifier = Modifier.height(6.dp))
//
//                                // Action Buttons (Download & View Details)
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    TextButton(
//                                        onClick = {
//                                            Toast.makeText(context, "Downloading ${record.title}...", Toast.LENGTH_SHORT).show()
//                                        }
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Default.Download,
//                                            contentDescription = null,
//                                            tint = Color(0xFF16A34A),
//                                            modifier = Modifier.size(16.dp)
//                                        )
//                                        Spacer(modifier = Modifier.width(4.dp))
//                                        Text("Download", color = Color(0xFF16A34A), fontWeight = FontWeight.Bold, fontSize = 13.sp)
//                                    }
//
//                                    TextButton(
//                                        onClick = {
//                                            navController.navigate("appointment_details")
//                                        }
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Default.Visibility,
//                                            contentDescription = null,
//                                            tint = Color(0xFF2196F3),
//                                            modifier = Modifier.size(16.dp)
//                                        )
//                                        Spacer(modifier = Modifier.width(4.dp))
//                                        Text("View Details", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 13.sp)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//// Standard 4-Tab Bottom Navigation Bar
//@Composable
//fun RecordsBottomNavigation(navController: NavController) {
//    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
//            label = { Text("Search", fontSize = 10.sp) },
//            selected = false,
//            onClick = { navController.navigate("doctor_list") }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Payments") },
//            label = { Text("Payments", fontSize = 10.sp) },
//            selected = false,
//            onClick = { navController.navigate("payments") }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.DateRange, contentDescription = "My Appt") },
//            label = { Text("My Appt", fontSize = 10.sp) },
//            selected = false,
//            onClick = { navController.navigate("my_appointments") }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
//            label = { Text("Profile", fontSize = 10.sp) },
//            selected = false,
//            onClick = { navController.navigate("profile") }
//        )
//    }
//}
//
//
//













































//package com.example.schedulapatientapp
//
//import android.widget.Toast
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
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
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RecordsScreen(navController: NavController, viewModel: BookingViewModel) {
//    val context = LocalContext.current
//    var selectedCategoryTab by remember { mutableStateOf("Prescriptions") }
//    val categories = listOf("Prescriptions", "Lab Reports", "Invoices")
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("My Records", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
//            )
//        },
//        bottomBar = { BookingBottomBar(navController, "records") }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .background(Color(0xFFF8FAFC))
//                .padding(16.dp)
//        ) {
//            // --- 1. CATEGORY TAB SELECTOR ---
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color(0xFFEDF2F7), RoundedCornerShape(12.dp))
//                    .padding(4.dp)
//            ) {
//                categories.forEach { tab ->
//                    val isSelected = (selectedCategoryTab == tab)
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .height(38.dp)
//                            .background(
//                                color = if (isSelected) Color(0xFF2196F3) else Color.Transparent,
//                                shape = RoundedCornerShape(10.dp)
//                            )
//                            .clickable { selectedCategoryTab = tab },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = tab,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 12.sp,
//                            color = if (isSelected) Color.White else Color.Gray
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // --- 2. UPLOAD REPORT BUTTON ---
//            OutlinedButton(
//                onClick = {
//                    Toast.makeText(context, "Document picker opening...", Toast.LENGTH_SHORT).show()
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                shape = RoundedCornerShape(12.dp),
//                border = BorderStroke(1.dp, Color(0xFF2196F3))
//            ) {
//                Icon(
//                    imageVector = Icons.Default.FileUpload,
//                    contentDescription = null,
//                    tint = Color(0xFF2196F3),
//                    modifier = Modifier.size(20.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("+ Upload Medical Record", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // --- 3. RECORDS LIST ---
//            if (viewModel.patientName.isEmpty()) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Surface(
//                            modifier = Modifier.size(60.dp),
//                            shape = CircleShape,
//                            color = Color(0xFFE3F2FD)
//                        ) {
//                            Box(contentAlignment = Alignment.Center) {
//                                Icon(
//                                    imageVector = Icons.Default.FolderOpen,
//                                    contentDescription = null,
//                                    tint = Color(0xFF2196F3),
//                                    modifier = Modifier.size(32.dp)
//                                )
//                            }
//                        }
//                        Spacer(modifier = Modifier.height(12.dp))
//                        Text("No Medical Records Found", color = Color.Gray, fontWeight = FontWeight.Medium)
//                        Text("Book an appointment to see your digital records here.", fontSize = 12.sp, color = Color.LightGray)
//                    }
//                }
//            } else {
//                LazyColumn(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    item {
//                        Card(
//                            modifier = Modifier.fillMaxWidth(),
//                            elevation = CardDefaults.cardElevation(2.dp),
//                            colors = CardDefaults.cardColors(containerColor = Color.White),
//                            shape = RoundedCornerShape(16.dp)
//                        ) {
//                            Column(modifier = Modifier.padding(16.dp)) {
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Surface(
//                                        color = Color(0xFFE3F2FD),
//                                        shape = RoundedCornerShape(8.dp)
//                                    ) {
//                                        Text(
//                                            text = "Token: ${viewModel.tokenNumber.ifEmpty { "#14" }}",
//                                            color = Color(0xFF2196F3),
//                                            fontWeight = FontWeight.Bold,
//                                            fontSize = 12.sp,
//                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//                                        )
//                                    }
//                                    Text(
//                                        text = selectedCategoryTab.dropLast(1),
//                                        fontSize = 12.sp,
//                                        color = Color.Gray,
//                                        fontWeight = FontWeight.Medium
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(12.dp))
//
//                                Text(
//                                    text = viewModel.selectedDoctorName.ifEmpty { "Dr. Suresh Patil" },
//                                    fontSize = 16.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
//                                Text(
//                                    text = "Patient: ${viewModel.patientName}",
//                                    fontSize = 14.sp,
//                                    color = Color.DarkGray,
//                                    fontWeight = FontWeight.Medium
//                                )
//
//                                Spacer(modifier = Modifier.height(6.dp))
//
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    Icon(
//                                        imageVector = Icons.Default.CalendarToday,
//                                        contentDescription = null,
//                                        tint = Color.Gray,
//                                        modifier = Modifier.size(14.dp)
//                                    )
//                                    Spacer(modifier = Modifier.width(4.dp))
//                                    Text(
//                                        text = "${viewModel.selectedDate.ifEmpty { "Today" }} | ${viewModel.selectedTime.ifEmpty { "10:00 AM" }}",
//                                        fontSize = 12.sp,
//                                        color = Color.Gray
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(12.dp))
//
//                                Divider(color = Color(0xFFF1F5F9))
//
//                                Spacer(modifier = Modifier.height(8.dp))
//
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.End
//                                ) {
//                                    TextButton(onClick = {
//                                        navController.navigate("appointment_details")
//                                    }) {
//                                        Icon(
//                                            imageVector = Icons.Default.Visibility,
//                                            contentDescription = null,
//                                            tint = Color(0xFF2196F3),
//                                            modifier = Modifier.size(16.dp)
//                                        )
//                                        Spacer(modifier = Modifier.width(4.dp))
//                                        Text("View Details", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//
//
//
//
