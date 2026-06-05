package com.choiminjun.makeitall.competition.game.engine

import com.choiminjun.makeitall.domain.model.Player

data class OvertakeResult(
    val occurred: Boolean,
    val newLeader: Player? = null,
    val bonus: Int = 0,
)

class OvertakeDetector {
    fun check(
        elapsedSec: Int,
        prevScoreA: Int,
        prevScoreB: Int,
        currScoreA: Int,
        currScoreB: Int,
    ): OvertakeResult {
        if (elapsedSec < 10) return OvertakeResult(false)

        val prevLeader = when {
            prevScoreA > prevScoreB -> Player.A
            prevScoreB > prevScoreA -> Player.B
            else -> null
        }

        val currLeader = when {
            currScoreA > currScoreB -> Player.A
            currScoreB > currScoreA -> Player.B
            else -> null
        }

        if (prevLeader != currLeader && currLeader != null) {
            return OvertakeResult(true, currLeader, 50)
        }

        return OvertakeResult(false)
    }
}
