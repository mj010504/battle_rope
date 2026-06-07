package com.choiminjun.battlerope.home

import com.choiminjun.battlerope.base.UiIntent
import com.choiminjun.battlerope.base.UiSideEffect
import com.choiminjun.battlerope.base.UiState

data class HomeState(
    val isLoading: Boolean = false,
) : UiState

enum class BlePermissionTarget {
    EXERCISE,
    COMPETITION,
}

sealed interface HomeIntent : UiIntent {
    data object ClickStartExercise : HomeIntent
    data object ClickCompetitionMode : HomeIntent
    data class BlePermissionResult(val granted: Boolean, val target: BlePermissionTarget) : HomeIntent
}

sealed interface HomeSideEffect : UiSideEffect {
    data object RequestBlePermissionForExercise : HomeSideEffect
    data object RequestBlePermissionForCompetition : HomeSideEffect
    data object NavigateToExercise : HomeSideEffect
    data object NavigateToCompetition : HomeSideEffect
}
