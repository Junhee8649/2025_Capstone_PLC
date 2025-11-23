package com.github.junhee8649.plc.data.model

import kotlinx.serialization.Serializable

/**
 * VAR 공정 실시간 데이터 모델
 *
 * @property timestamp 측정 시각 (ISO-8601 형식)
 * @property voltage 전압 (V)
 * @property current 전류 (A)
 * @property vacuum 진공도 (Torr)
 * @property height Ingot 높이 (mm)
 * @property descentSpeed Ingot 하강속도 (mm/s)
 * @property elapsedTime 작업 경과 시간 (초)
 */
@Serializable
data class VarProcessData(
    val timestamp: String,
    val voltage: Float,
    val current: Float,
    val vacuum: Float,
    val height: Float,
    val descentSpeed: Float,
    val elapsedTime: Int
)
