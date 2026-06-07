package com.choiminjun.battlerope.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.choiminjun.battlerope.designsystem.R

private val PretendardFontFamily = FontFamily(
    FontWeight.Light.pretendardFont(),
    FontWeight.Normal.pretendardFont(),
    FontWeight.Medium.pretendardFont(),
    FontWeight.SemiBold.pretendardFont(),
    FontWeight.Bold.pretendardFont(),
)

@OptIn(ExperimentalTextApi::class)
private fun FontWeight.pretendardFont() = Font(
    R.font.pretendard_variable,
    weight = this,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
)

@Immutable
data class BattleRopeTypography(
    val headingXLSB: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
    ),
    val headingLSB: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
    ),
    val headingMSB: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
    ),
    val bodyLM: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    val bodyMSB: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    val bodyMM: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    ),
    val bodyMR: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    val bodySM: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    val bodySR: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    val bodyXSM: TextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
    ),
)
