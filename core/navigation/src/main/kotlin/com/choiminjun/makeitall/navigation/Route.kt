package com.choiminjun.makeitall.navigation

sealed class Route(val route: String) {
    data object Home : Route("home")
    data object Exercise : Route("exercise")
}
