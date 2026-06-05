package com.choiminjun.makeitall.competition.selection

import com.choiminjun.makeitall.base.UiIntent
import com.choiminjun.makeitall.base.UiSideEffect
import com.choiminjun.makeitall.base.UiState
import com.choiminjun.makeitall.domain.model.GameMode

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
