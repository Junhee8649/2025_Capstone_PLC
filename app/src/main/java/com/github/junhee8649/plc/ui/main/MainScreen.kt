package com.github.junhee8649.plc.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.junhee8649.plc.ui.main.components.LineChart

@Composable
fun MainScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: MonitoringViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainContent(
        uiState = uiState,
        windowSizeClass = windowSizeClass,
        modifier = modifier
    )
}

@Composable
private fun MainContent(
    uiState: MonitoringUiState,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        when (uiState) {
            is MonitoringUiState.Loading -> LoadingContent()
            is MonitoringUiState.Empty -> EmptyContent()
            is MonitoringUiState.Error -> ErrorContent(uiState.message)
            is MonitoringUiState.Success -> MonitoringContent(
                uiState = uiState,
                windowSizeClass = windowSizeClass
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "데이터가 없습니다",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorContent(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "오류: $message",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun MonitoringContent(
    uiState: MonitoringUiState.Success,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> CompactLayout(uiState, modifier)
        else -> ExpandedLayout(uiState, modifier)
    }
}

@Composable
private fun CompactLayout(
    uiState: MonitoringUiState.Success,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TODO: 향후 카메라 영역 추가
        ChartSection(
            title = "전류 (A)",
            data = uiState.currentData,
            currentValue = uiState.latestData?.current
        )
        ChartSection(
            title = "진공도 (Torr)",
            data = uiState.vacuumData,
            currentValue = uiState.latestData?.vacuum
        )
        ChartSection(
            title = "하강속도 (mm/s)",
            data = uiState.descentSpeedData,
            currentValue = uiState.latestData?.descentSpeed
        )
    }
}

@Composable
private fun ExpandedLayout(
    uiState: MonitoringUiState.Success,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TODO: 향후 카메라 영역 (상단, 좌우 배치)
        // Row { CameraView(원본), CameraView(CV 필터링) }

        // 차트 영역 (하단, 그리드 배치)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChartSection(
                title = "전류 (A)",
                data = uiState.currentData,
                currentValue = uiState.latestData?.current,
                modifier = Modifier.weight(1f)
            )
            ChartSection(
                title = "진공도 (Torr)",
                data = uiState.vacuumData,
                currentValue = uiState.latestData?.vacuum,
                modifier = Modifier.weight(1f)
            )
            ChartSection(
                title = "하강속도 (mm/s)",
                data = uiState.descentSpeedData,
                currentValue = uiState.latestData?.descentSpeed,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ChartSection(
    title: String,
    data: List<Float>,
    currentValue: Float?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            currentValue?.let {
                Text(
                    text = String.format("%.2f", it),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        LineChart(
            data = data,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}