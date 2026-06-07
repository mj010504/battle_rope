package com.choiminjun.battlerope.domain.repository

import com.choiminjun.battlerope.domain.model.BleConnectionState
import com.choiminjun.battlerope.domain.model.JumpRopeSnapshot
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun observeConnectionState(): Flow<BleConnectionState>
    fun observeSnapshot(): Flow<JumpRopeSnapshot?>
    fun observeIsExerciseRunning(): Flow<Boolean>

    fun connectToDevice()
    fun startExercise()
    fun stopExercise()
    fun disconnect()
    fun release()
}
