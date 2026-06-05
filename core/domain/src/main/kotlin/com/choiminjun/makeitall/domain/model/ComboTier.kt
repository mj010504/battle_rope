package com.choiminjun.makeitall.domain.model

enum class ComboTier(
    val threshold: Int,
    val multiplier: Float,
) {
    NONE(0, 1.0f),
    BRONZE(10, 1.2f),
    SILVER(30, 1.5f),
    GOLD(50, 2.0f),
    PLATINUM(80, 2.5f),
}
