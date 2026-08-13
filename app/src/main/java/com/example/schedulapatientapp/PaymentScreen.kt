package com.example.schedulapatientapp


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.schedulapatientapp.database.AppointmentEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    // Collect appointments dynamically from Room DB
    val appointmentsFromRoom by mainViewModel.appointmentsList.collectAsState()

    // Search query state
    var searchQuery by remember { mutableStateOf("") }

    // Filter transactions dynamically
    val filteredAppointments = appointmentsFromRoom.filter { appt ->
        searchQuery.isBlank() ||
                appt.doctorName.contains(searchQuery, ignoreCase = true) ||
                appt.patientName.contains(searchQuery, ignoreCase = true) ||
                appt.paymentStatus.contains(searchQuery, ignoreCase = true) ||
                appt.status.contains(searchQuery, ignoreCase = true)
    }

    // Financial metrics calculations
    val totalPaid = appointmentsFromRoom.filter { it.paymentStatus == "Paid" || it.paymentMode == "Online" }.size * 500
    val totalPending = appointmentsFromRoom.filter { it.paymentStatus == "Pending" && it.status != "Cancelled" }.size * 500
    val totalRefunds = appointmentsFromRoom.filter { it.status == "Cancelled" }.size * 500

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Payments & Invoices", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { PaymentsBottomNavigation(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // TRANSACTION SEARCH BAR
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                tonalElevation = 1.dp,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (searchQuery.isEmpty()) {
                            Text("Search payments (Doctor, Patient, Paid...)", color = Color.LightGray, fontSize = 13.sp)
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = Color(0xFF1E293B)),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // SUMMARY METRICS CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("TOTAL PAID", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text("₹$totalPaid", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Divider(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp),
                        color = Color(0xFF334155)
                    )
                    Column {
                        Text("PENDING DUES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text("₹$totalPending", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
                    }
                    Divider(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp),
                        color = Color(0xFF334155)
                    )
                    Column {
                        Text("REFUNDS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text("₹$totalRefunds", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF38BDF8))
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text("Transaction History", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))

            Spacer(modifier = Modifier.height(10.dp))

            // TRANSACTIONS LIST
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredAppointments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchQuery.isBlank()) "No payment transactions recorded yet." else "No transactions match '$searchQuery'",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(filteredAppointments) { appointment ->
                        PaymentTransactionCard(appointment = appointment)
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun PaymentTransactionCard(appointment: AppointmentEntity) {
    val isCancelled = appointment.status == "Cancelled"
    val isPaid = appointment.paymentStatus == "Paid" || appointment.paymentMode == "Online"

    val badgeText = when {
        isCancelled -> "Refund Initiated"
        isPaid -> "Paid (Online)"
        else -> "Pending (Pay at Counter)"
    }

    val badgeBgColor = when {
        isCancelled -> Color(0xFFFEF3C7)
        isPaid -> Color(0xFFDCFCE7)
        else -> Color(0xFFE0F2FE)
    }

    val badgeTextColor = when {
        isCancelled -> Color(0xFFD97706)
        isPaid -> Color(0xFF166534)
        else -> Color(0xFF0369A1)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(42.dp),
                        shape = CircleShape,
                        color = Color(0xFFF1F5F9)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isCancelled) Icons.Default.CurrencyExchange else Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = if (isCancelled) Color(0xFFD97706) else Color(0xFF2196F3),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(appointment.doctorName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                        Text("${appointment.date} • Consultation Fee", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Text(
                    text = if (isCancelled) "+₹500" else "₹500",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isCancelled) Color(0xFF2563EB) else Color(0xFF1E293B)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = badgeBgColor,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = badgeTextColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "Patient: ${appointment.patientName}",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PaymentsBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("doctor_list") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, null) },
            label = { Text("Payments", fontSize = 10.sp) },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2196F3),
                selectedTextColor = Color(0xFF2196F3)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, null) },
            label = { Text("My Appt", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("my_appointments") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile", fontSize = 10.sp) },
            selected = false,
            onClick = { navController.navigate("profile") }
        )
    }
}