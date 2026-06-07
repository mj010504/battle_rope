package com.choiminjun.battlerope.domain.model

data class GameResult(
    val mode: GameMode,
    val winner: Player?,
    val playerA: PlayerScore,
    val playerB: PlayerScore,
    val totalDurationSec: Int,
)
