package com.choiminjun.battlerope.exercise

import com.choiminjun.battlerope.base.UiIntent
import com.choiminjun.battlerope.base.UiSideEffect
import com.choiminjun.battlerope.base.UiState
import com.choiminjun.battlerope.domain.model.BleConnectionState
import com.choiminjun.battlerope.domain.model.JumpRopeSnapshot

data class ExerciseState(
    val connectionState: BleConnectionState = BleConnectionState.Disconnected,
    val snapshot: JumpRopeSnapshot? = null,
    val isExerciseRunning: Boolean = false,
) : UiState

sealed interface ExerciseIntent : UiIntent {
    data object ClickBack : ExerciseIntent
    data object ClickStartExercise : ExerciseIntent
    data object ClickStopExercise : ExerciseIntent
}

sealed interface ExerciseSideEffect : UiSideEffect {
    data object NavigateBack : ExerciseSideEffect
}
