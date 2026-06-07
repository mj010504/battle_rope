package com.choiminjun.battlerope.competition.connection

import com.choiminjun.battlerope.base.UiIntent
import com.choiminjun.battlerope.base.UiSideEffect
import com.choiminjun.battlerope.base.UiState
import com.choiminjun.battlerope.domain.model.BleConnectionState

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
