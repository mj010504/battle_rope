package com.choiminjun.battlerope.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.choiminjun.battlerope.home.HomeRoute
import com.choiminjun.battlerope.navigation.HomeGraph

fun NavGraphBuilder.homeScreen(
    navigateToExercise: () -> Unit,
    navigateToCompetition: () -> Unit,
) {
    composable<HomeGraph.HomeRoute> {
        HomeRoute(
            navigateToExercise = navigateToExercise,
            navigateToCompetition = navigateToCompetition,
        )
    }
}
