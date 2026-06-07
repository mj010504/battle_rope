package com.choiminjun.battlerope.domain.model

data class FeverState(
    val isActive: Boolean = false,
    val startSec: Int = -1,
    val endSec: Int = -1,
    val multiplier: Float = 2.0f,
)
