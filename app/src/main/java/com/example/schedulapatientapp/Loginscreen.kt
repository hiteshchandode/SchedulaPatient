package com.example.schedulapatientapp


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// Theme Colors
val BrandBlue = Color(0xFF2196F3)
val ActionOrange = Color(0xFFFF9800)
val BackgroundLightBlue = Color(0xFFF8FBFF)


@Composable
fun LoginScreen(navController: NavHostController) {
    var phoneNumber by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBFF)) // Soft blue background from image
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // 1. LOGO CARD
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_hospital_logo),
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 2. TEXT HEADERS
        Text(
            text = buildAnnotatedString {
                append("Schedula")
                withStyle(style = SpanStyle(color = Color(0xFF2196F3))) {
                    append(" - Patient")
                }
            },
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Your health, scheduled simply.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 3. INPUT FIELD
        Text(
            text = "Mobile Number",
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 10) phoneNumber = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("+91 00000-00000", color = Color.LightGray) },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
        )

        // 4. REMEMBER ME & NEED HELP
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                Text("Remember me", fontSize = 14.sp, color = Color.Gray)
            }
            TextButton(onClick = { }) {
                Text("Need help?", color = Color(0xFF2196F3), fontSize = 14.sp)
            }
        }

        // 5. CONTINUE BUTTON
        Button(
            onClick = {navController.navigate("doctor_list") },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)), // Orange from image
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continue  →", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 6. ALTERNATIVE ACCESS SECTION
        Text("ALTERNATIVE ACCESS", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Google Button
            OutlinedButton(
                onClick = {navController.navigate("doctor_list")},
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Text("G Google", color = Color.DarkGray)
            }
            // Apple Button
            OutlinedButton(
                onClick = {navController.navigate("doctor_list")},
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Text(" Apple", color = Color.DarkGray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 7. FOOTER TEXT
        Text(
            text = "By continuing, you agree to our Terms of Service and Privacy Policy.",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}






    @Composable
    fun SocialButton(text: String, modifier: Modifier) {
        OutlinedButton(
            onClick = { },
            modifier = modifier.height(50.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Text(text, color = Color.DarkGray, fontWeight = FontWeight.Medium)
        }
    }
