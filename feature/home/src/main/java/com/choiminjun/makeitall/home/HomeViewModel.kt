package com.choiminjun.makeitall.home

import com.choiminjun.makeitall.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeState, HomeIntent, HomeSideEffect>(
    initialState = HomeState(),
) {
    override suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.ClickStartExercise -> clickStartExercise()
            HomeIntent.ClickCompetitionMode -> clickCompetitionMode()
        }
    }

    private fun clickStartExercise() {
        postSideEffect(HomeSideEffect.NavigateToExercise)
    }

    private fun clickCompetitionMode() {
        postSideEffect(HomeSideEffect.NavigateToCompetition)
    }
}
