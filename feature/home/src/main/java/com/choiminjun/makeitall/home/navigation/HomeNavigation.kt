package com.choiminjun.makeitall.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.choiminjun.makeitall.home.HomeRoute
import com.choiminjun.makeitall.navigation.Route

fun NavGraphBuilder.homeScreen(
    navigateToExercise: () -> Unit,
    navigateToCompetition: () -> Unit,
) {
    composable(Route.Home.route) {
        HomeRoute(
            navigateToExercise = navigateToExercise,
            navigateToCompetition = navigateToCompetition,
        )
    }
}
