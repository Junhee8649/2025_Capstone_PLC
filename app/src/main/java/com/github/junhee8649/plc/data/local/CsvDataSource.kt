package com.github.junhee8649.plc.data.local

import android.content.Context
import com.github.junhee8649.plc.data.model.VarProcessData
import com.github.junhee8649.plc.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * Assets 폴더에서 CSV 파일을 읽어 파싱
     *
     * @param fileName assets 폴더의 CSV 파일 이름
     * @return 파싱된 VAR 공정 데이터 리스트
     */
    suspend fun loadCsvData(fileName: String): List<VarProcessData> =
        withContext(ioDispatcher) {
            try {
                context.assets.open(fileName).bufferedReader().useLines { lines ->
                    lines
                        .drop(1) // 헤더 제거
                        .mapNotNull { line -> parseLine(line) }
                        .toList()
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to load CSV: $fileName")
                emptyList()
            }
        }

    /**
     * CSV 라인을 VarProcessData로 파싱
     *
     * CSV 구조:
     * Date,Time,전압,전류,진공도,높이,datetime,is_working,work_group,work_id,original_file,elapsed_time,높이변화,누적하강,하강속도,하강속도_원본,높이변화량
     * 0    1    2    3    4      5    6        7          8          9       10            11           12        13        14        15              16
     */
    private fun parseLine(line: String): VarProcessData? {
        return try {
            val values = line.split(",")
            if (values.size < 15) {
                Timber.w("Invalid CSV line: $line")
                return null
            }

            VarProcessData(
                timestamp = values[6].trim(),
                voltage = values[2].trim().toFloat(),
                current = values[3].trim().toFloat(),
                vacuum = values[4].trim().toFloat(),
                height = values[5].trim().toFloat(),
                descentSpeed = values[14].trim().toFloatOrNull() ?: 0f,
                elapsedTime = values[11].trim().toIntOrNull() ?: 0
            )
        } catch (e: Exception) {
            Timber.w(e, "Failed to parse line: $line")
            null
        }
    }

    /**
     * Assets 폴더의 모든 CSV 파일 목록 조회
     */
    suspend fun listCsvFiles(): List<String> = withContext(ioDispatcher) {
        try {
            context.assets.list("")
                ?.filter { it.endsWith(".csv") }
                ?: emptyList()
        } catch (e: Exception) {
            Timber.e(e, "Failed to list CSV files")
            emptyList()
        }
    }
}
