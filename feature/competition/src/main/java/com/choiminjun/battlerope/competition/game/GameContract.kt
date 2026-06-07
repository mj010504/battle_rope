package com.choiminjun.battlerope.competition.game

import com.choiminjun.battlerope.base.UiIntent
import com.choiminjun.battlerope.base.UiSideEffect
import com.choiminjun.battlerope.base.UiState
import com.choiminjun.battlerope.domain.model.FeverState
import com.choiminjun.battlerope.domain.model.GameEvent
import com.choiminjun.battlerope.domain.model.GameMode
import com.choiminjun.battlerope.domain.model.GameResult
import com.choiminjun.battlerope.domain.model.Player
import com.choiminjun.battlerope.domain.model.PlayerScore

enum class GamePhase {
    COUNTDOWN,
    PLAYING,
    FINISHED,
}

data class GameState(
    val mode: GameMode = GameMode.CLASSIC,
    val phase: GamePhase = GamePhase.COUNTDOWN,
    val countdownValue: Int = 3,
    val remainingTimeSec: Int = 0,
    val gameResult: GameResult = GameResult(
        mode = GameMode.CLASSIC,
        winner = Player.A,
        playerA = PlayerScore(Player.A),
        playerB = PlayerScore(Player.B),
        totalDurationSec = 0,
    ),
    val playerA: PlayerScore = PlayerScore(Player.A),
    val playerB: PlayerScore = PlayerScore(Player.B),
    val feverState: FeverState = FeverState(),
    val leader: Player? = null,
    val recentEvent: GameEvent? = null,
) : UiState

sealed interface GameIntent : UiIntent {
    data object ClickBack : GameIntent
    data object ClickRetry : GameIntent
}

sealed interface GameSideEffect : UiSideEffect {
    data object NavigateBack : GameSideEffect
}
