package com.choiminjun.makeitall.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.choiminjun.makeitall.home.HomeRoute
import com.choiminjun.makeitall.navigation.HomeGraph

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
