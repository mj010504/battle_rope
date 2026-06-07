package com.choiminjun.makeitall.exercise.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.choiminjun.makeitall.exercise.ExerciseRoute
import com.choiminjun.makeitall.navigation.ExerciseGraph

fun NavGraphBuilder.exerciseScreen(
    navigateBack: () -> Unit,
) {
    composable<ExerciseGraph.ExerciseRoute> {
        ExerciseRoute(navigateBack = navigateBack)
    }
}
