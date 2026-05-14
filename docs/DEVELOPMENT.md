# Development Guide

## Project Setup

### 1. Clone and Initial Setup

```bash
git clone https://github.com/devzahirul/AttendenceHRAndroid.git
cd AttendenceHRAndroid
git checkout develop

# Android Studio: File → Open → Select project root
```

### 2. Configure Local Environment

Create `local.properties` in project root:
```properties
sdk.dir=/path/to/Android/sdk
api.base.url=https://api-dev.example.com/
api.timeout=30
```

### 3. Build & Run

```bash
# Build debug version
./gradlew assembleDebug

# Install on device
./gradlew installDebug

# Run with debug logs
./gradlew :app:run
```

## Code Organization

### Directory Structure

```
features/attendance/
├── src/
│   ├── main/
│   │   ├── java/com/attendancehr/features/attendance/
│   │   │   ├── ui/
│   │   │   │   ├── screens/        # Composable screens
│   │   │   │   ├── components/     # Reusable components
│   │   │   │   └── viewmodels/     # ViewModels for screens
│   │   │   ├── domain/
│   │   │   │   ├── usecases/       # Business logic
│   │   │   │   ├── entities/       # Domain models
│   │   │   │   └── repositories/   # Repository interfaces
│   │   │   ├── data/
│   │   │   │   ├── repositories/   # Repository implementations
│   │   │   │   ├── datasources/    # API, Database sources
│   │   │   │   ├── models/         # Data transfer objects
│   │   │   │   └── di/             # Hilt modules
│   │   │   └── navigation/         # Navigation graph
│   │   └── res/
│   │       ├── values/
│   │       ├── drawable/
│   │       └── navigation/
│   └── test/                       # Unit tests
│       └── java/...
└── build.gradle.kts
```

### Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Kotlin Files | PascalCase.kt | `LoginViewModel.kt` |
| Classes | PascalCase | `LoginViewModel` |
| Functions | camelCase | `getAttendanceRecords()` |
| Variables | camelCase | `attendanceList` |
| Constants | SCREAMING_SNAKE_CASE | `CACHE_SIZE` |
| UI State | *UiState | `LoginUiState` |
| ViewModels | *ViewModel | `LoginViewModel` |
| Use Cases | *UseCase | `LoginUseCase` |
| Repositories | *Repository | `AuthRepository` |

## Coding Standards

### Kotlin Style Guide

