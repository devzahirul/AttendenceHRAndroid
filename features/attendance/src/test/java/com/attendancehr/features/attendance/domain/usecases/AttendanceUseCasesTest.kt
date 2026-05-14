package com.attendancehr.features.attendance.domain.usecases

import com.attendancehr.core.common.result.Result
import com.attendancehr.features.attendance.domain.entities.AttendanceRecord
import com.attendancehr.features.attendance.domain.entities.AttendanceStatus
import com.attendancehr.features.attendance.domain.repositories.AttendanceRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAttendanceRecordsUseCaseTest {
    private val attendanceRepository = mockk<AttendanceRepository>()
    private val useCase = GetAttendanceRecordsUseCase(attendanceRepository)

    @Test
    fun `invoke returns success when repository returns records`() = runTest {
        // Arrange
        val employeeId = "EMP001"
        val expectedRecords = listOf(
            AttendanceRecord(
                id = "1",
                employeeId = employeeId,
                checkInTime = LocalDateTime.now(),
                checkInLatitude = 37.7749,
                checkInLongitude = -122.4194,
                status = AttendanceStatus.PRESENT,
            ),
            AttendanceRecord(
                id = "2",
                employeeId = employeeId,
                checkInTime = LocalDateTime.now().minusDays(1),
                checkInLatitude = 37.7749,
                checkInLongitude = -122.4194,
                status = AttendanceStatus.PRESENT,
            ),
        )
        coEvery { attendanceRepository.getAttendanceRecords(employeeId) } returns expectedRecords

        // Act
        val result = useCase(employeeId)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedRecords, (result as Result.Success).data)
    }

    @Test
    fun `invoke returns error when repository throws exception`() = runTest {
        // Arrange
        val employeeId = "EMP001"
        val expectedException = Exception("Network error")
        coEvery { attendanceRepository.getAttendanceRecords(employeeId) } throws expectedException

        // Act
        val result = useCase(employeeId)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(expectedException, (result as Result.Error).exception)
    }

    @Test
    fun `invoke returns empty list when employee has no records`() = runTest {
        // Arrange
        val employeeId = "EMP001"
        coEvery { attendanceRepository.getAttendanceRecords(employeeId) } returns emptyList()

        // Act
        val result = useCase(employeeId)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(emptyList(), (result as Result.Success).data)
    }
}

class CheckInUseCaseTest {
    private val attendanceRepository = mockk<AttendanceRepository>()
    private val useCase = CheckInUseCase(attendanceRepository)

    @Test
    fun `invoke returns success when check-in succeeds`() = runTest {
        // Arrange
        val employeeId = "EMP001"
        val latitude = 37.7749
        val longitude = -122.4194
        val expectedRecord = AttendanceRecord(
            id = "1",
            employeeId = employeeId,
            checkInTime = LocalDateTime.now(),
            checkInLatitude = latitude,
            checkInLongitude = longitude,
            status = AttendanceStatus.PRESENT,
        )
        coEvery { attendanceRepository.checkIn(employeeId, latitude, longitude) } returns expectedRecord

        // Act
        val result = useCase(employeeId, latitude, longitude)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedRecord, (result as Result.Success).data)
    }

    @Test
    fun `invoke returns error when check-in fails`() = runTest {
        // Arrange
        val employeeId = "EMP001"
        val latitude = 37.7749
        val longitude = -122.4194
        val expectedException = Exception("Location services unavailable")
        coEvery { attendanceRepository.checkIn(employeeId, latitude, longitude) } throws expectedException

        // Act
        val result = useCase(employeeId, latitude, longitude)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(expectedException, (result as Result.Error).exception)
    }
}
