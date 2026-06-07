package com.choiminjun.battlerope.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.choiminjun.battlerope.competition.navigation.deviceConnectionScreen
import com.choiminjun.battlerope.competition.navigation.gameScreen
import com.choiminjun.battlerope.competition.navigation.modeSelectionScreen
import com.choiminjun.battlerope.competition.navigation.navigateToGame
import com.choiminjun.battlerope.competition.navigation.navigateToModeSelection
import com.choiminjun.battlerope.exercise.navigation.exerciseScreen
import com.choiminjun.battlerope.home.navigation.homeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = HomeGraph.HomeRoute,
    ) {
        homeScreen(
            navigateToExercise = { navController.navigate(ExerciseGraph.ExerciseRoute) },
            navigateToCompetition = { navController.navigate(CompetitionGraph.DeviceConnectionRoute) },
        )
        exerciseScreen(
            navigateBack = { navController.popBackStack() },
        )
        deviceConnectionScreen(
            navigateBack = { navController.popBackStack() },
            navigateToModeSelection = { navController.navigateToModeSelection() },
        )
        modeSelectionScreen(
            navigateBack = { navController.popBackStack() },
            navigateToGame = { mode -> navController.navigateToGame(mode) },
        )
        gameScreen(
            navigateBack = {
                navController.popBackStack<HomeGraph.HomeRoute>(inclusive = false)
            },
        )
    }
}
