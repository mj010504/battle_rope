# 코딩 컨벤션

## ViewModel Intent 네이밍
동사가 먼저 오는 형태: `VerbNoun`

```kotlin
// ✅ 올바른 예
data object ClickBack : HomeIntent
data class UpdateQuery(val query: String) : HomeIntent
data object FocusSearch : HomeIntent
data class ClickBusRoute(val routeId: String) : HomeIntent

// ❌ 잘못된 예
data object BackClick : HomeIntent
data class QueryUpdate(val query: String) : HomeIntent
```

## Composable 함수 파라미터 (콜백) 네이밍
| 콜백 종류 | 접두사 | 예시 |
|-----------|--------|------|
| 일반 UI 이벤트 | `on~` | `onBackClick`, `onQueryChange`, `onSearchFocused` |
| 화면 이동 (Navigation) | `navigate~` | `navigateToBusRoute`, `navigateToBusNode`, `navigateBack` |

```kotlin
// ✅ 올바른 예
fun HomeRoute(
    navigateToBusRoute: (String) -> Unit,   // 화면 이동
    navigateToBusNode: (String) -> Unit,    // 화면 이동
)

fun HomeScreen(
    onBackClick: () -> Unit,                // UI 이벤트
    onQueryChange: (String) -> Unit,        // UI 이벤트
    onSearchFocused: () -> Unit,            // UI 이벤트
)

fun NavGraphBuilder.homeGraph(
    navigateToBusRoute: (String) -> Unit,
    navigateToBusNode: (String) -> Unit,
    navigateBack: () -> Unit,
    navigateToAlarmSetting: (String) -> Unit,
)
```

## ViewModel private 함수 네이밍
Intent를 처리하는 ViewModel의 private 함수도 동사가 먼저 오는 `VerbNoun` 형태로 작성한다. `on~` 접두사를 사용하지 않는다.

```kotlin
// ✅ 올바른 예
private fun selectNode(node: BusNode) { ... }
private fun dismissBottomSheet() { ... }
private fun confirmAlarm() { ... }
private fun observeAlarm() { ... }

// ❌ 잘못된 예
private fun onSelectNode(node: BusNode) { ... }
private fun onDismissBottomSheet() { ... }
private fun onConfirmAlarm() { ... }
```

## init 블록 규칙
`init` 블록 내부의 로직은 반드시 private 함수로 추출하고, `init`에서는 함수 호출만 한다.

```kotlin
// ✅ 올바른 예
init {
    observeAlarm()
    loadNodes()
}
private fun observeAlarm() {
    viewModelScope.launch { ... }
}

// ❌ 잘못된 예
init {
    viewModelScope.launch {
        alarmRepository.observeAlarm().collect { ... }
    }
}
```

## 요약
| 위치 | 규칙 | 예시 |
|------|------|------|
| `sealed interface ~Intent` | 동사 먼저 | `ClickBack`, `UpdateQuery`, `FocusSearch` |
| ViewModel private 함수 | 동사 먼저, `on~` 금지 | `selectNode`, `confirmAlarm`, `observeAlarm` |
| Composable UI 콜백 파라미터 | `on~` | `onBackClick`, `onQueryChange` |
| Navigation 콜백 파라미터 | `navigate~` | `navigateToBusRoute`, `navigateBack` |

## ViewModel API 호출 규칙

ViewModel에서 suspend 함수(Repository API 호출 등)를 호출할 때는 `runCatching` 대신 `suspendRunCatching`을 사용한다.

- `runCatching`은 `CancellationException`을 삼켜 코루틴 취소가 제대로 동작하지 않는다.
- `suspendRunCatching`은 `CancellationException`을 다시 던져 취소를 안전하게 처리한다.


## 코드 수정 후 품질 검사 (필수)

코드를 수정하는 작업이 끝나면 반드시 아래 명령을 순서대로 실행하고, 실패 시 수정 후 재실행한다.

```bash
# 1. ktlint, detekt 정적 분석
./gradlew ktlintFormat detekt --auto-correct

# 2. 빌드 검증
./gradlew compileDebugKotlin
```

- 정적 분석 또는 빌드 실패 시 오류를 확인하고 코드를 수정한 뒤 다시 실행한다.
- 모든 단계가 `BUILD SUCCESSFUL`이 되어야 작업 완료로 간주한다.
