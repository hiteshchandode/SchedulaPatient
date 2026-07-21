package com.example.schedulapatientapp


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class FamilyMember(
    val name: String,
    val gender: String,
    val age: Int,
    val relationship: String,
    val relationColor: Color,
    val relationBg: Color,
    val avatarBg: Color,
    val avatarIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsAndFamilyScreen(navController: NavController) {
    val membersList = remember {
        listOf(
            FamilyMember(
                name = "Muthukumar",
                gender = "Male",
                age = 28,
                relationship = "Self",
                relationColor = Color(0xFF2563EB),
                relationBg = Color(0xFFEFF6FF),
                avatarBg = Color(0xFFEFF6FF),
                avatarIcon = Icons.Default.Person
            ),
            FamilyMember(
                name = "Meena",
                gender = "Female",
                age = 26,
                relationship = "Wife",
                relationColor = Color(0xFFDB2777),
                relationBg = Color(0xFFFCE7F3),
                avatarBg = Color(0xFFFCE7F3),
                avatarIcon = Icons.Default.Person
            ),
            FamilyMember(
                name = "Kishore",
                gender = "Male",
                age = 2,
                relationship = "Son",
                relationColor = Color(0xFF16A34A),
                relationBg = Color(0xFFDCFCE7),
                avatarBg = Color(0xFFDCFCE7),
                avatarIcon = Icons.Default.ChildCare
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Friends & Family", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1E293B))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(16.dp), tint = Color(0xFF1E293B))
                    }
                },
                actions = {
                    IconButton(
                        onClick = { },
                        modifier = Modifier.padding(end = 8.dp).size(36.dp).background(Color(0xFF2196F3), CircleShape)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Add Contact", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.PersonSearch, null) }, label = { Text("Find a Doctor", fontSize = 10.sp) }, selected = false, onClick = { })
                NavigationBarItem(icon = { Icon(Icons.Default.Folder, null) }, label = { Text("My Records", fontSize = 10.sp) }, selected = false, onClick = { })
                NavigationBarItem(icon = { Icon(Icons.Outlined.CalendarMonth, null) }, label = { Text("My Appt", fontSize = 10.sp) }, selected = true, onClick = { })
                NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile", fontSize = 10.sp) }, selected = false, onClick = { })
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF1F5F9))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Box(modifier = Modifier.size(42.dp).background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Group, null, tint = Color(0xFF3B82F6), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Manage Care Seekers", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Add family members or dependents for faster appointment booking and healthcare management.", fontSize = 12.sp, color = Color(0xFF64748B), lineHeight = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "${membersList.size} FAMILY MEMBERS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), modifier = Modifier.padding(bottom = 12.dp))

                    membersList.forEachIndexed { index, member ->
                        // NavController passes down perfectly here now
                        FamilyMemberRowItem(member = member, navController = navController)
                        if (index < membersList.lastIndex) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FamilyMemberRowItem(member: FamilyMember, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(member.avatarBg, CircleShape), contentAlignment = Alignment.Center) {
                Icon(member.avatarIcon, null, tint = member.relationColor, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = member.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                Text(text = "${member.gender} • ${member.age} Years", fontSize = 12.sp, color = Color(0xFF64748B), modifier = Modifier.padding(vertical = 2.dp))
                Surface(color = member.relationBg, shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = when (member.relationship) {
                            "Self" -> "👤 Self"
                            "Wife" -> "💗 Wife"
                            else -> "👶 Son"
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = member.relationColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Button(
                    onClick = { navController.navigate("google_review") },
                    modifier = Modifier.width(120.dp).height(34.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Icon(Icons.Outlined.CalendarMonth, null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Appointment", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.width(120.dp).height(34.dp),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF2196F3)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Icon(Icons.Default.MailOutline, null, tint = Color(0xFF2196F3), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Invite", color = Color(0xFF2196F3), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}






























//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBackIosNew
//import androidx.compose.material.icons.filled.ChildCare
//import androidx.compose.material.icons.filled.Folder
//import androidx.compose.material.icons.filled.Group
//import androidx.compose.material.icons.filled.MailOutline
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.PersonAdd
//import androidx.compose.material.icons.filled.PersonSearch
//import androidx.compose.material.icons.outlined.CalendarMonth
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//
//// Structured Data Model for Family Members
//data class FamilyMember(
//    val name: String,
//    val gender: String,
//    val age: Int,
//    val relationship: String, // "Self", "Wife", "Son"
//    val relationColor: Color,
//    val relationBg: Color,
//    val avatarBg: Color,
//    val avatarIcon: ImageVector
//)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FriendsAndFamilyScreen(navController: NavController) {
//    // Exact data structures mapped from the wireframe / screenshot parameters
//    val membersList = remember {
//        listOf(
//            FamilyMember(
//                name = "Muthukumar",
//                gender = "Male",
//                age = 28,
//                relationship = "Self",
//                relationColor = Color(0xFF2563EB),
//                relationBg = Color(0xFFEFF6FF),
//                avatarBg = Color(0xFFEFF6FF),
//                avatarIcon = Icons.Default.Person
//            ),
//            FamilyMember(
//                name = "Meena",
//                gender = "Female",
//                age = 26,
//                relationship = "Wife",
//                relationColor = Color(0xFFDB2777),
//                relationBg = Color(0xFFFCE7F3),
//                avatarBg = Color(0xFFFCE7F3),
//                avatarIcon = Icons.Default.Person
//            ),
//            FamilyMember(
//                name = "Kishore",
//                gender = "Male",
//                age = 2,
//                relationship = "Son",
//                relationColor = Color(0xFF16A34A),
//                relationBg = Color(0xFFDCFCE7),
//                avatarBg = Color(0xFFDCFCE7),
//                avatarIcon = Icons.Default.ChildCare
//            )
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Box(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            "Friends & Family",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 20.sp,
//                            color = Color(0xFF1E293B)
//                        )
//                    }
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            Icons.Default.ArrowBackIosNew,
//                            contentDescription = "Back",
//                            modifier = Modifier.size(16.dp),
//                            tint = Color(0xFF1E293B)
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(
//                        onClick = { /* Handle add new care seeker functionality */ },
//                        modifier = Modifier
//                            .padding(end = 8.dp)
//                            .size(36.dp)
//                            .background(Color(0xFF2196F3), CircleShape)
//                    ) {
//                        Icon(
//                            Icons.Default.PersonAdd,
//                            contentDescription = "Add Contact",
//                            tint = Color.White,
//                            modifier = Modifier.size(16.dp)
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
//            )
//        },
//        bottomBar = { FamilyBottomBar(navController) }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(Color(0xFFF1F5F9))
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            // --- 1. MANAGE CARE SEEKERS TOP BANNER ---
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
//            ) {
//                Row(
//                    modifier = Modifier.padding(16.dp),
//                    verticalAlignment = Alignment.Top
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(42.dp)
//                            .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(Icons.Default.Group, null, tint = Color(0xFF3B82F6), modifier = Modifier.size(20.dp))
//                    }
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Column {
//                        Text(
//                            "Manage Care Seekers",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 15.sp,
//                            color = Color(0xFF1E293B)
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Text(
//                            "Add family members or dependents for faster appointment booking and healthcare management.",
//                            fontSize = 12.sp,
//                            color = Color(0xFF64748B),
//                            lineHeight = 16.sp
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // --- 2. LIST WRAPPER CONTAINER CARD ---
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White)
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = "${membersList.size} FAMILY MEMBERS",
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF94A3B8),
//                        modifier = Modifier.padding(bottom = 12.dp)
//                    )
//
//                    // Render List Iteration Loops Safely
//                    membersList.forEachIndexed { index, member ->
//                        FamilyMemberRowItem(member = member)
//                        if (index < membersList.lastIndex) {
//                            Spacer(modifier = Modifier.height(12.dp))
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun FamilyMemberRowItem(member: FamilyMember) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Dynamic Custom Colored Avatar Image Circle Container
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .background(member.avatarBg, CircleShape),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(member.avatarIcon, null, tint = member.relationColor, modifier = Modifier.size(22.dp))
//            }
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            // Text Metadata Column Block
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = member.name,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp,
//                    color = Color(0xFF1E293B)
//                )
//                Text(
//                    text = "${member.gender} • ${member.age} Years",
//                    fontSize = 12.sp,
//                    color = Color(0xFF64748B),
//                    modifier = Modifier.padding(vertical = 2.dp)
//                )
//                Surface(
//                    color = member.relationBg,
//                    shape = RoundedCornerShape(6.dp)
//                ) {
//                    Text(
//                        text = when (member.relationship) {
//                            "Self" -> "👤 Self"
//                            "Wife" -> "💗 Wife"
//                            else -> "👶 Son"
//                        },
//                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
//                        color = member.relationColor,
//                        fontSize = 11.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            // Actions Buttons Control Right Side Stack Column
//            Column(horizontalAlignment = Alignment.End) {
//                Button(
//                    onClick = { /* Handle appointment booking action */ },
//                    modifier = Modifier
//                        .width(120.dp)
//                        .height(34.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
//                    shape = RoundedCornerShape(6.dp),
//                    contentPadding = PaddingValues(0.dp)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        Icon(Icons.Outlined.CalendarMonth, null, tint = Color.White, modifier = Modifier.size(14.dp))
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text("Appointment", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                OutlinedButton(
//                    onClick = { /* Handle team contact invite action */ },
//                    modifier = Modifier
//                        .width(120.dp)
//                        .height(34.dp),
//                    shape = RoundedCornerShape(6.dp),
//                    border = BorderStroke(1.dp, Color(0xFF2196F3)),
//                    contentPadding = PaddingValues(0.dp)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        Icon(Icons.Default.MailOutline, null, tint = Color(0xFF2196F3), modifier = Modifier.size(14.dp))
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text("Invite", color = Color(0xFF2196F3), fontSize = 12.sp, fontWeight = FontWeight.Bold)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun FamilyBottomBar(navController: NavController) {
//    NavigationBar(containerColor = Color.White) {
//        NavigationBarItem(icon = { Icon(Icons.Default.PersonSearch, null) }, label = { Text("Find a Doctor", fontSize = 10.sp) }, selected = false, onClick = { })
//        NavigationBarItem(icon = { Icon(Icons.Default.Folder, null) }, label = { Text("My Records", fontSize = 10.sp) }, selected = false, onClick = { })
//        NavigationBarItem(icon = { Icon(Icons.Outlined.CalendarMonth, null) }, label = { Text("My Appt", fontSize = 10.sp) }, selected = true, onClick = { })
//        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile", fontSize = 10.sp) }, selected = false, onClick = { })
//    }
//}