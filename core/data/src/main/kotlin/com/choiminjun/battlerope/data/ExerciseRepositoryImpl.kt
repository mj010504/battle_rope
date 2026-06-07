package com.choiminjun.battlerope.data

import com.choiminjun.battlerope.ble.source.JumpRopeBleSource
import com.choiminjun.battlerope.domain.model.BleConnectionState
import com.choiminjun.battlerope.domain.model.JumpRopeSnapshot
import com.choiminjun.battlerope.domain.repository.ExerciseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val jumpRopeBleSource: JumpRopeBleSource,
) : ExerciseRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _isExerciseRunning = MutableStateFlow(false)

    init {
        watchConnectionState()
        watchScanResults()
        watchAckResult()
    }

    private fun watchConnectionState() {
        scope.launch {
            jumpRopeBleSource.connectionState.collect { state ->
                if (state is BleConnectionState.Connected) {
                    jumpRopeBleSource.subscribeToExerciseData()
                } else if (state is BleConnectionState.Disconnected) {
                    _isExerciseRunning.value = false
                }
            }
        }
    }

    private fun watchScanResults() {
        scope.launch {
            jumpRopeBleSource.scanResults.collect { devices ->
                val device = devices.firstOrNull() ?: return@collect
                jumpRopeBleSource.stopScan()
                jumpRopeBleSource.connect(device)
            }
        }
    }

    private fun watchAckResult() {
        scope.launch {
            jumpRopeBleSource.ackResult.collect { ack ->
                if (!ack.isSuccess) {
                    Timber.w(
                        "ACK 실패 | requestCmdId=0x${"%02X".format(ack.requestCmdId)} | resultCode=0x${"%02X".format(ack.resultCode)}",
                    )
                    return@collect
                }
                when (ack.requestCmdId) {
                    JumpRopeBleSource.CMD_START[0] -> _isExerciseRunning.value = true
                    JumpRopeBleSource.CMD_STOP[0] -> _isExerciseRunning.value = false
                }
            }
        }
    }

    override fun observeConnectionState(): Flow<BleConnectionState> = jumpRopeBleSource.connectionState

    override fun observeSnapshot(): Flow<JumpRopeSnapshot?> = jumpRopeBleSource.snapshot

    override fun observeIsExerciseRunning(): Flow<Boolean> = _isExerciseRunning.asStateFlow()

    override fun connectToDevice() {
        jumpRopeBleSource.startScan()
    }

    override fun startExercise() = jumpRopeBleSource.startExercise()

    override fun stopExercise() = jumpRopeBleSource.stopExercise()

    override fun disconnect() = jumpRopeBleSource.disconnect()

    override fun release() {
        jumpRopeBleSource.stopExercise()
        jumpRopeBleSource.unsubscribeFromExerciseData()
        jumpRopeBleSource.disconnect()
    }
}
