package com.attendancehr.features.attendance.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.attendancehr.features.attendance.domain.entities.AttendanceRecord
import com.attendancehr.features.attendance.domain.entities.AttendanceStatus
import com.attendancehr.features.attendance.ui.viewmodels.AttendanceUiState
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class AttendanceScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_displaysLoadingIndicatorAndMessage() {
        // Arrange
        val state = AttendanceUiState.Loading

        // Act
        composeTestRule.setContent {
            MaterialTheme {
                AttendanceContent(
                    state = state,
                    onCheckIn = { _, _ -> },
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Loading attendance records...").assertIsDisplayed()
    }

    @Test
    fun errorState_displaysErrorMessage() {
        // Arrange
        val errorMessage = "Failed to load attendance records"
        val state = AttendanceUiState.Error(errorMessage)

        // Act
        composeTestRule.setContent {
            MaterialTheme {
                AttendanceContent(
                    state = state,
                    onCheckIn = { _, _ -> },
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun successState_displaysCheckInButtonAndRecordsList() {
        // Arrange
        val records = listOf(
            AttendanceRecord(
                id = "1",
                employeeId = "EMP001",
                checkInTime = LocalDateTime.now(),
                checkInLatitude = 37.7749,
                checkInLongitude = -122.4194,
                status = AttendanceStatus.PRESENT,
            ),
        )
        val state = AttendanceUiState.Success(records)
        var checkInCalled = false
        var capturedLat: Double? = null
        var capturedLon: Double? = null

        // Act
        composeTestRule.setContent {
            MaterialTheme {
                AttendanceContent(
                    state = state,
                    onCheckIn = { lat, lon ->
                        checkInCalled = true
                        capturedLat = lat
                        capturedLon = lon
                    },
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Check In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Attendance Records").assertIsDisplayed()

        // Verify check-in callback
        composeTestRule.onNodeWithText("Check In").performClick()
        assert(checkInCalled)
        assert(capturedLat == 37.7749)
        assert(capturedLon == -122.4194)
    }

    @Test
    fun checkInSuccessState_displaysSuccessMessageAndTime() {
        // Arrange
        val record = AttendanceRecord(
            id = "1",
            employeeId = "EMP001",
            checkInTime = LocalDateTime.of(2024, 1, 15, 9, 0),
            checkInLatitude = 37.7749,
            checkInLongitude = -122.4194,
            status = AttendanceStatus.PRESENT,
        )
        val state = AttendanceUiState.CheckInSuccess(record)

        // Act
        composeTestRule.setContent {
            MaterialTheme {
                AttendanceContent(
                    state = state,
                    onCheckIn = { _, _ -> },
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("✓ Check-in Successful").assertIsDisplayed()
        composeTestRule.onNodeWithText("Time: 09:00").assertIsDisplayed()
    }

    @Test
    fun attendanceRecordCard_displaysRecordDetails() {
        // Arrange
        val record = AttendanceRecord(
            id = "1",
            employeeId = "EMP001",
            checkInTime = LocalDateTime.of(2024, 1, 15, 9, 0),
            checkOutTime = LocalDateTime.of(2024, 1, 15, 17, 30),
            checkInLatitude = 37.7749,
            checkInLongitude = -122.4194,
            status = AttendanceStatus.PRESENT,
        )

        // Act
        composeTestRule.setContent {
            MaterialTheme {
                AttendanceRecordCard(record = record)
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Check-In: 09:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("Check-Out: 17:30").assertIsDisplayed()
        composeTestRule.onNodeWithText("Status: PRESENT").assertIsDisplayed()
    }

    @Test
    fun attendanceRecordCard_withoutCheckoutTime_doesNotDisplayCheckOutTime() {
        // Arrange
        val record = AttendanceRecord(
            id = "1",
            employeeId = "EMP001",
            checkInTime = LocalDateTime.of(2024, 1, 15, 9, 0),
            checkInLatitude = 37.7749,
            checkInLongitude = -122.4194,
            status = AttendanceStatus.PRESENT,
        )

        // Act
        composeTestRule.setContent {
            MaterialTheme {
                AttendanceRecordCard(record = record)
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Check-In: 09:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("Check-Out:").assertDoesNotExist()
    }

    @Test
    fun successState_withEmptyRecords_displaysEmptyList() {
        // Arrange
        val records = emptyList<AttendanceRecord>()
        val state = AttendanceUiState.Success(records)

        // Act
        composeTestRule.setContent {
            MaterialTheme {
                AttendanceContent(
                    state = state,
                    onCheckIn = { _, _ -> },
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Check In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Attendance Records").assertIsDisplayed()
    }
}
