package com.choiminjun.makeitall.exercise

import com.choiminjun.makeitall.base.UiIntent
import com.choiminjun.makeitall.base.UiSideEffect
import com.choiminjun.makeitall.base.UiState
import com.choiminjun.makeitall.domain.model.BleConnectionState
import com.choiminjun.makeitall.domain.model.JumpRopeSnapshot

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
