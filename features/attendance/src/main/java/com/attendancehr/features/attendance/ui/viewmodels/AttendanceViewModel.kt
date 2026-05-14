package com.attendancehr.features.attendance.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attendancehr.core.common.result.Result
import com.attendancehr.features.attendance.domain.entities.AttendanceRecord
import com.attendancehr.features.attendance.domain.usecases.CheckInUseCase
import com.attendancehr.features.attendance.domain.usecases.GetAttendanceRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val getAttendanceRecordsUseCase: GetAttendanceRecordsUseCase,
    private val checkInUseCase: CheckInUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<AttendanceUiState>(AttendanceUiState.Loading)
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    fun loadAttendanceRecords(employeeId: String) {
        viewModelScope.launch {
            _uiState.value = AttendanceUiState.Loading
            val result = getAttendanceRecordsUseCase(employeeId)
            _uiState.value = when (result) {
                is Result.Success -> AttendanceUiState.Success(result.data)
                is Result.Error -> AttendanceUiState.Error(result.exception.message ?: "Unknown error")
                is Result.Loading -> AttendanceUiState.Loading
            }
        }
    }

    fun checkIn(employeeId: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = AttendanceUiState.Loading
            val result = checkInUseCase(employeeId, latitude, longitude)
            _uiState.value = when (result) {
                is Result.Success -> AttendanceUiState.CheckInSuccess(result.data)
                is Result.Error -> AttendanceUiState.Error(result.exception.message ?: "Check-in failed")
                is Result.Loading -> AttendanceUiState.Loading
            }
        }
    }
}

sealed class AttendanceUiState {
    object Loading : AttendanceUiState()
    data class Success(val records: List<AttendanceRecord>) : AttendanceUiState()
    data class CheckInSuccess(val record: AttendanceRecord) : AttendanceUiState()
    data class Error(val message: String) : AttendanceUiState()
}
