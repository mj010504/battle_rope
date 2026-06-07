package com.choiminjun.battlerope.competition.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.choiminjun.battlerope.competition.connection.DeviceConnectionRoute
import com.choiminjun.battlerope.competition.game.GameRoute
import com.choiminjun.battlerope.competition.selection.ModeSelectionRoute
import com.choiminjun.battlerope.domain.model.GameMode
import com.choiminjun.battlerope.navigation.CompetitionGraph

fun NavGraphBuilder.deviceConnectionScreen(
    navigateBack: () -> Unit,
    navigateToModeSelection: () -> Unit,
) {
    composable<CompetitionGraph.DeviceConnectionRoute> {
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
    composable<CompetitionGraph.ModeSelectionRoute> {
        ModeSelectionRoute(
            navigateBack = navigateBack,
            navigateToGame = navigateToGame,
        )
    }
}

fun NavGraphBuilder.gameScreen(
    navigateBack: () -> Unit,
) {
    composable<CompetitionGraph.GameRoute> {
        GameRoute(
            navigateBack = navigateBack,
        )
    }
}

fun NavController.navigateToDeviceConnection() {
    navigate(CompetitionGraph.DeviceConnectionRoute)
}

fun NavController.navigateToModeSelection() {
    navigate(CompetitionGraph.ModeSelectionRoute)
}

fun NavController.navigateToGame(mode: GameMode) {
    navigate(CompetitionGraph.GameRoute(mode.name))
}
