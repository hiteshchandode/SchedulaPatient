package com.example.schedulapatientapp

    import androidx.compose.material3.Text
    import androidx.navigation.NavHostController
    import androidx.navigation.compose.rememberNavController
    import androidx.compose.foundation.BorderStroke
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.text.KeyboardOptions
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
    import androidx.compose.ui.text.withStyle
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp


    // 1. Theme Colors based on your reference image
    val BrandBlue = Color(0xFF2196F3)
    val ActionOrange = Color(0xFFFF9800)
    val BackgroundLightBlue = Color(0xFFF8FBFF)

    @Composable
    fun LoginScreen(navController: NavHostController) {
        // State variables for user input
        var phoneNumber by remember { mutableStateOf("") }
        var rememberMe by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLightBlue)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // 2. Logo Holder (Icon centered in a soft-corner square)
            Surface(
                modifier = Modifier.size(90.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // This uses a default Android heart icon.
                    // Replace with R.drawable.ic_hospital_logo once you add your file.
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hospital_logo2),
                        contentDescription = "Hospital Logo",
                        tint = BrandBlue,
                        modifier = Modifier.size(45.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Title with Mixed Colors
            Text(
                text = buildAnnotatedString {
                    append("Schedula - ")
                    withStyle(style = SpanStyle(color = BrandBlue, fontWeight = FontWeight.Bold)) {
                        append("Patient")
                    }
                },
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            Text(
                text = "Your health, scheduled simply.",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 4. Input Field Section
            Text(
                text = "Mobile Number",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("+91 00000-00000", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = BrandBlue,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            // 5. Remember Me and Help Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = BrandBlue)
                    )
                    Text("Remember me", fontSize = 13.sp, color = Color.Gray)
                }
                TextButton(onClick = { /* Help logic */ }) {
                    Text("Need help?", color = BrandBlue, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 6. Primary Action Button
            Button(
                onClick = {
                    // We removed the extra ) and the extra { } from here
                    navController.navigate("search")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ActionOrange),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                // This is the only place where the UI (Text/Row) should live
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    // Simple arrow icon
                    Text("→", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

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
