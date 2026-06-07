package com.choiminjun.battlerope.domain.model

data class PlayerScore(
    val player: Player,
    val rawJumpCount: Int = 0,
    val mappedJumpCount: Int = 0,
    val score: Int = 0,
    val combo: Int = 0,
    val maxCombo: Int = 0,
    val trippedCount: Int = 0,
    val overtakeCount: Int = 0,
    val feverScore: Int = 0,
)
