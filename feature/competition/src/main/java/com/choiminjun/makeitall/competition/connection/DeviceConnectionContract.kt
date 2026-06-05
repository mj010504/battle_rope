package com.choiminjun.makeitall.competition.connection

import com.choiminjun.makeitall.base.UiIntent
import com.choiminjun.makeitall.base.UiSideEffect
import com.choiminjun.makeitall.base.UiState
import com.choiminjun.makeitall.domain.model.BleConnectionState

data class DeviceConnectionState(
    val connectionState: BleConnectionState = BleConnectionState.Disconnected,
    val isConnected: Boolean = false,
) : UiState

sealed interface DeviceConnectionIntent : UiIntent {
    data object ClickBack : DeviceConnectionIntent
    data object ClickGameStart : DeviceConnectionIntent
}

sealed interface DeviceConnectionSideEffect : UiSideEffect {
    data object NavigateBack : DeviceConnectionSideEffect
    data object NavigateToModeSelection : DeviceConnectionSideEffect
}
