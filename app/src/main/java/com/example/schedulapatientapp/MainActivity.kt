package com.example.schedulapatientapp

import androidx.compose.ui.graphics.Color
import com.example.schedulapatientapp.SearchScreen
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
import androidx.compose.runtime.internal.composableLambda
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
                        composable("doctor_list") {
                            SearchScreen(navController)
                        }

                        //Screen 3: Doctor profile
                        composable("doctor_profile") {
                            // We pass the navController so we can use the "Back" button later
                            DoctorProfileScreen(navController = navController)

                        }

                    }


                    }
                }
            }
        }
    }


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        SchedulaPatientAppTheme {
            Greeting("Android")
        }
    }

