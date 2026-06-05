package com.choiminjun.makeitall.competition.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choiminjun.makeitall.designsystem.theme.MakeitallTheme
import com.choiminjun.makeitall.domain.model.GameResult
import com.choiminjun.makeitall.domain.model.Player
import com.choiminjun.makeitall.domain.model.PlayerScore

@Composable
fun GameResultSection(
    result: GameResult,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // 승자 표시
            val winnerText = when (result.winner) {
                Player.A -> "USER A 승리!"
                Player.B -> "USER B 승리!"
                null -> "무승부!"
            }
            Text(
                text = winnerText,
                style = MakeitallTheme.typography.headingXLSB.copy(fontSize = 48.sp),
                color = MakeitallTheme.colors.primary,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 플레이어 결과
            Row(
                horizontalArrangement = Arrangement.spacedBy(64.dp),
            ) {
                PlayerResult(
                    label = "USER A",
                    playerScore = result.playerA,
                )
                PlayerResult(
                    label = "USER B",
                    playerScore = result.playerB,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PlayerResult(
    label: String,
    playerScore: PlayerScore,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = MakeitallTheme.typography.headingLSB,
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatRow("최종 점수", playerScore.score.toString())
                StatRow("최고 콤보", playerScore.maxCombo.toString())
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatRow("총 점프", playerScore.rawJumpCount.toString())
                StatRow("줄걸림", playerScore.trippedCount.toString())
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatRow("추월", playerScore.overtakeCount.toString())
                StatRow("피버 점수", playerScore.feverScore.toString())
            }

        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "$label:",
            style = MakeitallTheme.typography.bodyMM,
            color = MakeitallTheme.colors.textSecondary,
        )
        Text(
            text = value,
            style = MakeitallTheme.typography.bodyMSB,
            color = MakeitallTheme.colors.textPrimary,
        )
    }
}
