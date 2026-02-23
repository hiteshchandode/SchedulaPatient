package com.example.schedulapatientapp

import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.schedulapatientapp.ui.theme.SchedulaPatientAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Your App Theme wrapper
            SchedulaPatientAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ONE Navigation Controller to rule them all
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {

                        // Screen 1: LOGIN
                        composable("login") {
                            // Ensure your LoginScreen function accepts 'navController'
                            LoginScreen(navController = navController)
                        }

                        // Screen 2: SEARCH (The design you uploaded)
                        composable("search") {
                            SearchDoctorScreen()
                        }
                    }
                }
            }
        }
    }
}

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {setContent {
//            // Replace 'SchedulaTheme' with whatever your project named your theme
//            SchedulaPatientAppTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    LoginScreen()
//                }
//            }
//        }
//            SchedulaPatientAppTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Composable
//fun MyAppNavigation() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "login") {
//        composable("login") {
//            // Pass the navController to your Login Screen
//            LoginScreen(onLoginSuccess = { navController.navigate("search") })
//        }
//        composable("search") {
//            DoctorSearchScreen()
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SchedulaPatientAppTheme {
        Greeting("Android")
    }
}