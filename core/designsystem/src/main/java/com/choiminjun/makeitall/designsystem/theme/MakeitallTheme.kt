package com.choiminjun.makeitall.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalTypography = staticCompositionLocalOf {
    MakeitallTypography()
}

/**
 * 앱 전역 테마.
 *
 * 색상 팔레트는 아직 정의하지 않고 Material3 기본 ColorScheme 을 사용한다.
 * Pretendard 기반 [MakeitallTypography] 를 CompositionLocal 로 제공한다.
 */
@Composable
fun MakeitallTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalTypography provides MakeitallTypography(),
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
}
