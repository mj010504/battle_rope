package com.choiminjun.makeitall.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.makeitall.designsystem.theme.MakeitallTheme
import com.choiminjun.makeitall.domain.model.BleConnectionState
import com.choiminjun.makeitall.domain.model.JumpRopeSnapshot
import timber.log.Timber

@Composable
fun ExerciseRoute(
    navigateBack: () -> Unit,
    viewModel: ExerciseViewModel = hiltViewModel(),
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            ExerciseSideEffect.NavigateBack -> {
                Timber.d("[ExerciseRoute] NavigateBack 사이드이펙트 수신 → navigateBack 호출")
                navigateBack()
            }
        }
    }

    ExerciseScreen(
        uiState = uiState,
        onBackClick = { viewModel.onIntent(ExerciseIntent.ClickBack) },
        onStartClick = { viewModel.onIntent(ExerciseIntent.ClickStartExercise) },
        onStopClick = { viewModel.onIntent(ExerciseIntent.ClickStopExercise) },
    )
}

@Composable
fun ExerciseScreen(
    uiState: ExerciseState,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (val connectionState = uiState.connectionState) {
            is BleConnectionState.Disconnected,
            is BleConnectionState.Connecting,
            -> {
                CircularProgressIndicator()
                Text(
                    text = "장치 연결 중...",
                    style = MakeitallTheme.typography.bodyMR,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }

            is BleConnectionState.Connected -> {
                val snapshot = uiState.snapshot
                if (snapshot != null) {
                    Text(
                        text = "USER A   ${snapshot.userACount}",
                        style = MakeitallTheme.typography.headingLSB,
                    )
                    Text(
                        text = "USER B   ${snapshot.userBCount}",
                        style = MakeitallTheme.typography.headingLSB,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                    Text(
                        text = "${snapshot.elapsedSec}초",
                        style = MakeitallTheme.typography.bodyMR,
                        modifier = Modifier.padding(top = 24.dp),
                    )
                } else {
                    Text(
                        text = "${connectionState.deviceName} 연결됨",
                        style = MakeitallTheme.typography.bodyMR,
                    )
                    Text(
                        text = "데이터 수신 대기 중...",
                        style = MakeitallTheme.typography.bodyMR,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Button(
                        onClick = onStartClick,
                        enabled = !uiState.isExerciseRunning,
                    ) {
                        Text(text = "운동 시작")
                    }
                    Button(
                        onClick = onStopClick,
                        enabled = uiState.isExerciseRunning,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                        ),
                    ) {
                        Text(text = "운동 중지")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseScreenDisconnectedPreview() {
    MakeitallTheme {
        ExerciseScreen(
            uiState = ExerciseState(),
            onBackClick = {},
            onStartClick = {},
            onStopClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseScreenConnectedPreview() {
    MakeitallTheme {
        ExerciseScreen(
            uiState = ExerciseState(
                connectionState = BleConnectionState.Connected("JumpRope-01"),
                snapshot = JumpRopeSnapshot(elapsedSec = 5, userACount = 10, userBCount = 9),
                isExerciseRunning = true,
            ),
            onBackClick = {},
            onStartClick = {},
            onStopClick = {},
        )
    }
}
