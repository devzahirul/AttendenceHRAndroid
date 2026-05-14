# Attendance HR - Multi-Modular Android Application

A professional-grade HR Management Android application built with Google L11 Android Engineer quality standards. This project follows modern Android development best practices with a clean, multi-modular architecture.

## 🏗️ Project Architecture

### Multi-Module Structure

```
AttendenceHR/
├── app/                          # Main application module
├── core/
│   ├── common/                   # Shared utilities, extensions, models
│   ├── database/                 # Room database, DAOs, repositories
│   ├── network/                  # Retrofit API clients, interceptors
│   └── ui/                       # Compose theme, shared components
└── features/                     # Feature modules (dynamic features ready)
    ├── auth/                     # Authentication & Login
    ├── dashboard/                # Main dashboard & analytics
    ├── attendance/               # Attendance tracking & check-in/out
    ├── employee/                 # Employee management & directory
    └── settings/                 # App settings & preferences
```

### Architecture Pattern: **Clean Architecture + MVVM**

Each feature module follows:
- **Presentation Layer**: Composables, ViewModels, State Management
- **Domain Layer**: Use Cases, Business Logic
- **Data Layer**: Repositories, DataSources, Models

## 📋 Technology Stack

### Android & Jetpack
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Kotlin**: 1.9.23
- **Java Version**: 11

### UI Framework
- **Jetpack Compose**: Modern declarative UI toolkit
- **Material 3**: Latest Material Design system
- **Navigation Compose**: Type-safe navigation

### Architecture & DI
- **Hilt**: Dependency injection
- **MVVM**: Architecture pattern
- **Coroutines**: Async programming
- **Flow**: Reactive data streams

### Database & Local Storage
- **Room**: Local database ORM
- **DataStore**: Preferences & configuration

### Networking
- **Retrofit**: HTTP client
- **OkHttp**: HTTP interceptors & logging
- **Kotlinx Serialization**: JSON serialization

### Testing
- **JUnit 4**: Unit testing
- **Mockk**: Mocking library
- **Turbine**: Flow testing
- **Espresso**: UI testing
- **Compose UI Test**: Compose component testing

### Code Quality
- **Detekt**: Static analysis
- **KtLint**: Kotlin code formatting
- **ProGuard/R8**: Code obfuscation & optimization

## 🚀 Getting Started

### Prerequisites
- Android Studio Giraffe or newer
- JDK 11+
- Android SDK 34+

### Building the Project

```bash
# Clone repository
git clone https://github.com/yourusername/AttendenceHRAndroid.git
cd AttendenceHRAndroid

# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Build release APK
./gradlew assembleRelease

# Code quality checks
./gradlew ktlintCheck detekt
```

### Running on Device/Emulator

```bash
# Install and run debug app
./gradlew installDebug
adb shell am start -n com.attendancehr/.MainActivity

# Run specific feature tests
./gradlew :features:attendance:test
```

## 📦 Module Descriptions

### Core Modules

#### `core:common`
Shared utilities, extensions, and base classes
- Result wrapper for error handling
- Common data models
- Extension functions
- Utility helpers

#### `core:database`
Local database layer using Room
- Entity definitions
- Database DAOs
- Local data sources
- Migration strategies

#### `core:network`
Remote API communication
- Retrofit service definitions
- API interceptors
- Serialization adapters
- Error handling

#### `core:ui`
Shared UI components and theming
- Material 3 theme
- Reusable Composables
- Design tokens
- Custom components

### Feature Modules

#### `features:auth`
User authentication
- Login/Register screens
- Session management
- Credential handling
- Authentication state

#### `features:dashboard`
Main application dashboard
- Analytics & insights
- Quick actions
- User information
- Recent activities

#### `features:attendance`
Attendance tracking system
- Check-in/out functionality
- Attendance records
- History & reports
- Location-based tracking

#### `features:employee`
Employee management
- Employee directory
- Profile management
- Team information
- Performance tracking

#### `features:settings`
Application settings
- User preferences
- Notification settings
- Theme configuration
- Account settings

