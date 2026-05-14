package com.attendancehr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.attendancehr.core.ui.theme.AttendanceHRTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendanceHRTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "auth",
                ) {
                    // Auth navigation graph
                    // Dashboard navigation graph
                    // Attendance navigation graph
                    // Employee navigation graph
                    // Settings navigation graph
                }
            }
        }
    }
}
