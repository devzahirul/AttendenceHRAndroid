# Architecture Documentation

## Overview

This project follows **Clean Architecture** combined with **MVVM** (Model-View-ViewModel) pattern. It's designed to be testable, maintainable, and scalable.

## Layered Architecture

```
Presentation Layer (UI)
    ↓
Domain Layer (Use Cases & Entities)
    ↓
Data Layer (Repositories, DataSources)
    ↓
External (Network, Database, Local Storage)
```

## Layer Responsibilities

### Presentation Layer (UI)
**Files**: `ui/screens`, `ui/components`, `ui/viewmodels`

Responsibilities:
- Jetpack Compose UI components
- ViewModels for state management
- Navigation handling
- User interaction handling

```kotlin
class LoginViewModel @Inject constructor(
    private val authUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val result = authUseCase(email, password)
            _uiState.value = when (result) {
                is Result.Success -> LoginUiState.Success
                is Result.Error -> LoginUiState.Error(result.exception.message)
                is Result.Loading -> LoginUiState.Loading
            }
        }
    }
}
```

### Domain Layer
**Files**: `domain/usecases`, `domain/entities`, `domain/repositories`

Responsibilities:
- Business logic (Use Cases)
- Entity definitions
- Repository interfaces (contracts)
- No Android dependencies

```kotlin
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return try {
            val user = authRepository.login(email, password)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
```

### Data Layer
**Files**: `data/repositories`, `data/datasources`, `data/models`

Responsibilities:
- Repository implementations
- API client calls
- Database operations
- Local data source management
- Data transformation

```kotlin
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userDao: UserDao,
) : AuthRepository {
    override suspend fun login(email: String, password: String): User {
        val response = authApi.login(LoginRequest(email, password))
        val user = response.toEntity()
        userDao.insert(user)
        return user
    }
}
```

## Dependency Injection with Hilt

### Module Setup

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com/")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        userDao: UserDao,
    ): AuthRepository = AuthRepositoryImpl(authApi, userDao)
}
```

### Injecting Dependencies

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: LoginUseCase,
) : ViewModel()
```

## State Management

### ViewModel Pattern

- Use `StateFlow` for reactive state
- Expose `AsStateFlow()` for immutability
- Perform operations in `viewModelScope`

```kotlin
sealed class UiState {
    object Loading : UiState()
    data class Success<T>(val data: T) : UiState()
    data class Error(val message: String) : UiState()
}
```

### Compose State Hoisting

State should be hoisted to the lowest common parent:

```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LoginContent(
        state = uiState,
        onLoginClick = { email, password ->
            viewModel.login(email, password)
        },
    )
}

@Composable
private fun LoginContent(
    state: LoginUiState,
    onLoginClick: (String, String) -> Unit,
) {
    // UI implementation
}
```

## Navigation

Using Jetpack Navigation Compose:

```kotlin
@Composable
fun RootNavGraph() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "auth",
    ) {
        authNavGraph(navController)
        dashboardNavGraph(navController)
        attendanceNavGraph(navController)
    }
}

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "login",
        route = "auth",
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
            )
        }
    }
}
```

## Error Handling

### Result Type Pattern

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

// Usage in repository
suspend fun getUsers(): Result<List<User>> = try {
    val response = api.getUsers()
    Result.Success(response.map { it.toEntity() })
} catch (e: Exception) {
    Result.Error(e)
}
```

## Testing Strategy

### Unit Testing

```kotlin
class LoginViewModelTest {
    private val authUseCase = mockk<LoginUseCase>()
    private val viewModel = LoginViewModel(authUseCase)

    @Test
    fun `login with valid credentials should succeed`() = runTest {
        val user = User(id = "1", name = "John Doe")
        coEvery { authUseCase("email@test.com", "password") } returns Result.Success(user)

        viewModel.login("email@test.com", "password")

        viewModel.uiState.test {
            assertEquals(LoginUiState.Loading, awaitItem())
            assertEquals(LoginUiState.Success, awaitItem())
        }
    }
}
```

### Integration Testing

Test across layers to ensure proper integration.

## Best Practices

### 1. Single Responsibility Principle
- Each class has one reason to change
- ViewModels handle state, not API calls
- Repositories handle data, not UI logic

### 2. Dependency Inversion
- Depend on abstractions (interfaces)
- Inject dependencies
- Use Hilt for dependency management

### 3. Immutability
- Use `data class` with `copy()`
- Expose `StateFlow` not `MutableStateFlow`
- Use sealed classes for type safety

### 4. Coroutines
- Always use `viewModelScope` in ViewModels
- Use `launch` for async operations
- Use `async` for parallel operations

### 5. Testing
- Mock external dependencies
- Test business logic in domain layer
- Test UI state changes in presentation layer
- Use test fixtures for common test data

## Module Dependencies

```
app → features:auth, features:dashboard, etc.
features:* → core:ui, core:common, core:network, core:database
core:ui → core:common
core:network → core:common
core:database → core:common
core:common → (no dependencies on other modules)
```

**Note**: Features should NOT depend on each other directly. Communicate through deep links or a navigation graph.

---

For more details, see individual module documentation.
