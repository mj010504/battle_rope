package com.choiminjun.makeitall.competition.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.choiminjun.makeitall.competition.connection.DeviceConnectionRoute
import com.choiminjun.makeitall.competition.game.GameRoute
import com.choiminjun.makeitall.competition.selection.ModeSelectionRoute
import com.choiminjun.makeitall.domain.model.GameMode
import com.choiminjun.makeitall.navigation.Route

fun NavGraphBuilder.deviceConnectionScreen(
    navigateBack: () -> Unit,
    navigateToModeSelection: () -> Unit,
) {
    composable(Route.DeviceConnection.route) {
        DeviceConnectionRoute(
            navigateBack = navigateBack,
            navigateToModeSelection = navigateToModeSelection,
        )
    }
}

fun NavGraphBuilder.modeSelectionScreen(
    navigateBack: () -> Unit,
    navigateToGame: (GameMode) -> Unit,
) {
    composable(Route.ModeSelection.route) {
        ModeSelectionRoute(
            navigateBack = navigateBack,
            navigateToGame = navigateToGame,
        )
    }
}

fun NavGraphBuilder.gameScreen(
    navigateBack: () -> Unit,
) {
    composable(
        route = Route.Game.route,
        arguments = listOf(
            navArgument(Route.Game.MODE_ARG) {
                type = NavType.StringType
            },
        ),
    ) {
        GameRoute(
            navigateBack = navigateBack,
        )
    }
}

fun NavController.navigateToDeviceConnection() {
    navigate(Route.DeviceConnection.route)
}

fun NavController.navigateToModeSelection() {
    navigate(Route.ModeSelection.route)
}

fun NavController.navigateToGame(mode: GameMode) {
    navigate(Route.Game.createRoute(mode.name))
}
