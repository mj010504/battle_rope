package com.choiminjun.makeitall.home

import com.choiminjun.makeitall.base.UiIntent
import com.choiminjun.makeitall.base.UiSideEffect
import com.choiminjun.makeitall.base.UiState

data class HomeState(
    val isLoading: Boolean = false,
) : UiState

sealed interface HomeIntent : UiIntent {
    data object ClickStartExercise : HomeIntent
    data object ClickCompetitionMode : HomeIntent
}

sealed interface HomeSideEffect : UiSideEffect {
    data object NavigateToExercise : HomeSideEffect
    data object NavigateToCompetition : HomeSideEffect
}
