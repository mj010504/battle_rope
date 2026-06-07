package com.choiminjun.battlerope.competition.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.choiminjun.battlerope.base.BaseViewModel
import com.choiminjun.battlerope.competition.game.engine.GameEngine
import com.choiminjun.battlerope.domain.model.GameMode
import com.choiminjun.battlerope.domain.model.GameResult
import com.choiminjun.battlerope.domain.model.Player
import com.choiminjun.battlerope.domain.model.PlayerScore
import com.choiminjun.battlerope.domain.repository.ExerciseRepository
import com.choiminjun.battlerope.navigation.CompetitionGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
) : BaseViewModel<GameState, GameIntent, GameSideEffect>(
    initialState = GameState(),
) {
    private var countdownJob: Job? = null
    private var gameTimerJob: Job? = null
    private var latestRawCountA: Int = 0
    private var latestRawCountB: Int = 0
    private lateinit var gameEngine: GameEngine

    init {
        initializeGame(savedStateHandle)
        observeSnapshot()
    }

    private fun initializeGame(savedStateHandle: SavedStateHandle) {
        val modeArg = savedStateHandle.toRoute<CompetitionGraph.GameRoute>().mode
        val mode = runCatching { GameMode.valueOf(modeArg) }.getOrDefault(GameMode.CLASSIC)
        gameEngine = GameEngine(mode)
        reduce {
            copy(
                mode = mode,
                remainingTimeSec = mode.durationSec,
            )
        }
        startCountdown()
    }

    override suspend fun handleIntent(intent: GameIntent) {
        when (intent) {
            GameIntent.ClickBack -> clickBack()
        }
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
        gameTimerJob?.cancel()
        exerciseRepository.release()
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (count in COUNTDOWN_START downTo 1) {
                reduce { copy(countdownValue = count) }
                delay(COUNTDOWN_INTERVAL_MS)
            }

            startGame()
        }
    }

    private fun startGame() {
        reduce {
            copy(
                phase = GamePhase.PLAYING,
                playerA = PlayerScore(Player.A),
                playerB = PlayerScore(Player.B),
            )
        }
        latestRawCountA = 0
        latestRawCountB = 0
        exerciseRepository.startExercise()
        startGameTimer()
    }

    private fun startGameTimer() {
        gameTimerJob?.cancel()
        gameTimerJob = viewModelScope.launch {
            val duration = state.value.mode.durationSec
            for (elapsedSec in 1..duration) {
                delay(TIMER_INTERVAL_MS)

                val currentState = Pair(state.value.playerA, state.value.playerB)
                val tickResult = gameEngine.tick(elapsedSec, latestRawCountA, latestRawCountB, currentState)
                val remaining = duration - elapsedSec

                val leadingPlayer = when {
                    tickResult.playerA.score > tickResult.playerB.score -> Player.A
                    tickResult.playerB.score > tickResult.playerA.score -> Player.B
                    else -> null
                }

                reduce {
                    copy(
                        remainingTimeSec = remaining,
                        playerA = tickResult.playerA,
                        playerB = tickResult.playerB,
                        feverState = tickResult.feverState,
                        leader = leadingPlayer,
                        recentEvent = tickResult.events.lastOrNull(),
                        gameResult = GameResult(
                            mode = mode,
                            winner = leadingPlayer,
                            playerA = tickResult.playerA,
                            playerB = tickResult.playerB,
                            totalDurationSec = duration,
                        ),
                    )
                }

                if (remaining == 0) {
                    finishGame()
                    return@launch
                }
            }
        }
    }

    private fun finishGame() {
        exerciseRepository.stopExercise()
        reduce { copy(phase = GamePhase.FINISHED) }
    }

    private fun observeSnapshot() {
        viewModelScope.launch {
            exerciseRepository.observeSnapshot().collect { snapshot ->
                if (snapshot != null) {
                    latestRawCountA = snapshot.userACount
                    latestRawCountB = snapshot.userBCount
                }
            }
        }
    }

    private fun clickBack() {
        countdownJob?.cancel()
        gameTimerJob?.cancel()
        exerciseRepository.stopExercise()
        postSideEffect(GameSideEffect.NavigateBack)
    }

    companion object {
        private const val COUNTDOWN_START = 3
        private val COUNTDOWN_INTERVAL_MS: Duration = 1.seconds
        private val TIMER_INTERVAL_MS: Duration = 1.seconds
    }
}
