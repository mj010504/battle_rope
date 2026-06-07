package com.choiminjun.battlerope.competition.selection

import com.choiminjun.battlerope.base.UiIntent
import com.choiminjun.battlerope.base.UiSideEffect
import com.choiminjun.battlerope.base.UiState
import com.choiminjun.battlerope.domain.model.GameMode

data class ModeSelectionState(
    val availableModes: List<GameMode> = GameMode.entries,
    val selectedMode: GameMode? = null,
) : UiState

sealed interface ModeSelectionIntent : UiIntent {
    data object ClickBack : ModeSelectionIntent
    data class SelectMode(val mode: GameMode) : ModeSelectionIntent
    data object ClickStartGame : ModeSelectionIntent
}

sealed interface ModeSelectionSideEffect : UiSideEffect {
    data object NavigateBack : ModeSelectionSideEffect
    data class NavigateToGame(val mode: GameMode) : ModeSelectionSideEffect
}
