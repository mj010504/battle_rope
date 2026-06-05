# 경쟁 모드 게임 엔진 상세 설계서

## 개요

스마트 줄넘기 앱의 경쟁 모드 게임 메카닉 및 엔진 설계. BLE 원시 데이터를 랜덤 매핑하고, 콤보/피버/추월 시스템으로 게이미피케이션을 구현한다.

## 게임 메카닉 요약

| 항목 | 내용 |
|------|------|
| **게임 모드** | SPRINT(30초), CLASSIC(60초), ENDURANCE(120초) |
| **랜덤 매핑** | BLE delta → 랜덤값 변환, 0이면 줄 걸림 (확률 ~8%) |
| **콤보** | 점프 기반, 줄 걸리면 리셋 |
| **콤보 보너스** | 누적 배율 (콤보↑ → 점프당 점수↑) |
| **피버타임** | 랜덤 구간, 20초간, 점수 2배 |
| **추월 보너스** | 순간 +10점, 게임 시작 10초 이후부터 가능 |

## 클래스 구조

```
feature/competition/src/main/java/.../game/
├── GameContract.kt          # UI 상태, Intent, SideEffect
├── GameViewModel.kt         # UI 상태 관리, Engine 연결
└── engine/
    ├── GameEngine.kt        # 메인 엔진 (조합)
    ├── DataRandomizer.kt    # BLE delta → 랜덤 매핑
    ├── ComboTracker.kt      # 콤보 추적 및 배율 계산
    ├── FeverManager.kt      # 피버타임 관리
    ├── OvertakeDetector.kt  # 추월 감지
    └── ScoreCalculator.kt   # 최종 점수 계산

core/domain/src/main/kotlin/.../model/
├── GameMode.kt              # (기존) SPRINT, CLASSIC, ENDURANCE
├── Player.kt                # 플레이어 식별 enum
├── PlayerScore.kt           # 플레이어별 점수 상태
├── ComboTier.kt             # 콤보 배율 티어
├── FeverState.kt            # 피버 상태
└── GameEvent.kt             # 이벤트 (줄걸림, 추월, 피버 등)
```

### 책임 분리

| 클래스 | 책임 |
|--------|------|
| `GameEngine` | 컴포넌트 조합, 1초마다 tick 처리 |
| `DataRandomizer` | delta → 랜덤값 변환, 줄걸림 판정 |
| `ComboTracker` | 콤보 카운트, 배율 계산 |
| `FeverManager` | 피버 구간 결정, 활성화 여부 |
| `OvertakeDetector` | 추월 감지, 10초 이후 체크 |
| `ScoreCalculator` | 기본점수 × 콤보배율 × 피버배율 + 보너스 |

## 데이터 모델

### Player

```kotlin
enum class Player { A, B }
```

### PlayerScore

```kotlin
data class PlayerScore(
    val player: Player,
    val rawJumpCount: Int = 0,      // BLE 원본 누적값
    val mappedJumpCount: Int = 0,   // 랜덤 매핑된 누적값
    val score: Int = 0,             // 최종 점수
    val combo: Int = 0,             // 현재 콤보
    val maxCombo: Int = 0,          // 최고 콤보
)
```

### ComboTier

```kotlin
enum class ComboTier(val threshold: Int, val multiplier: Float) {
    NONE(0, 1.0f),
    BRONZE(5, 1.2f),
    SILVER(10, 1.5f),
    GOLD(20, 2.0f),
    PLATINUM(30, 2.5f),
}
```

### FeverState

```kotlin
data class FeverState(
    val isActive: Boolean = false,
    val startSec: Int = -1,         // 피버 시작 시점
    val endSec: Int = -1,           // 피버 종료 시점
    val multiplier: Float = 2.0f,   // 피버 배율
)
```

### GameEvent

```kotlin
sealed interface GameEvent {
    data class Tripped(val player: Player) : GameEvent
    data class Overtake(val player: Player, val bonus: Int) : GameEvent
    data object FeverStart : GameEvent
    data object FeverEnd : GameEvent
    data class ComboMilestone(val player: Player, val combo: Int) : GameEvent
}
```

## 엔진 컴포넌트 상세

### DataRandomizer

- 줄 걸림 확률: ~8%
- 매핑 범위: 1 ~ (delta × 1.5)

```kotlin
fun mapDelta(delta: Int): Int {
    if (delta <= 0) return 0
    if (random.nextFloat() < 0.08f) return 0  // 줄 걸림
    val maxValue = (delta * 1.5f).toInt().coerceAtLeast(1)
    return random.nextInt(1, maxValue + 1)
}
```

### ComboTracker

- mappedDelta > 0 이면 콤보 누적
- mappedDelta == 0 이면 콤보 리셋
- 콤보 수에 따라 ComboTier 결정

```kotlin
fun update(currentCombo: Int, mappedDelta: Int): Pair<Int, ComboTier> {
    val newCombo = if (mappedDelta > 0) currentCombo + mappedDelta else 0
    val tier = ComboTier.entries
        .sortedByDescending { it.threshold }
        .first { newCombo >= it.threshold }
    return newCombo to tier
}
```

### FeverManager

- 피버 구간: 게임 시간의 20%~70% 사이에서 랜덤 시작
- 피버 지속: 20초
- 피버 배율: 2.0x

