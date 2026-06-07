package com.choiminjun.battlerope.exercise.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.choiminjun.battlerope.exercise.ExerciseRoute
import com.choiminjun.battlerope.navigation.ExerciseGraph

fun NavGraphBuilder.exerciseScreen(
    navigateBack: () -> Unit,
) {
    composable<ExerciseGraph.ExerciseRoute> {
        ExerciseRoute(navigateBack = navigateBack)
    }
}
