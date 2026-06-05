package com.choiminjun.makeitall.competition.selection

import com.choiminjun.makeitall.base.BaseViewModel
import com.choiminjun.makeitall.domain.model.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModeSelectionViewModel @Inject constructor() :
    BaseViewModel<ModeSelectionState, ModeSelectionIntent, ModeSelectionSideEffect>(
        initialState = ModeSelectionState(),
    ) {

    override suspend fun handleIntent(intent: ModeSelectionIntent) {
        when (intent) {
            ModeSelectionIntent.ClickBack -> clickBack()
            is ModeSelectionIntent.SelectMode -> selectMode(intent.mode)
            ModeSelectionIntent.ClickStartGame -> startGame()
        }
    }

    private fun selectMode(mode: GameMode) {
        reduce { copy(selectedMode = mode) }
    }

    private fun startGame() {
        val mode = state.value.selectedMode ?: return
        postSideEffect(ModeSelectionSideEffect.NavigateToGame(mode))
    }

    private fun clickBack() {
        postSideEffect(ModeSelectionSideEffect.NavigateBack)
    }
}
