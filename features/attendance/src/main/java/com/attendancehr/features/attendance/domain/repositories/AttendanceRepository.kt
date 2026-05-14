package com.attendancehr.features.attendance.domain.repositories

import com.attendancehr.features.attendance.domain.entities.AttendanceRecord
import com.attendancehr.features.attendance.domain.entities.AttendanceSummary

interface AttendanceRepository {
    suspend fun checkIn(
        employeeId: String,
        latitude: Double,
        longitude: Double,
    ): AttendanceRecord

    suspend fun checkOut(
        employeeId: String,
        latitude: Double,
        longitude: Double,
    ): AttendanceRecord

    suspend fun getAttendanceRecords(employeeId: String): List<AttendanceRecord>

    suspend fun getAttendanceSummary(employeeId: String): AttendanceSummary

    suspend fun getMonthlyAttendance(employeeId: String, year: Int, month: Int): List<AttendanceRecord>
}
