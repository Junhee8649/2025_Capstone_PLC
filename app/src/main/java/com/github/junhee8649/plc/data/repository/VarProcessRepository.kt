package com.github.junhee8649.plc.data.repository

import com.github.junhee8649.plc.data.local.CsvDataSource
import com.github.junhee8649.plc.data.model.VarProcessData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface VarProcessRepository {
    /**
     * 실시간 VAR 공정 데이터 스트림
     * 서버 연동 전에는 정적 CSV 데이터를 반환
     */
    fun getRealtimeData(): Flow<List<VarProcessData>>

    /**
     * 특정 CSV 파일의 모든 데이터 조회
     */
    suspend fun loadHistoricalData(fileName: String): List<VarProcessData>
}

@Singleton
class VarProcessRepositoryImpl @Inject constructor(
    private val csvDataSource: CsvDataSource
) : VarProcessRepository {

    override fun getRealtimeData(): Flow<List<VarProcessData>> = flow {
        // TODO: 서버 연동 시 WebSocket 또는 HTTP 폴링으로 대체
        // 현재는 정적 CSV 데이터를 시뮬레이션하여 반환
        val allData = csvDataSource.loadCsvData("SA00000_20250715_W003.csv")

        if (allData.isEmpty()) {
            emit(emptyList())
            return@flow
        }

        // 실시간 데이터 시뮬레이션: 1초마다 새 데이터 추가
        var currentIndex = 0
        while (currentIndex < allData.size) {
            // 슬라이딩 윈도우: 최근 6개 데이터만 유지 (실시간 모니터링, 항상 화면에 표시)
            val startIndex = maxOf(0, currentIndex - 5)
            emit(allData.subList(startIndex, currentIndex + 1))
            currentIndex++
            delay(1000) // 1초 간격
        }
    }

    override suspend fun loadHistoricalData(fileName: String): List<VarProcessData> {
        return csvDataSource.loadCsvData(fileName)
    }
}
