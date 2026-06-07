package com.choiminjun.battlerope.competition.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.battlerope.designsystem.R
import com.choiminjun.battlerope.designsystem.component.BattleRopeButton
import com.choiminjun.battlerope.designsystem.theme.BattleRopeTheme
import com.choiminjun.battlerope.domain.model.GameMode

@Composable
fun ModeSelectionRoute(
    navigateBack: () -> Unit,
    navigateToGame: (GameMode) -> Unit,
    viewModel: ModeSelectionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            ModeSelectionSideEffect.NavigateBack -> navigateBack()
            is ModeSelectionSideEffect.NavigateToGame -> navigateToGame(sideEffect.mode)
        }
    }

    ModeSelectionScreen(
        uiState = uiState,
        onClickBack = { viewModel.onIntent(ModeSelectionIntent.ClickBack) },
        onModeClick = { mode -> viewModel.onIntent(ModeSelectionIntent.SelectMode(mode)) },
        onStartGameClick = { viewModel.onIntent(ModeSelectionIntent.ClickStartGame) },
    )
}

@Composable
fun ModeSelectionScreen(
    uiState: ModeSelectionState,
    onClickBack: () -> Unit,
    onModeClick: (GameMode) -> Unit,
    onStartGameClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 20.dp),
        ) {
            item {
                IconButton(
                    modifier = Modifier.padding(start = 20.dp),
                    onClick = onClickBack,
                ) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left), contentDescription = null)
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Box(
                        modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(BattleRopeTheme.colors.primary.copy(0.3f))
                            .padding(vertical = 8.dp, horizontal = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("경쟁 모드", color = BattleRopeTheme.colors.primary, style = BattleRopeTheme.typography.bodyMSB)
                    }

                    Spacer(Modifier.height(4.dp))
                    Text("시간 선택", color = BattleRopeTheme.colors.textPrimary, style = BattleRopeTheme.typography.headingLSB)
                    Spacer(Modifier.height(8.dp))
                    Text("대결 시간을 선택해주세요. 점수가 높은 쪽이 승리해요.", style = BattleRopeTheme.typography.bodyMM, color = BattleRopeTheme.colors.textSecondary)
                }

                Spacer(Modifier.height(48.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    uiState.availableModes.forEachIndexed { index, mode ->
                        if (index > 0) {
                            Spacer(modifier = Modifier.width(24.dp))
                        }
                        ModeButton(
                            mode = mode,
                            isSelected = mode == uiState.selectedMode,
                            onClick = { onModeClick(mode) },
                        )
                    }
                }
            }
        }
        BattleRopeButton(
            text = "시작하기",
            onClick = onStartGameClick,
            enabled = uiState.selectedMode != null,
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .padding(bottom = 48.dp)
                .align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun ModeButton(
    mode: GameMode,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BattleRopeTheme.colors.background)
            .border(
                width = 1.dp,
                color = if (isSelected) BattleRopeTheme.colors.primary else BattleRopeTheme.colors.coolNeutral90,
                shape = RoundedCornerShape(12.dp),
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            }
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "${mode.durationSec}초",
            style = BattleRopeTheme.typography.headingLSB,
            color = if (isSelected) BattleRopeTheme.colors.primary else BattleRopeTheme.colors.textPrimary,
        )
        Text(
            text = mode.displayName,
            style = BattleRopeTheme.typography.bodyMR,
            color = if (isSelected) BattleRopeTheme.colors.primary else BattleRopeTheme.colors.textPrimary,
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun ModeSelectionScreenPreview() {
    BattleRopeTheme {
        ModeSelectionScreen(
            uiState = ModeSelectionState(),
            onModeClick = {},
            onStartGameClick = {},
            onClickBack = {},
        )
    }
}
