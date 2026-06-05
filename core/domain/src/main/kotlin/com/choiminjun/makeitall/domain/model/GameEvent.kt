package com.choiminjun.makeitall.domain.model

sealed interface GameEvent {
    data class Tripped(val player: Player) : GameEvent
    data class Overtake(val player: Player, val bonus: Int) : GameEvent
    data object FeverStart : GameEvent
    data object FeverEnd : GameEvent
    data class ComboMilestone(val player: Player, val combo: Int) : GameEvent
}
