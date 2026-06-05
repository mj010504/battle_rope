package com.choiminjun.makeitall.domain.model

enum class GameMode(
    val durationSec: Int,
    val displayName: String,
) {
    SPRINT(30, "스프린트"),
    CLASSIC(60, "클래식"),
    ENDURANCE(120, "지구전"),
}
