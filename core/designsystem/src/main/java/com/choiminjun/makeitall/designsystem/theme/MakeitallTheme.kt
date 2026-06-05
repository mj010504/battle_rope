package com.choiminjun.makeitall.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalTypography = staticCompositionLocalOf {
    MakeitallTypography()
}

private val LocalColors = staticCompositionLocalOf {
    LightMakeitallColors
}

/**
 * 앱 전역 테마.
 *
 * Pretendard 기반 [MakeitallTypography] 와 [MakeitallColors] 를 CompositionLocal 로 제공한다.
 */
@Composable
fun MakeitallTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalTypography provides MakeitallTypography(),
        LocalColors provides LightMakeitallColors,
    ) {
        MaterialTheme(
            content = content,
        )
    }
}

object MakeitallTheme {
    val typography: MakeitallTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val colors: MakeitallColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current
}
