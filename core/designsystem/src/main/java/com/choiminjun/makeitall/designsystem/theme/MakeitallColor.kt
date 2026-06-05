package com.choiminjun.makeitall.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// ─── Cool Neutral ───
val CoolNeutral99 = Color(0xFFFCFCFC)
val CoolNeutral98 = Color(0xFFF7F8F9)
val CoolNeutral97 = Color(0xFFF4F5F6)
val CoolNeutral96 = Color(0xFFF1F2F4)
val CoolNeutral95 = Color(0xFFEDEEF0)
val CoolNeutral90 = Color(0xFFDFE1E5)
val CoolNeutral80 = Color(0xFFC2C4C8)
val CoolNeutral70 = Color(0xFFA4A7AD)
val CoolNeutral60 = Color(0xFF878A91)
val CoolNeutral50 = Color(0xFF6B6E76)
val CoolNeutral40 = Color(0xFF54565C)
val CoolNeutral30 = Color(0xFF3D3E43)
val CoolNeutral25 = Color(0xFF34353A)
val CoolNeutral23 = Color(0xFF2F3034)
val CoolNeutral22 = Color(0xFF2D2E32)
val CoolNeutral20 = Color(0xFF292A2D)
val CoolNeutral17 = Color(0xFF232427)
val CoolNeutral15 = Color(0xFF1F2023)
val CoolNeutral10 = Color(0xFF17181A)
val CoolNeutral7 = Color(0xFF101112)
val CoolNeutral5 = Color(0xFF0B0C0D)

// ─── Blue (brand primary) ───
val Blue99 = Color(0xFFF2F7FF)
val Blue95 = Color(0xFFE0EBFF)
val Blue90 = Color(0xFFC2D8FF)
val Blue80 = Color(0xFF85B0FF)
val Blue70 = Color(0xFF5290FF)
val Blue60 = Color(0xFF2A77FF)
val Blue50 = Color(0xFF0066FF) // Brand blue
val Blue40 = Color(0xFF0052CC)
val Blue30 = Color(0xFF003D99)
val Blue22 = Color(0xFF002C70)
val Blue20 = Color(0xFF002966)
val Blue15 = Color(0xFF001F4D)
val Blue10 = Color(0xFF001533)

// ─── Red ───
val Red99 = Color(0xFFFFF3F2)
val Red95 = Color(0xFFFFDBD7)
val Red90 = Color(0xFFFFB5AC)
val Red80 = Color(0xFFFF7C6E)
val Red70 = Color(0xFFFF4836)
val Red60 = Color(0xFFD11A0A)
val Red50 = Color(0xFF9C1208)
val Red40 = Color(0xFF660C05)
val Red30 = Color(0xFF4A0904)

// ─── Green ───
val Green99 = Color(0xFFF1FFF6)
val Green95 = Color(0xFFC9F7D7)
val Green90 = Color(0xFF95EBAE)
val Green80 = Color(0xFF41D175)
val Green70 = Color(0xFF1FB055)
val Green60 = Color(0xFF008E3C)
val Green50 = Color(0xFF007531)
val Green30 = Color(0xFF00431B)

// ─── Yellow ───
val Yellow99 = Color(0xFFFFFBEC)
val Yellow95 = Color(0xFFFFF1B8)
val Yellow90 = Color(0xFFFFE070)
val Yellow80 = Color(0xFFFFC400)
val Yellow60 = Color(0xFFC29200)
val Yellow46 = Color(0xFF8C6900)
val Yellow30 = Color(0xFF523D00)

@Immutable
data class MakeitallColors(
    // coolNeutral
    val coolNeutral99: Color,
    val coolNeutral98: Color,
    val coolNeutral97: Color,
    val coolNeutral96: Color,
    val coolNeutral95: Color,
    val coolNeutral90: Color,
    val coolNeutral80: Color,
    val coolNeutral70: Color,
    val coolNeutral60: Color,
    val coolNeutral50: Color,
    val coolNeutral40: Color,
    val coolNeutral30: Color,
    val coolNeutral25: Color,
    val coolNeutral23: Color,
    val coolNeutral22: Color,
    val coolNeutral20: Color,
    val coolNeutral17: Color,
    val coolNeutral15: Color,
    val coolNeutral10: Color,
    val coolNeutral7: Color,
    val coolNeutral5: Color,

    // blue
    val blue99: Color,
    val blue95: Color,
    val blue90: Color,
    val blue80: Color,
    val blue70: Color,
    val blue60: Color,
    val blue50: Color,
    val blue40: Color,
    val blue30: Color,
    val blue22: Color,
    val blue20: Color,
    val blue15: Color,
    val blue10: Color,

    // Semantic
    val background: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val icon: Color,
    val yellow: Color,
    val red: Color,
    val green: Color,
    val black: Color,
    val white: Color,
    val error: Color,

    // Game-specific
    val primary: Color,
    val fever: Color,
    val tripped: Color,
    val combo: Color,
)

