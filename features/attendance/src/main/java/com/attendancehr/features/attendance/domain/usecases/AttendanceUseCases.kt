package com.attendancehr.features.attendance.domain.usecases

import com.attendancehr.core.common.result.Result
import com.attendancehr.features.attendance.domain.entities.AttendanceRecord
import com.attendancehr.features.attendance.domain.repositories.AttendanceRepository
import javax.inject.Inject

class GetAttendanceRecordsUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
) {
    suspend operator fun invoke(employeeId: String): Result<List<AttendanceRecord>> {
        return try {
            val records = attendanceRepository.getAttendanceRecords(employeeId)
            Result.Success(records)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

class CheckInUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
) {
    suspend operator fun invoke(employeeId: String, latitude: Double, longitude: Double): Result<AttendanceRecord> {
        return try {
            val record = attendanceRepository.checkIn(employeeId, latitude, longitude)
            Result.Success(record)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
