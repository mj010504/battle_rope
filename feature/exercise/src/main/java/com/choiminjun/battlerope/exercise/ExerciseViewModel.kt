package com.choiminjun.battlerope.exercise

import androidx.lifecycle.viewModelScope
import com.choiminjun.battlerope.base.BaseViewModel
import com.choiminjun.battlerope.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : BaseViewModel<ExerciseState, ExerciseIntent, ExerciseSideEffect>(
    initialState = ExerciseState(),
) {
    init {
        observeConnectionState()
        observeSnapshot()
        observeIsExerciseRunning()
        connectToDevice()
    }

    override suspend fun handleIntent(intent: ExerciseIntent) {
        when (intent) {
            ExerciseIntent.ClickBack -> clickBack()
            ExerciseIntent.ClickStartExercise -> startExercise()
            ExerciseIntent.ClickStopExercise -> stopExercise()
        }
    }

    override fun onCleared() {
        super.onCleared()
        exerciseRepository.release()
    }

    private fun connectToDevice() {
        exerciseRepository.connectToDevice()
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            exerciseRepository.observeConnectionState().collect { connectionState ->
                reduce { copy(connectionState = connectionState) }
            }
        }
    }

    private fun observeSnapshot() {
        viewModelScope.launch {
            exerciseRepository.observeSnapshot().collect { snapshot ->
                reduce { copy(snapshot = snapshot) }
            }
        }
    }

    private fun observeIsExerciseRunning() {
        viewModelScope.launch {
            exerciseRepository.observeIsExerciseRunning().collect { isRunning ->
                reduce { copy(isExerciseRunning = isRunning) }
            }
        }
    }

    private fun startExercise() {
        exerciseRepository.startExercise()
    }

    private fun stopExercise() {
        exerciseRepository.stopExercise()
    }

    private fun clickBack() {
        postSideEffect(ExerciseSideEffect.NavigateBack)
    }
}