val LightMakeitallColors = MakeitallColors(
    // coolNeutral
    coolNeutral99 = CoolNeutral99,
    coolNeutral98 = CoolNeutral98,
    coolNeutral97 = CoolNeutral97,
    coolNeutral96 = CoolNeutral96,
    coolNeutral95 = CoolNeutral95,
    coolNeutral90 = CoolNeutral90,
    coolNeutral80 = CoolNeutral80,
    coolNeutral70 = CoolNeutral70,
    coolNeutral60 = CoolNeutral60,
    coolNeutral50 = CoolNeutral50,
    coolNeutral40 = CoolNeutral40,
    coolNeutral30 = CoolNeutral30,
    coolNeutral25 = CoolNeutral25,
    coolNeutral23 = CoolNeutral23,
    coolNeutral22 = CoolNeutral22,
    coolNeutral20 = CoolNeutral20,
    coolNeutral17 = CoolNeutral17,
    coolNeutral15 = CoolNeutral15,
    coolNeutral10 = CoolNeutral10,
    coolNeutral7 = CoolNeutral7,
    coolNeutral5 = CoolNeutral5,

    // blue
    blue99 = Blue99,
    blue95 = Blue95,
    blue90 = Blue90,
    blue80 = Blue80,
    blue70 = Blue70,
    blue60 = Blue60,
    blue50 = Blue50,
    blue40 = Blue40,
    blue30 = Blue30,
    blue22 = Blue22,
    blue20 = Blue20,
    blue15 = Blue15,
    blue10 = Blue10,

    // Semantic - Light Theme
    background = CoolNeutral99,
    textPrimary = CoolNeutral10,
    textSecondary = CoolNeutral50,
    icon = CoolNeutral60,
    yellow = Yellow80,
    red = Red60,
    green = Green60,
    black = Color(0xFF000000),
    white = Color(0xFFFFFFFF),
    error = Red60,

    // Game-specific - Light Theme
    primary = Blue50,
    fever = Yellow80,
    tripped = Red60,
    combo = Green60,
)

val DarkMakeitallColors = MakeitallColors(
    // coolNeutral
    coolNeutral99 = CoolNeutral99,
    coolNeutral98 = CoolNeutral98,
    coolNeutral97 = CoolNeutral97,
    coolNeutral96 = CoolNeutral96,
    coolNeutral95 = CoolNeutral95,
    coolNeutral90 = CoolNeutral90,
    coolNeutral80 = CoolNeutral80,
    coolNeutral70 = CoolNeutral70,
    coolNeutral60 = CoolNeutral60,
    coolNeutral50 = CoolNeutral50,
    coolNeutral40 = CoolNeutral40,
    coolNeutral30 = CoolNeutral30,
    coolNeutral25 = CoolNeutral25,
    coolNeutral23 = CoolNeutral23,
    coolNeutral22 = CoolNeutral22,
    coolNeutral20 = CoolNeutral20,
    coolNeutral17 = CoolNeutral17,
    coolNeutral15 = CoolNeutral15,
    coolNeutral10 = CoolNeutral10,
    coolNeutral7 = CoolNeutral7,
    coolNeutral5 = CoolNeutral5,

    // blue
    blue99 = Blue99,
    blue95 = Blue95,
    blue90 = Blue90,
    blue80 = Blue80,
    blue70 = Blue70,
    blue60 = Blue60,
    blue50 = Blue50,
    blue40 = Blue40,
    blue30 = Blue30,
    blue22 = Blue22,
    blue20 = Blue20,
    blue15 = Blue15,
    blue10 = Blue10,

    // Semantic - Dark Theme
    background = CoolNeutral10,
    textPrimary = CoolNeutral99,
    textSecondary = CoolNeutral60,
    icon = CoolNeutral50,
    yellow = Yellow80,
    red = Red70,
    green = Green70,
    black = Color(0xFF000000),
    white = Color(0xFFFFFFFF),
    error = Red70,

    // Game-specific - Dark Theme
    primary = Blue50,
    fever = Yellow80,
    tripped = Red70,
    combo = Green70,
)