```kotlin
fun generateFeverWindow(totalDurationSec: Int): FeverState {
    val feverDuration = 20
    val minStart = (totalDurationSec * 0.2f).toInt()
    val maxStart = (totalDurationSec * 0.7f).toInt() - feverDuration
    val startSec = Random.nextInt(minStart, maxStart.coerceAtLeast(minStart + 1))
    return FeverState(startSec = startSec, endSec = startSec + feverDuration)
}
```

### OvertakeDetector

- 10초 이후부터 추월 감지 활성화
- 리더가 바뀌면 추월 보너스 +10점

```kotlin
fun check(elapsedSec: Int, prevA: Int, prevB: Int, currA: Int, currB: Int): OvertakeResult {
    if (elapsedSec < 10) return OvertakeResult(false)
    val prevLeader = compareScores(prevA, prevB)
    val currLeader = compareScores(currA, currB)
    if (prevLeader != currLeader && currLeader != null) {
        return OvertakeResult(true, currLeader, 10)
    }
    return OvertakeResult(false)
}
```

### ScoreCalculator

- 점수 = 매핑된 점프 × 콤보 배율 × 피버 배율

```kotlin
fun calculate(mappedDelta: Int, comboTier: ComboTier, isFeverActive: Boolean): Int {
    if (mappedDelta <= 0) return 0
    val comboMult = comboTier.multiplier
    val feverMult = if (isFeverActive) 2.0f else 1.0f
    return (mappedDelta * comboMult * feverMult).toInt()
}
```

### GameEngine

메인 엔진으로 모든 컴포넌트를 조합하여 `tick()` 메서드로 1초마다 게임 상태를 업데이트한다.

```kotlin
fun tick(elapsedSec: Int, rawCountA: Int, rawCountB: Int): TickResult {
    // 1. Delta 계산 (이전값과 비교)
    // 2. 랜덤 매핑
    // 3. 콤보 업데이트
    // 4. 피버 체크
    // 5. 점수 계산
    // 6. 상태 업데이트
    // 7. 추월 체크 및 보너스 적용
    return TickResult(playerA, playerB, feverState, events)
}
```

## UI 상태 (GameContract)

### GameState

```kotlin
data class GameState(
    val mode: GameMode = GameMode.CLASSIC,
    val phase: GamePhase = GamePhase.COUNTDOWN,
    val countdownValue: Int = 3,
    val remainingTimeSec: Int = 0,
    val playerA: PlayerScore = PlayerScore(Player.A),
    val playerB: PlayerScore = PlayerScore(Player.B),
    val feverState: FeverState = FeverState(),
    val leader: Player? = null,
    val recentEvent: GameEvent? = null,
) : UiState
```

### GameSideEffect

```kotlin
sealed interface GameSideEffect : UiSideEffect {
    data object NavigateBack : GameSideEffect
    data object Vibrate : GameSideEffect
    data class NavigateToResult(val result: GameResult) : GameSideEffect
}
```

## UI 피드백

| 이벤트 | 시각 효과 | 진동 |
|--------|----------|------|
| 피버 시작 | 화면 테두리 금색 글로우 | ✗ |
| 피버 중 | 배경 펄스 애니메이션 | ✗ |
| 추월 | 상대 영역 번쩍 + "추월!" 텍스트 | ✓ |
| 줄 걸림 | 내 점수 영역 빨간색 플래시 | ✓ (짧게) |
| 콤보 티어 상승 | 콤보 숫자 확대 애니메이션 | ✗ |

## 게임 화면 레이아웃

```
┌─────────────────────────────────────────────┐
│  [남은시간]           🔥피버🔥              │
├─────────────────────┬───────────────────────┤
│                     │                       │
│     USER A          │        USER B         │
│                     │                       │
│    [점수: 142]      │      [점수: 128]      │
│    [콤보: 15] 1.5x  │      [콤보: 8] 1.2x   │
│                     │                       │
│   ███████████████   │   █████████████       │
│                     │                       │
└─────────────────────┴───────────────────────┘
```

## 결과 화면

```kotlin
data class GameResult(
    val mode: GameMode,
    val winner: Player?,           // null이면 무승부
    val playerA: PlayerScore,
    val playerB: PlayerScore,
    val totalDurationSec: Int,
)
```

표시 항목:
- 승자 표시 (USER A 승리! / USER B 승리! / 무승부!)
- 최종 점수 비교
- 최고 콤보 비교
- 총 점프 횟수 비교
- [다시하기] [모드선택] 버튼

## 파일 목록

### 신규 생성

- `core/domain/.../model/Player.kt`
- `core/domain/.../model/PlayerScore.kt`
- `core/domain/.../model/ComboTier.kt`
- `core/domain/.../model/FeverState.kt`
- `core/domain/.../model/GameEvent.kt`
- `core/domain/.../model/GameResult.kt`
- `feature/competition/.../game/engine/GameEngine.kt`
- `feature/competition/.../game/engine/DataRandomizer.kt`
- `feature/competition/.../game/engine/ComboTracker.kt`
- `feature/competition/.../game/engine/FeverManager.kt`
- `feature/competition/.../game/engine/OvertakeDetector.kt`
- `feature/competition/.../game/engine/ScoreCalculator.kt`
- `feature/competition/.../result/GameResultScreen.kt`
- `feature/competition/.../result/GameResultViewModel.kt`
- `feature/competition/.../result/GameResultContract.kt`

### 수정

- `feature/competition/.../game/GameContract.kt`
- `feature/competition/.../game/GameViewModel.kt`
- `feature/competition/.../game/GameScreen.kt`
- `feature/competition/.../navigation/CompetitionNavigation.kt`
- `core/navigation/.../Route.kt`
