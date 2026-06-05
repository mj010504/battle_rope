package com.choiminjun.makeitall.competition.connection

import androidx.lifecycle.viewModelScope
import com.choiminjun.makeitall.base.BaseViewModel
import com.choiminjun.makeitall.ble.source.JumpRopeBleSource
import com.choiminjun.makeitall.domain.model.BleConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceConnectionViewModel @Inject constructor(
    private val jumpRopeBleSource: JumpRopeBleSource,
) : BaseViewModel<DeviceConnectionState, DeviceConnectionIntent, DeviceConnectionSideEffect>(
    initialState = DeviceConnectionState(),
) {
    init {
        observeConnectionState()
        observeScanResults()
        startScan()
    }

    override suspend fun handleIntent(intent: DeviceConnectionIntent) {
        when (intent) {
            DeviceConnectionIntent.ClickBack -> clickBack()
            DeviceConnectionIntent.ClickGameStart -> postSideEffect(DeviceConnectionSideEffect.NavigateToModeSelection)
        }
    }

    override fun onCleared() {
        super.onCleared()
        jumpRopeBleSource.stopScan()
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
                reduce { copy(connectionState = connectionState) }

                if (connectionState is BleConnectionState.Connected) {
                    reduce { copy(isConnected = true) }
                }
            }
        }
    }

    private fun clickBack() {
        jumpRopeBleSource.disconnect()
        postSideEffect(DeviceConnectionSideEffect.NavigateBack)
    }
}
