package com.choiminjun.battlerope.domain.model

sealed interface BleConnectionState {
    data object Disconnected : BleConnectionState
    data object Connecting : BleConnectionState
    data class Connected(val deviceName: String) : BleConnectionState
}
