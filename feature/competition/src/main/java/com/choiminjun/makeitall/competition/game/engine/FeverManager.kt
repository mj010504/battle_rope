package com.choiminjun.makeitall.competition.game.engine

import com.choiminjun.makeitall.domain.model.FeverState
import kotlin.random.Random

class FeverManager(private val random: Random = Random.Default) {
    fun generateFeverWindow(totalDurationSec: Int): FeverState {
        val feverDuration = 20
        val minStart = (totalDurationSec * 0.2f).toInt()
        val maxStart = (totalDurationSec * 0.7f).toInt() - feverDuration
        val startSec = random.nextInt(minStart, maxStart.coerceAtLeast(minStart + 1))
        return FeverState(
            isActive = false,
            startSec = startSec,
            endSec = startSec + feverDuration,
            multiplier = 2.0f,
        )
    }

    fun checkFeverActive(elapsedSec: Int, feverState: FeverState): FeverState {
        val isActive = elapsedSec in feverState.startSec until feverState.endSec
        return feverState.copy(isActive = isActive)
    }
}
