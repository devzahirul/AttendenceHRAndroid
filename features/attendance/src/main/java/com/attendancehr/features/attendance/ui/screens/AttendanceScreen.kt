package com.attendancehr.features.attendance.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.attendancehr.features.attendance.domain.entities.AttendanceRecord
import com.attendancehr.features.attendance.ui.viewmodels.AttendanceUiState
import com.attendancehr.features.attendance.ui.viewmodels.AttendanceViewModel
import java.time.format.DateTimeFormatter

@Composable
fun AttendanceScreen(
    employeeId: String,
    viewModel: AttendanceViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(employeeId) {
        viewModel.loadAttendanceRecords(employeeId)
    }

    AttendanceContent(
        state = uiState,
        onCheckIn = { lat, lon -> viewModel.checkIn(employeeId, lat, lon) },
        modifier = modifier,
    )
}

@Composable
private fun AttendanceContent(
    state: AttendanceUiState,
    onCheckIn: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is AttendanceUiState.Loading -> {
            LoadingState(modifier)
        }

        is AttendanceUiState.Success -> {
            SuccessState(
                records = state.records,
                onCheckIn = onCheckIn,
                modifier = modifier,
            )
        }

        is AttendanceUiState.CheckInSuccess -> {
            CheckInSuccessState(
                record = state.record,
                modifier = modifier,
            )
        }

        is AttendanceUiState.Error -> {
            ErrorState(
                message = state.message,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Text(
            text = "Loading attendance records...",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SuccessState(
    records: List<AttendanceRecord>,
    onCheckIn: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Button(
            onClick = { onCheckIn(37.7749, -122.4194) }, // Example coordinates
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text("Check In")
        }

        Text(
            text = "Attendance Records",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall,
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(records, key = { it.id }) { record ->
                AttendanceRecordCard(record)
            }
        }
    }
}

@Composable
private fun AttendanceRecordCard(record: AttendanceRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Check-In: ${record.checkInTime.format(DateTimeFormatter.ISO_LOCAL_TIME)}",
                style = MaterialTheme.typography.bodyMedium,
            )
            record.checkOutTime?.let {
                Text(
                    text = "Check-Out: ${it.format(DateTimeFormatter.ISO_LOCAL_TIME)}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Text(
                text = "Status: ${record.status}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
private fun CheckInSuccessState(record: AttendanceRecord, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "✓ Check-in Successful",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Time: ${record.checkInTime.format(DateTimeFormatter.ISO_LOCAL_TIME)}",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ErrorState(message: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error,
        )
        Text(
            text = message,
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
