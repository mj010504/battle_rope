package com.choiminjun.makeitall.competition.game.engine

import com.choiminjun.makeitall.domain.model.ComboTier

class ScoreCalculator {
    fun calculate(
        mappedDelta: Int,
        comboTier: ComboTier,
        isFeverActive: Boolean,
    ): Int {
        if (mappedDelta <= 0) return 0
        val comboMult = comboTier.multiplier
        val feverMult = if (isFeverActive) 2.0f else 1.0f
        return (mappedDelta * comboMult * feverMult).toInt()
    }
}
