# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew :app:assembleDebug
./gradlew :app:assembleRelease

# Unit tests
./gradlew :app:testDebugUnitTest

# Run a single test class
./gradlew :app:testDebugUnitTest --tests "com.valmiraguiar.listo.feature.lists.presentation.edit.EditListViewModelTest"

# Static analysis
./gradlew detekt
```

Detekt reports are written to `reports/detekt/`. Config lives in `config/detekt/detekt.yml`.

## Architecture

The app follows a strict three-layer architecture: **UI → Domain → Data**.

### Layer summary

| Layer | Location | Responsibility |
|---|---|---|
| UI | `feature/.../presentation/` | Compose screens, ViewModels, state/action/result classes |
| Domain | `feature/.../domain/` | Use cases, repository interfaces, domain models |
| Data | `feature/.../data/` | Repository implementations, Room DAOs, entities, local data sources |

ViewModels never reach into the data layer directly — they always go through use cases, which depend only on the repository interface.

### ViewModel pattern

Each screen has three state files in a `state/` sub-package:

- `*UiState` — `StateFlow` snapshot of what the UI should render.
- `*UiAction` — sealed interface of events dispatched by the screen via `viewModel.dispatch(action)`.
- `*UiResult` — `SharedFlow` of one-off events (navigation, error toasts) consumed once by the screen via `LaunchEffect`.

### Navigation (Navigation 3)

Routes are typed `NavKey` objects (`@Serializable data object` or `data class` for routes with arguments). Each feature declares its keys in a `*Key.kt` file and registers its screens via an extension function on `EntryProviderScope` in `*EntryProvider.kt`. The top-level `ListoApp` calls these entry-provider extension functions to build the `NavDisplay`.

`ListoNavigator` wraps a `ListoNavigationState` (a `SnapshotStateList`-backed back stack). Navigation extension functions like `navigateToListDetails(listId)` live alongside the keys in the feature's `navigation/` package.

### `FlowResult<Data, Error>`

Use cases that wrap repository flows emit `FlowResult<T, Throwable>`. The `onSuccess` / `onError` extension functions in `feature/common/extensions/FlowExt.kt` allow chaining handlers in a readable style without nested when-expressions.

### DI (Hilt)

Each feature has its own `di/` package with `@Module` classes that bind interfaces to their implementations. The `core/di/` package provides shared infrastructure (e.g., coroutine dispatchers).

### Room database

The database is `ShoppingListDatabase` (v2) with two tables: `shopping_lists` and `products` (CASCADE delete). Exported schema files live in `app/schemas/`. When adding a migration, add it to `ShoppingListDatabaseMigrations` and export the new schema.

Entity → domain model mapping happens in `ShoppingListLocalDataSourceImpl`, not in the DAOs or the repository implementation.

### Login prompt

The login prompt is intentionally separate from the shopping-list database. It is stored in `SharedPreferences` via `LoginPromptRepositoryImpl` and has its own use cases, repository interface, and DI module under `feature/login/`.

## Testing conventions

- Unit tests use **MockK** for mocking and `kotlinx-coroutines-test` with `StandardTestDispatcher`.
- Tests that need a `Main` dispatcher use the `MainDispatcherRule` helper class (defined inline in the test files).
- ViewModel tests prefer a hand-written `Fake*Repository` implementing the repository interface over MockK mocks — see `EditListViewModelTest` for the established pattern.
