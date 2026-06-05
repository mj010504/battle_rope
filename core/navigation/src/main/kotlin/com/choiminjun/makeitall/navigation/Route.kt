package com.choiminjun.makeitall.navigation

sealed class Route(val route: String) {
    data object Home : Route("home")
    data object Exercise : Route("exercise")
    data object DeviceConnection : Route("device_connection")
    data object ModeSelection : Route("mode_selection")
    data object Game : Route("game/{mode}") {
        const val MODE_ARG = "mode"
        fun createRoute(mode: String): String = "game/$mode"
    }
}