Follow [Kotlin Official Style Guide](https://kotlinlang.org/docs/coding-conventions.html)

#### File Layout

```kotlin
// 1. Package declaration
package com.attendancehr.features.auth.ui

// 2. Imports
import androidx.compose.foundation.layout.Box
import androidx.hilt.navigation.compose.hiltViewModel

// 3. Top-level declarations
typealias UserMapper = (ApiUser) -> User

// 4. Class/Interface/Object declarations
class LoginViewModel @Inject constructor(
    private val useCase: LoginUseCase,
) : ViewModel()
```

#### Naming and Formatting

```kotlin
// Bad
fun l(x: Int, y: Int): Int = x + y
val O = 10

// Good
fun add(x: Int, y: Int): Int = x + y
val MAX_USERS = 10
```

### Compose Best Practices

#### Composable Guidelines

```kotlin
// ✅ Correct
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LoginContent(state, viewModel::login)
}

// ❌ Avoid
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Button(onClick = { viewModel.login() })
}
```

#### State Hoisting

```kotlin
// ✅ Correct - State hoisted to parent
@Composable
fun ParentScreen() {
    var count by remember { mutableStateOf(0) }
    ChildComponent(count = count, onCountChange = { count = it })
}

@Composable
fun ChildComponent(count: Int, onCountChange: (Int) -> Unit) {
    Button(onClick = { onCountChange(count + 1) })
}

// ❌ Avoid - State trapped in child
@Composable
fun ChildComponent() {
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ })
}
```

## Feature Development Workflow

### Step 1: Create Feature Module

```bash
mkdir -p features/newfeature/src/{main,test}
mkdir -p features/newfeature/src/main/java/com/attendancehr/features/newfeature/{ui,domain,data}
```

### Step 2: Create Gradle File

Copy from existing feature and update namespace:

```kotlin
android {
    namespace = "com.attendancehr.features.newfeature"
}
```

### Step 3: Update settings.gradle.kts

```kotlin
include(":features:newfeature")
```

### Step 4: Structure Feature

```
features/newfeature/
├── domain/
│   ├── entities/
│   │   └── NewFeatureEntity.kt
│   ├── repositories/
│   │   └── NewFeatureRepository.kt
│   └── usecases/
│       └── GetNewFeatureUseCase.kt
├── data/
│   ├── api/
│   │   └── NewFeatureApi.kt
│   ├── dao/
│   │   └── NewFeatureDao.kt
│   └── repositories/
│       └── NewFeatureRepositoryImpl.kt
├── ui/
│   ├── screens/
│   │   └── NewFeatureScreen.kt
│   ├── components/
│   │   └── NewFeatureCard.kt
│   └── viewmodels/
│       └── NewFeatureViewModel.kt
└── navigation/
    └── NewFeatureNavigation.kt
```

## Testing Guidelines

### Unit Test Structure

```kotlin
class GetNewFeatureUseCaseTest {
    private val repository = mockk<NewFeatureRepository>()
    private val useCase = GetNewFeatureUseCase(repository)

    @Test
    fun `invoke returns success when repository succeeds`() = runTest {
        // Arrange
        val expectedData = listOf(NewFeatureEntity(id = 1, name = "Test"))
        coEvery { repository.getItems() } returns expectedData

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedData, (result as Result.Success).data)
    }
}
```

### Test Coverage

Aim for:
- **Core modules**: 80%+ coverage
- **Feature modules**: 70%+ coverage
- **Critical paths**: 100% coverage

Run coverage:
```bash
./gradlew testDebugUnitTest
```

## Code Quality Checks

### Before Committing

```bash
# Format code
./gradlew ktlintFormat

# Check formatting
./gradlew ktlintCheck

# Run static analysis
./gradlew detekt

# Run tests
./gradlew test

# Build debug APK
./gradlew assembleDebug
```

### Pre-commit Hook

Create `.git/hooks/pre-commit`:

```bash
#!/bin/bash
./gradlew ktlintCheck && ./gradlew detekt
if [ $? -ne 0 ]; then
    echo "Code quality checks failed"
    exit 1
fi
```

## Git Workflow

### Branch Naming

```
feature/[task-id]-short-description
bugfix/[task-id]-short-description
hotfix/[task-id]-short-description
docs/[task-id]-short-description
```

### Commit Messages

```
[FEATURE] Add user authentication

- Implement login screen with email/password
- Add authentication use case
- Integrate with auth API
- Add unit tests

Closes #123
```

### Pull Request Process

1. **Branch from `develop`**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/ATT-123-add-login
   ```

2. **Make Changes & Test**
   ```bash
   # Code, test, format
   ./gradlew ktlintFormat test
   ```

3. **Push & Create PR**
   ```bash
   git push origin feature/ATT-123-add-login
   # Create PR on GitHub
   ```

4. **Code Review**
   - Address reviewer comments
   - Update PR if needed

5. **Merge**
   - Squash commits
   - Use conventional commit message

## Debugging

### Logcat Filtering

```bash
# Show only app logs
adb logcat | grep attendancehr

# Show errors and warnings
adb logcat *:W

# Dump to file
adb logcat > logcat.log
```

### Android Studio Debugger

1. Set breakpoint (click line number)
2. Run app in debug mode (Shift + F9)
3. Step through code (F10 = step over, F11 = step into)

### Profiler

- Monitor → Profiler
- Check CPU, Memory, Network, Battery

## Performance Tips

### Compose Performance

```kotlin
// ❌ Bad - Recomposes entire list
@Composable
fun UserList(users: List<User>) {
    Column {
        users.forEach { user ->
            UserCard(user)
        }
    }
}

// ✅ Good - Memoized items
@Composable
fun UserList(users: List<User>) {
    LazyColumn {
        items(users, key = { it.id }) { user ->
            UserCard(user)
        }
    }
}
```

### Memory Optimization

- Avoid memory leaks with Hilt scopes
- Use `remember` wisely in Compose
- Clear listeners when ViewModel clears
- Use `viewLifecycleOwner` for lifecycle-aware operations

## Troubleshooting

### Build Issues

```bash
# Clean build
./gradlew clean build

# Clear gradle cache
rm -rf ~/.gradle/caches
./gradlew clean build

# Check dependencies
./gradlew dependencyInsight --configuration debugRuntimeClasspath --dependency com.example
```

### Dependency Conflicts

```bash
./gradlew app:dependencies
```

### Hilt Issues

- Ensure `@HiltAndroidApp` in Application
- Check all `@Inject` constructors are valid
- Clear project and rebuild

---

For additional help, see:
- [Architecture Documentation](./ARCHITECTURE.md)
- [Testing Guide](./TESTING.md)
- [Kotlin Docs](https://kotlinlang.org/docs/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
