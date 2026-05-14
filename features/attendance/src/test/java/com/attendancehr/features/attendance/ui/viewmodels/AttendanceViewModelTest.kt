package com.attendancehr.features.attendance.ui.viewmodels

import app.cash.turbine.test
import com.attendancehr.core.common.result.Result
import com.attendancehr.features.attendance.domain.entities.AttendanceRecord
import com.attendancehr.features.attendance.domain.entities.AttendanceStatus
import com.attendancehr.features.attendance.domain.usecases.CheckInUseCase
import com.attendancehr.features.attendance.domain.usecases.GetAttendanceRecordsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AttendanceViewModelTest {

    private val getAttendanceRecordsUseCase = mockk<GetAttendanceRecordsUseCase>()
    private val checkInUseCase = mockk<CheckInUseCase>()
    private lateinit var viewModel: AttendanceViewModel

    @Test
    fun `loadAttendanceRecords emits Loading then Success when use case returns success`() = runTest {
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
        )
        coEvery { getAttendanceRecordsUseCase(employeeId) } returns Result.Success(expectedRecords)

        viewModel = AttendanceViewModel(getAttendanceRecordsUseCase, checkInUseCase)

        // Act & Assert
        viewModel.uiState.test {
            // Initial state is Loading
            assertIs<AttendanceUiState.Loading>(awaitItem())

            viewModel.loadAttendanceRecords(employeeId)

            // Should emit Loading first
            assertIs<AttendanceUiState.Loading>(awaitItem())

            // Then should emit Success with the records
            val successState = awaitItem()
            assertIs<AttendanceUiState.Success>(successState)
            assertEquals(expectedRecords, successState.records)
        }
    }

    @Test
    fun `loadAttendanceRecords emits Loading then Error when use case returns error`() = runTest {
        // Arrange
        val employeeId = "EMP001"
        val errorMessage = "Network error"
        val exception = Exception(errorMessage)
        coEvery { getAttendanceRecordsUseCase(employeeId) } returns Result.Error(exception)

        viewModel = AttendanceViewModel(getAttendanceRecordsUseCase, checkInUseCase)

        // Act & Assert
        viewModel.uiState.test {
            // Initial state is Loading
            assertIs<AttendanceUiState.Loading>(awaitItem())

            viewModel.loadAttendanceRecords(employeeId)

            // Should emit Loading first
            assertIs<AttendanceUiState.Loading>(awaitItem())

            // Then should emit Error with the error message
            val errorState = awaitItem()
            assertIs<AttendanceUiState.Error>(errorState)
            assertEquals(errorMessage, errorState.message)
        }
    }

    @Test
    fun `checkIn emits Loading then CheckInSuccess when use case returns success`() = runTest {
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
        coEvery { checkInUseCase(employeeId, latitude, longitude) } returns Result.Success(expectedRecord)

        viewModel = AttendanceViewModel(getAttendanceRecordsUseCase, checkInUseCase)

        // Act & Assert
        viewModel.uiState.test {
            // Initial state is Loading
            assertIs<AttendanceUiState.Loading>(awaitItem())

            viewModel.checkIn(employeeId, latitude, longitude)

            // Should emit Loading first
            assertIs<AttendanceUiState.Loading>(awaitItem())

            // Then should emit CheckInSuccess with the record
            val successState = awaitItem()
            assertIs<AttendanceUiState.CheckInSuccess>(successState)
            assertEquals(expectedRecord, successState.record)
        }
    }

    @Test
    fun `checkIn emits Loading then Error when use case returns error`() = runTest {
        // Arrange
        val employeeId = "EMP001"
        val latitude = 37.7749
        val longitude = -122.4194
        val errorMessage = "Location services unavailable"
        val exception = Exception(errorMessage)
        coEvery { checkInUseCase(employeeId, latitude, longitude) } returns Result.Error(exception)

        viewModel = AttendanceViewModel(getAttendanceRecordsUseCase, checkInUseCase)

        // Act & Assert
        viewModel.uiState.test {
            // Initial state is Loading
            assertIs<AttendanceUiState.Loading>(awaitItem())

            viewModel.checkIn(employeeId, latitude, longitude)

            // Should emit Loading first
            assertIs<AttendanceUiState.Loading>(awaitItem())

            // Then should emit Error with the error message
            val errorState = awaitItem()
            assertIs<AttendanceUiState.Error>(errorState)
            assertEquals("Location services unavailable", errorState.message)
        }
    }

    @Test
    fun `checkIn uses custom error message when exception has no message`() = runTest {
        // Arrange
        val employeeId = "EMP001"
        val latitude = 37.7749
        val longitude = -122.4194
        val exception = Exception()
        coEvery { checkInUseCase(employeeId, latitude, longitude) } returns Result.Error(exception)

        viewModel = AttendanceViewModel(getAttendanceRecordsUseCase, checkInUseCase)

        // Act & Assert
        viewModel.uiState.test {
            assertIs<AttendanceUiState.Loading>(awaitItem())

            viewModel.checkIn(employeeId, latitude, longitude)

            assertIs<AttendanceUiState.Loading>(awaitItem())

            val errorState = awaitItem()
            assertIs<AttendanceUiState.Error>(errorState)
            assertEquals("Check-in failed", errorState.message)
        }
    }

    @Test
    fun `loadAttendanceRecords uses custom error message when exception has no message`() = runTest {
        // Arrange
        val employeeId = "EMP001"
        val exception = Exception()
        coEvery { getAttendanceRecordsUseCase(employeeId) } returns Result.Error(exception)

        viewModel = AttendanceViewModel(getAttendanceRecordsUseCase, checkInUseCase)

        // Act & Assert
        viewModel.uiState.test {
            assertIs<AttendanceUiState.Loading>(awaitItem())

            viewModel.loadAttendanceRecords(employeeId)

            assertIs<AttendanceUiState.Loading>(awaitItem())

            val errorState = awaitItem()
            assertIs<AttendanceUiState.Error>(errorState)
            assertEquals("Unknown error", errorState.message)
        }
    }
}
