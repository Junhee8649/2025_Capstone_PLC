package com.github.junhee8649.plc.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.junhee8649.plc.data.model.VarProcessData
import com.github.junhee8649.plc.data.repository.VarProcessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MonitoringViewModel @Inject constructor(
    repository: VarProcessRepository
) : ViewModel() {

    val uiState: StateFlow<MonitoringUiState> = repository.getRealtimeData()
        .map { data ->
            if (data.isEmpty()) {
                MonitoringUiState.Empty
            } else {
                MonitoringUiState.Success(
                    currentData = data.map { it.current },
                    vacuumData = data.map { it.vacuum },
                    descentSpeedData = data.map { it.descentSpeed },
                    timestamps = data.map { it.timestamp },
                    latestData = data.lastOrNull()
                )
            }
        }
        .catch { e ->
            Timber.e(e, "Failed to load monitoring data")
            emit(MonitoringUiState.Error(e.message ?: "Unknown error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MonitoringUiState.Loading
        )
}

sealed interface MonitoringUiState {
    data object Loading : MonitoringUiState
    data object Empty : MonitoringUiState

    data class Success(
        val currentData: List<Float>,
        val vacuumData: List<Float>,
        val descentSpeedData: List<Float>,
        val timestamps: List<String>,
        val latestData: VarProcessData?
    ) : MonitoringUiState

    data class Error(val message: String) : MonitoringUiState
}
