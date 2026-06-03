package com.choiminjun.makeitall.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.choiminjun.makeitall.exercise.navigation.exerciseScreen
import com.choiminjun.makeitall.home.navigation.homeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.route,
    ) {
        homeScreen(
            navigateToExercise = { navController.navigate(Route.Exercise.route) },
        )
        exerciseScreen(
            navigateBack = { navController.popBackStack() },
        )
    }
}
