package com.choiminjun.battlerope.competition.game

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.battlerope.designsystem.theme.BattleRopeTheme
import com.choiminjun.battlerope.domain.model.GameMode
import com.choiminjun.battlerope.domain.model.Player
import com.choiminjun.battlerope.domain.model.PlayerScore

@Composable
fun GameRoute(
    navigateBack: () -> Unit,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val activity = context as? Activity

    DisposableEffect(Unit) {
        val originalOrientation = activity?.requestedOrientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity?.requestedOrientation =
                originalOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            GameSideEffect.NavigateBack -> navigateBack()
        }
    }

    GameScreen(
        uiState = uiState,
        onBackClick = { viewModel.onIntent(GameIntent.ClickBack) },
    )
}

@Composable
fun GameScreen(
    uiState: GameState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        when (uiState.phase) {
            GamePhase.COUNTDOWN -> CountdownContent(countdownValue = uiState.countdownValue)
            GamePhase.PLAYING -> PlayingContent(uiState = uiState)
            GamePhase.FINISHED -> GameResultSection(
                result = uiState.gameResult,
            )
        }
    }
}

@Composable
private fun CountdownContent(
    countdownValue: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = countdownValue.toString(),
        color = BattleRopeTheme.colors.blue50,
        style = BattleRopeTheme.typography.headingXLSB.copy(fontSize = 120.sp),
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Composable
private fun PlayingContent(
    uiState: GameState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = formatTime(uiState.remainingTimeSec),
                    style = BattleRopeTheme.typography.headingXLSB,
                )

                if (uiState.feverState.isActive) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "🔥 FEVER TIME 🔥",
                        style = BattleRopeTheme.typography.headingLSB,
                        color = BattleRopeTheme.colors.primary,
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PlayerScoreDisplay(
                    label = "USER A",
                    playerScore = uiState.playerA,
                    isLeader = uiState.leader == Player.A,
                )

                PlayerScoreDisplay(
                    label = "USER B",
                    playerScore = uiState.playerB,
                    isLeader = uiState.leader == Player.B,
                )
            }
        }
    }
}

@Composable
private fun PlayerScore(
    label: String,
    score: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = BattleRopeTheme.typography.headingLSB,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = score.toString(),
            style = BattleRopeTheme.typography.headingXLSB.copy(fontSize = 72.sp),
        )
    }
}

@Composable
private fun PlayerScoreDisplay(
    label: String,
    playerScore: PlayerScore,
    isLeader: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = BattleRopeTheme.typography.headingLSB,
            color = if (isLeader) BattleRopeTheme.colors.primary else BattleRopeTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = playerScore.score.toString(),
            style = BattleRopeTheme.typography.headingXLSB.copy(fontSize = 64.sp),
            color = if (isLeader) BattleRopeTheme.colors.primary else BattleRopeTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "콤보 ${playerScore.combo}",
            style = BattleRopeTheme.typography.bodyMSB,
        )
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%d:%02d".format(minutes, secs)
}

@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 400,
)
@Composable
private fun GameScreenCountdownPreview() {
    BattleRopeTheme {
        GameScreen(
            uiState = GameState(
                phase = GamePhase.COUNTDOWN,
                countdownValue = 3,
            ),
            onBackClick = {},
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 400,
)
@Composable
private fun GameScreenPlayingPreview() {
    BattleRopeTheme {
        GameScreen(
            uiState = GameState(
                mode = GameMode.SPRINT,
                phase = GamePhase.PLAYING,
                remainingTimeSec = 25,
                playerA = PlayerScore(
                    player = Player.A,
                    score = 42,
                    combo = 5,
                ),
                playerB = PlayerScore(
                    player = Player.B,
                    score = 38,
                    combo = 3,
                ),
            ),
            onBackClick = {},
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 400,
)
@Composable
private fun GameScreenFinishedPreview() {
    BattleRopeTheme {
        GameScreen(
            uiState = GameState(
                mode = GameMode.SPRINT,
                phase = GamePhase.FINISHED,
                remainingTimeSec = 0,
                playerA = PlayerScore(
                    player = Player.A,
                    score = 87,
                ),
                playerB = PlayerScore(
                    player = Player.B,
                    score = 82,
                ),
            ),
            onBackClick = {},
        )
    }
}
