package com.attendancehr

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AttendanceHRApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize app-level dependencies
    }
}
