package com.choiminjun.battlerope.competition.connection

import androidx.lifecycle.viewModelScope
import com.choiminjun.battlerope.base.BaseViewModel
import com.choiminjun.battlerope.domain.model.BleConnectionState
import com.choiminjun.battlerope.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceConnectionViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : BaseViewModel<DeviceConnectionState, DeviceConnectionIntent, DeviceConnectionSideEffect>(
    initialState = DeviceConnectionState(),
) {
    init {
        observeConnectionState()
        connectToDevice()
    }

    override suspend fun handleIntent(intent: DeviceConnectionIntent) {
        when (intent) {
            DeviceConnectionIntent.ClickBack -> clickBack()
            DeviceConnectionIntent.ClickGameStart -> postSideEffect(DeviceConnectionSideEffect.NavigateToModeSelection)
        }
    }

    private fun connectToDevice() {
        exerciseRepository.connectToDevice()
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            exerciseRepository.observeConnectionState().collect { connectionState ->
                reduce {
                    copy(
                        connectionState = connectionState,
                        isConnected = connectionState is BleConnectionState.Connected,
                    )
                }
            }
        }
    }

    private fun clickBack() {
        exerciseRepository.disconnect()
        postSideEffect(DeviceConnectionSideEffect.NavigateBack)
    }
}
