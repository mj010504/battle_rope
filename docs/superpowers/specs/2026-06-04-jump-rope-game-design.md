# 스마트 줄넘기 게이미피케이션 설계서

## 개요

BLE 스마트 줄넘기 디바이스의 실시간 운동 데이터를 활용한 게이미피케이션 앱.
USER A와 USER B 두 사용자의 경쟁/협력 모드를 지원한다.

## 타겟 사용자

- 중고등학생 및 청소년 (13-18세)
- 경쟁과 성취감 중심의 게임 경험

## 게임 모드

### 1. 경쟁 모드 (Competition)
- 1:1 실시간 대결
- 사용자가 시간 선택 (30초 / 1분 / 2분)
- 포인트 시스템으로 승패 결정

### 2. 협력 모드 (Cooperation)
- 두 사용자가 함께 목표 점수 달성
- 난이도 선택 (쉬움/보통/어려움)
- 난이도에 따라 시간 자동 설정:
  - 쉬움: 2분, 목표 300점
  - 보통: 1분, 목표 200점
  - 어려움: 30초, 목표 150점

## 데이터 처리

### BLE 원본 데이터
- USER A: 1→3→5→7... (홀수, +2씩 증가)
- USER B: 2→4→6→8... (짝수, +2씩 증가)

### 랜덤 매핑
원본 데이터의 규칙성을 해소하기 위해 게임 레이어에서 랜덤 매핑 적용:
- 원본 증가량 (+2) → 랜덤 증가량 (0~4)
- 원본 데이터는 그대로 보존, 게임 점수 계산에만 변환값 사용

## 점수 시스템

| 항목 | 점수 |
|------|------|
| 1회 점프 | +1점 |
| 5콤보 달성 | +3점 보너스 |
| 10콤보 달성 | +7점 보너스 |
| 20콤보 달성 | +15점 보너스 |

### 콤보 규칙
- 매 초 점프가 있으면 콤보 누적
- 랜덤 매핑 결과가 0이면 콤보 즉시 리셋

## 시각적 테마: 화면 색상 점령

### 경쟁 모드
- 화면이 두 색상(A: 파랑, B: 빨강)으로 분할
- 점수 비율에 따라 경계선 이동
- 앞서면 내 색상 영역 확장

### 협력 모드
- 합산 점수로 보라색 영역이 채워짐
- 목표 달성 시 화면 전체가 보라색

## 상태별 피드백

### 경쟁 모드
| 상태 | 시각 효과 | 진동 |
|------|----------|------|
| 앞설 때 | 내 색상 밝아짐 | - |
| 추월할 때 | 화면 번쩍 | 짧은 진동 |
| 밀릴 때 | 빨간 경고 테두리 | 짧은 진동 |

### 협력 모드
| 상태 | 시각 효과 | 진동 |
|------|----------|------|
| 잘됨 | 화면 밝음, 초록 테두리 | - |
| 중간 | 기본 상태 | - |
| 못 미침 | 화면 어두움, 주황 테두리 | 약한 진동 |

## 결과 화면

- 승패 (경쟁) 또는 목표 달성 여부 (협력)
- 최종 점수
- 최고 콤보
- 총 점프 횟수

## 모듈 구조

```
feature/game/
├── navigation/
│   └── GameNavigation.kt
├── GameContract.kt
├── GameViewModel.kt
├── screen/
│   ├── GameModeSelectScreen.kt
│   ├── GameSettingScreen.kt
│   ├── GamePlayScreen.kt
│   └── GameResultScreen.kt
├── component/
│   ├── ColorBattleField.kt
│   ├── ScoreDisplay.kt
│   ├── TimerDisplay.kt
│   └── StatusOverlay.kt
└── engine/
    ├── GameEngine.kt
    ├── ScoreCalculator.kt
    └── DataRandomizer.kt

core/domain/model/
├── GameMode.kt
├── Difficulty.kt
├── GameConfig.kt
├── PlayerState.kt
├── BattleStatus.kt
├── CoopStatus.kt
├── GameResult.kt
└── PlayerStats.kt
```

## 화면 플로우

```
Home → GameModeSelect → GameSetting → GamePlay → GameResult
                                                      ↓
                                              [다시하기] → GamePlay
                                              [모드선택] → GameModeSelect
```

## 기타 결정사항

- 보상/성취 시스템: 없음 (단순화)
- 사운드: 없음
- 진동: 추월, 콤보, 게임 종료 등 주요 이벤트에 적용
- 기존 exercise 모듈: 테스트용으로 유지
