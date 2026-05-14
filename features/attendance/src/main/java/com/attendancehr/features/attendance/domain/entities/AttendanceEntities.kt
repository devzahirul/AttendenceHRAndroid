package com.attendancehr.features.attendance.domain.entities

import java.time.LocalDateTime

data class AttendanceRecord(
    val id: String,
    val employeeId: String,
    val checkInTime: LocalDateTime,
    val checkOutTime: LocalDateTime? = null,
    val checkInLatitude: Double,
    val checkInLongitude: Double,
    val checkOutLatitude: Double? = null,
    val checkOutLongitude: Double? = null,
    val status: AttendanceStatus = AttendanceStatus.PRESENT,
)

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    LATE,
    ON_LEAVE,
    HALF_DAY,
}

data class AttendanceSummary(
    val totalPresent: Int,
    val totalAbsent: Int,
    val totalLate: Int,
    val totalOnLeave: Int,
    val attendancePercentage: Double,
)
