package com.choiminjun.battlerope.competition.game.engine

import kotlin.random.Random

class DataRandomizer {
    fun mapDelta(delta: Int): Int {
        // delta 값을 기반으로 하되, 매번 다른 시드가 생성되도록 조합 (예: hashCode 활용)
        val combinedSeed = (delta * 31) + System.nanoTime().toInt()
        val seededRandom = Random(combinedSeed)
        return seededRandom.nextInt(0, 11)
    }
}
