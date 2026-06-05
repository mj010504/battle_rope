package com.choiminjun.makeitall.competition.connection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.choiminjun.makeitall.designsystem.R
import com.choiminjun.makeitall.designsystem.theme.MakeitallTheme
import com.choiminjun.makeitall.domain.model.BleConnectionState

@Composable
fun DeviceConnectionRoute(
    navigateBack: () -> Unit,
    navigateToModeSelection: () -> Unit,
    viewModel: DeviceConnectionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            DeviceConnectionSideEffect.NavigateBack -> navigateBack()
            DeviceConnectionSideEffect.NavigateToModeSelection -> navigateToModeSelection()
        }
    }

    DeviceConnectionScreen(
        uiState = uiState,
        onBackClick = { viewModel.onIntent(DeviceConnectionIntent.ClickBack) },
        onGameStartClick = { viewModel.onIntent(DeviceConnectionIntent.ClickGameStart) },
    )
}

@Composable
fun DeviceConnectionScreen(
    uiState: DeviceConnectionState,
    onBackClick: () -> Unit,
    onGameStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 20.dp)
    ) {
        IconButton(
            modifier = Modifier.padding(start = 20.dp),
            onClick = onBackClick,
        ) {
            Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left), contentDescription = null)
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {



            if (uiState.isConnected) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "줄넘기 연결 완료",
                        style = MakeitallTheme.typography.headingLSB,
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .padding(horizontal = 60.dp),
                        onClick = {
                            onGameStartClick()
                        },
                    ) {
                        Text(
                            text = "게임 시작",
                            color = MakeitallTheme.colors.white,
                            style = MakeitallTheme.typography.headingLSB,
                        )
                    }
                }
            } else {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.jump_rope))
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = LottieConstants.IterateForever,
                )
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(200.dp),
                )
                Text(
                    text = "줄넘기 연결 중...",
                    style = MakeitallTheme.typography.headingLSB,
                    modifier = Modifier.padding(top = 24.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceConnectionScreenConnectingPreview() {
    MakeitallTheme {
        DeviceConnectionScreen(
            uiState = DeviceConnectionState(
                connectionState = BleConnectionState.Connecting,
            ),
            onBackClick = {},
            onGameStartClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceConnectionScreenConnectedPreview() {
    MakeitallTheme {
        DeviceConnectionScreen(
            uiState = DeviceConnectionState(
                connectionState = BleConnectionState.Connected("JumpRope-01"),
                isConnected = true,
            ),
            onBackClick = {},
            onGameStartClick = {},
        )
    }
}