## 🧪 Testing Strategy

### Unit Tests
```bash
./gradlew test
```

### Integration Tests
```bash
./gradlew connectedAndroidTest
```

### Code Coverage
```bash
./gradlew testDebugUnitTest
```

### Test Structure
- Each module has `src/test` for unit tests
- Shared testing utilities in `core:common`
- Mock objects and test doubles using Mockk
- Flow testing with Turbine

## 📊 Code Quality Standards

### KtLint
Auto-formats Kotlin code to official style
```bash
./gradlew ktlintFormat
```

### Detekt
Comprehensive static analysis
```bash
./gradlew detekt
```

### Configuration
- detekt.yml: Comprehensive detekt rules
- .editorconfig: IDE formatting rules
- gradle.properties: Build optimization

## 🔄 CI/CD Pipeline

GitHub Actions workflow (`.github/workflows/android-ci.yml`):
1. **Lint**: KtLint & Detekt code quality checks
2. **Unit Tests**: Run all unit tests
3. **Build**: Create debug APK
4. **Instrumented Tests**: UI and integration tests
5. **Release Build**: Build signed release APK

## 📱 Google L11 Engineer Standards

This project adheres to Google L11 Android Engineer quality standards:

### ✅ Architecture
- [x] Multi-modular, scalable structure
- [x] Clean Architecture principles
- [x] MVVM pattern implementation
- [x] Proper separation of concerns

### ✅ Code Quality
- [x] Automated code style enforcement (KtLint)
- [x] Static analysis (Detekt)
- [x] Type safety with Kotlin
- [x] Null safety & sealed classes

### ✅ Testing
- [x] Unit test coverage
- [x] Integration test framework
- [x] UI test examples
- [x] Test fixtures & mocking

### ✅ Performance
- [x] ProGuard/R8 minification
- [x] Resource shrinking
- [x] Dependency optimization
- [x] Coroutines for async work

### ✅ Security
- [x] ProGuard obfuscation
- [x] HTTPS enforcement
- [x] Certificate pinning ready
- [x] Secure data storage

### ✅ Accessibility
- [x] Material Design 3
- [x] Content descriptions ready
- [x] Color contrast compliance
- [x] Navigation accessibility

### ✅ Documentation
- [x] Module-level documentation
- [x] Code comments for complex logic
- [x] Architecture documentation
- [x] Setup instructions

## 📚 Development Guidelines

### Adding a New Feature

1. **Create feature module**
   ```bash
   mkdir -p features/newfeature/src/{main/java/com/attendancehr/features/newfeature,test}
   ```

2. **Update `settings.gradle.kts`**
   ```kotlin
   include(":features:newfeature")
   ```

3. **Create `build.gradle.kts`** (copy from existing feature)

4. **Structure**
   ```
   features/newfeature/
   ├── ui/               # Composables, ViewModels
   ├── domain/           # Use cases, repositories
   ├── data/             # API clients, local data
   └── navigation/       # Navigation graph
   ```

### Dependency Management

- Use `libs.versions.toml` for version catalog
- All dependencies centralized
- Version updates in one place
- Consistent across modules

### Testing Requirements

- Minimum 70% code coverage for critical paths
- Every public API has unit tests
- Integration tests for feature flows
- UI tests for user interactions

## 🔐 Security Considerations

- API secrets in local.properties (not in version control)
- SSL pinning for production APIs
- Secure SharedPreferences via EncryptedSharedPreferences
- Proper token refresh handling
- Input validation on all user inputs

## 🚦 Branching Strategy

- `main`: Production-ready code
- `develop`: Development integration branch
- `feature/*`: Feature development
- `bugfix/*`: Bug fixes
- `hotfix/*`: Production hotfixes

## 📞 Support & Issues

- Create GitHub issues for bugs
- Use pull request template
- Code review before merge
- Automated checks must pass

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

## 👨‍💻 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

---

**Built with Google L11 Android Engineer Quality Standards** ⭐