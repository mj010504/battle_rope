package com.choiminjun.battlerope.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalTypography = staticCompositionLocalOf {
    BattleRopeTypography()
}

private val LocalColors = staticCompositionLocalOf {
    LightBattleRopeColors
}

/**
 * 앱 전역 테마.
 *
 * Pretendard 기반 [BattleRopeTypography] 와 [BattleRopeColors] 를 CompositionLocal 로 제공한다.
 */
@Composable
fun BattleRopeTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalTypography provides BattleRopeTypography(),
        LocalColors provides LightBattleRopeColors,
    ) {
        MaterialTheme(
            content = content,
        )
    }
}

object BattleRopeTheme {
    val typography: BattleRopeTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val colors: BattleRopeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current
}
