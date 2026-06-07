package com.choiminjun.battlerope.navigation

import kotlinx.serialization.Serializable

sealed interface Route

sealed interface HomeGraph : Route {
    @Serializable
    data object HomeRoute : HomeGraph
}

sealed interface ExerciseGraph : Route {
    @Serializable
    data object ExerciseRoute : ExerciseGraph
}

sealed interface CompetitionGraph : Route {
    @Serializable
    data object DeviceConnectionRoute : CompetitionGraph

    @Serializable
    data object ModeSelectionRoute : CompetitionGraph

    @Serializable
    data class GameRoute(val mode: String) : CompetitionGraph
}
