package com.choiminjun.makeitall.competition.game.engine

import com.choiminjun.makeitall.domain.model.ComboTier

class ComboTracker {
    fun update(currentCombo: Int, mappedDelta: Int): Pair<Int, ComboTier> {
        val newCombo = if (mappedDelta > 0) currentCombo + mappedDelta else 0
        val tier = ComboTier.entries
            .sortedByDescending { it.threshold }
            .first { newCombo >= it.threshold }
        return newCombo to tier
    }
}
