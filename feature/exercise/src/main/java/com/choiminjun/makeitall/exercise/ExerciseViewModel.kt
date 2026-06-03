package com.choiminjun.makeitall.exercise

import androidx.lifecycle.viewModelScope
import com.choiminjun.makeitall.base.BaseViewModel
import com.choiminjun.makeitall.ble.source.JumpRopeBleSource
import com.choiminjun.makeitall.domain.model.BleConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val jumpRopeBleSource: JumpRopeBleSource,
) : BaseViewModel<ExerciseState, ExerciseIntent, ExerciseSideEffect>(
    initialState = ExerciseState(),
) {
    init {
        observeConnectionState()
        observeSnapshot()
        observeScanResults()
        observeAckResult()
        startScan()
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
        jumpRopeBleSource.unsubscribeFromExerciseData()
        jumpRopeBleSource.disconnect()
    }

    private fun startScan() {
        jumpRopeBleSource.startScan()
    }

    private fun observeScanResults() {
        viewModelScope.launch {
            jumpRopeBleSource.scanResults.collect { devices ->
                val device = devices.firstOrNull() ?: return@collect
                jumpRopeBleSource.stopScan()
                jumpRopeBleSource.connect(device)
            }
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            jumpRopeBleSource.connectionState.collect { connectionState ->
                if (connectionState is BleConnectionState.Connected) {
                    jumpRopeBleSource.subscribeToExerciseData()
                }
                reduce {
                    copy(
                        connectionState = connectionState,
                        isExerciseRunning = if (connectionState is BleConnectionState.Disconnected) {
                            false
                        } else {
                            isExerciseRunning
                        },
                    )
                }
            }
        }
    }

    private fun observeSnapshot() {
        viewModelScope.launch {
            jumpRopeBleSource.snapshot.collect { snapshot ->
                reduce { copy(snapshot = snapshot) }
            }
        }
    }

    private fun observeAckResult() {
        viewModelScope.launch {
            jumpRopeBleSource.ackResult.collect { ack ->
                if (!ack.isSuccess) {
                    Timber.w("ACK 실패 | requestCmdId=0x${"%02X".format(ack.requestCmdId)} | resultCode=0x${"%02X".format(ack.resultCode)}")
                    return@collect
                }
                when (ack.requestCmdId) {
                    JumpRopeBleSource.CMD_START[0] -> reduce { copy(isExerciseRunning = true) }
                    JumpRopeBleSource.CMD_STOP[0] -> reduce { copy(isExerciseRunning = false) }
                }
            }
        }
    }

    private fun startExercise() {
        jumpRopeBleSource.startExercise()
    }

    private fun stopExercise() {
        jumpRopeBleSource.stopExercise()
    }

    private fun clickBack() {
        postSideEffect(ExerciseSideEffect.NavigateBack)
    }
}
