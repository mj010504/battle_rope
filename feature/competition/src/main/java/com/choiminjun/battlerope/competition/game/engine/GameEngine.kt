package com.choiminjun.battlerope.competition.game.engine

import com.choiminjun.battlerope.domain.model.FeverState
import com.choiminjun.battlerope.domain.model.GameEvent
import com.choiminjun.battlerope.domain.model.GameMode
import com.choiminjun.battlerope.domain.model.Player
import com.choiminjun.battlerope.domain.model.PlayerScore
import timber.log.Timber
import kotlin.random.Random

data class TickResult(
    val playerA: PlayerScore,
    val playerB: PlayerScore,
    val feverState: FeverState,
    val events: List<GameEvent>,
)

class GameEngine(
    private val mode: GameMode,
    random: Random = Random.Default,
) {
    private val dataRandomizer = DataRandomizer()
    private val comboTracker = ComboTracker()
    private val feverManager = FeverManager(random)
    private val overtakeDetector = OvertakeDetector()
    private val scoreCalculator = ScoreCalculator()

    private var feverState: FeverState = feverManager.generateFeverWindow(mode.durationSec)

    fun tick(elapsedSec: Int, rawCountA: Int, rawCountB: Int, currentState: Pair<PlayerScore, PlayerScore>): TickResult {
        val events = mutableListOf<GameEvent>()
        val (prevA, prevB) = currentState
        Timber.d("rawCountA %s", rawCountA)
        Timber.d("rawCountB %s", rawCountB)
        // 랜덤 매핑
        val mappedA = dataRandomizer.mapDelta(rawCountA)
        val mappedB = dataRandomizer.mapDelta(rawCountB)

        // 줄걸림 이벤트 체크
        if (rawCountA > 0 && mappedA == 0) events.add(GameEvent.Tripped(Player.A))
        if (rawCountB > 0 && mappedB == 0) events.add(GameEvent.Tripped(Player.B))

        // 3. 콤보 업데이트
        val (newComboA, tierA) = comboTracker.update(prevA.combo, mappedA)
        val (newComboB, tierB) = comboTracker.update(prevB.combo, mappedB)

        // 콤보 마일스톤 체크
        if (newComboA > prevA.combo && newComboA >= 10 && newComboA % 10 == 0) {
            events.add(GameEvent.ComboMilestone(Player.A, newComboA))
        }
        if (newComboB > prevB.combo && newComboB >= 10 && newComboB % 10 == 0) {
            events.add(GameEvent.ComboMilestone(Player.B, newComboB))
        }

        // 4. 피버 체크
        val oldFeverActive = feverState.isActive
        feverState = feverManager.checkFeverActive(elapsedSec, feverState)
        if (!oldFeverActive && feverState.isActive) events.add(GameEvent.FeverStart)
        if (oldFeverActive && !feverState.isActive) events.add(GameEvent.FeverEnd)

        // 5. 점수 계산
        val scoreGainA = scoreCalculator.calculate(mappedA, tierA, feverState.isActive)
        val scoreGainB = scoreCalculator.calculate(mappedB, tierB, feverState.isActive)

        val newScoreA = prevA.score + scoreGainA
        val newScoreB = prevB.score + scoreGainB

        // 6. 추월 체크
        val overtakeResult = overtakeDetector.check(
            elapsedSec,
            prevA.score,
            prevB.score,
            newScoreA,
            newScoreB,
        )
        if (overtakeResult.occurred && overtakeResult.newLeader != null) {
            events.add(GameEvent.Overtake(overtakeResult.newLeader, overtakeResult.bonus))
        }

        // 7. 상태 업데이트
        val trippedA = if (rawCountA > 0 && mappedA == 0) 1 else 0
        val trippedB = if (rawCountB > 0 && mappedB == 0) 1 else 0
        val overtookA = if (overtakeResult.newLeader == Player.A) 1 else 0
        val overtookB = if (overtakeResult.newLeader == Player.B) 1 else 0
        val feverScoreA = if (feverState.isActive) scoreGainA else 0
        val feverScoreB = if (feverState.isActive) scoreGainB else 0

        val finalA = PlayerScore(
            player = Player.A,
            rawJumpCount = rawCountA,
            mappedJumpCount = prevA.mappedJumpCount + mappedA,
            score = newScoreA + if (overtakeResult.newLeader == Player.A) overtakeResult.bonus else 0,
            combo = newComboA,
            maxCombo = maxOf(prevA.maxCombo, newComboA),
            trippedCount = prevA.trippedCount + trippedA,
            overtakeCount = prevA.overtakeCount + overtookA,
            feverScore = prevA.feverScore + feverScoreA,
        )

        val finalB = PlayerScore(
            player = Player.B,
            rawJumpCount = rawCountB,
            mappedJumpCount = prevB.mappedJumpCount + mappedB,
            score = newScoreB + if (overtakeResult.newLeader == Player.B) overtakeResult.bonus else 0,
            combo = newComboB,
            maxCombo = maxOf(prevB.maxCombo, newComboB),
            trippedCount = prevB.trippedCount + trippedB,
            overtakeCount = prevB.overtakeCount + overtookB,
            feverScore = prevB.feverScore + feverScoreB,
        )

        return TickResult(finalA, finalB, feverState, events)
    }
}
