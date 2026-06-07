package com.choiminjun.battlerope.home

import com.choiminjun.battlerope.base.BaseViewModel
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
            is HomeIntent.BlePermissionResult -> handleBlePermissionResult(intent.granted, intent.target)
        }
    }

    private fun clickStartExercise() {
        postSideEffect(HomeSideEffect.RequestBlePermissionForExercise)
    }

    private fun clickCompetitionMode() {
        postSideEffect(HomeSideEffect.RequestBlePermissionForCompetition)
    }

    private fun handleBlePermissionResult(granted: Boolean, target: BlePermissionTarget) {
        if (!granted) return
        when (target) {
            BlePermissionTarget.EXERCISE -> postSideEffect(HomeSideEffect.NavigateToExercise)
            BlePermissionTarget.COMPETITION -> postSideEffect(HomeSideEffect.NavigateToCompetition)
        }
    }
}
