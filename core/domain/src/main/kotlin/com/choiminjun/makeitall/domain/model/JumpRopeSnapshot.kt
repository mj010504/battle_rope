package com.choiminjun.makeitall.domain.model

/**
 * BLE 디바이스가 1초마다 전달하는 줄넘기 운동 데이터의 도메인 표현.
 *
 * @property elapsedSec START 이후 경과 시간(초)
 * @property userACount USER A 누적 줄넘기 횟수
 * @property userBCount USER B 누적 줄넘기 횟수
 */
data class JumpRopeSnapshot(
    val elapsedSec: Int,
    val userACount: Int,
    val userBCount: Int,
)
