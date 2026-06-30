# Shopme Android App - AI Agent Guide

## Project Overview
Shopme is a multi-role Android marketplace application supporting both customers and sellers with real-time messaging, orders, and Firebase integration. Built with **Kotlin**, **Jetpack Compose**, **Clean Architecture**, and **MVVM+MVI patterns**.

## Architecture & Module Structure

### Core Layers (Data Flow)
```
Presentation (Features) → Domain (Use Cases) → Data (Repositories) → Network/Cache
```

**Key Modules:**
- **`app/`** - Application entry point, main activity, Firebase init, Hilt app setup
- **`domain/`** - Pure Kotlin models, use cases, repository interfaces (no Android deps)
- **`data/`** - Repository implementations, remote/local data sources, mappers, Room database
- **`core/`** - Database entities, app-wide error handling (`ApiException`), constant configurations
- **`common/`** - Shared UI constants, build config (BASE_URL, Firebase IDs), network setup
- **`feature-auth/`** - Login, registration, password reset flows
- **`feature-customer/`** - Customer-facing screens (home, search, cart, orders, profile, chat)
- **`feature-seller/`** - Seller dashboard, product management, store setup, order handling
- **`feature-firebase/`** - Firebase-specific integration (Firestore, Authentication, Messaging)
- **`nav/`** - Jetpack Navigation routes defined as sealed classes (e.g., `SellerDashboardRoute`, `CustomerHomeRoute`)
- **`common/`** - Shared utilities, HTTP interceptors, base URLs
- **`core/`** - Database utilities (Room), error definitions

## Critical Architecture Patterns

### 1. MVVM + MVI (Event/Effect/State)
**Pattern File:** `feature-*/contract/*.kt`
```kotlin
// Example: SellerDashboardContract.kt
data class SellerDashboardUiState(...)  // Immutable state
sealed class SellerDashboardEvent       // User interactions
sealed class SellerDashboardEffect      // One-time navigation/effects
```
- Events trigger ViewModel logic
- ViewModel emits State (for UI binding) + Effects (for navigation)
- Use `Turbine` library for testing flows
- **Strict Rule:** Never skip the contract file - always define UiState/Event/Effect first

### 2. Repository Pattern with Mappers
**Location:** `data/repository/`, `data/mapper/`
- **Data Sources:** `BaseRemoteDataSource` (Retrofit/Ktor), local Room repositories
- **Mappers:** Multi-level conversion pipeline:
  ```
  API Response → DTO → Domain Model → UI State
  Room Entity ↔ Domain Model
  ```
- **Request/Response Mappers:** Handle API contract transformations
- **Advantage:** Decoupled domain logic from network/database implementations

### 3. Use Cases (Domain Layer)
**Location:** `domain/usecase/`
- Single responsibility: one public `invoke()` function per use case
- Input: Parameters from presentation, Output: Flow<Resource<T>> or single value
- All async work wrapped in coroutines with proper scoping
- Example: `CreateFoodUseCase.kt` → `invoke(param: FoodUpsertParam)`

### 4. Dependency Injection (Hilt)
**Key Modules:**
- `data/di/DatabaseModule.kt` - Room DB, DAOs
- `data/di/RepositoryModule.kt` - Repository/DataSource bindings
- Feature modules inherit from `common/` dependencies
- **Constructor injection** is standard; Hilt handles scoping
- Use `@Singleton` for network clients, DB; `@ActivityScoped` for state management

### 5. Firebase Real-Time Integration
**Location:** `feature-firebase/`, `data/realtime/`
- **Firestore:** Real-time chat, notifications (see `ShopmeRealtimeGatewayImpl.kt`)
- **Authentication:** Firebase Auth with custom claims (seller/customer roles)
- **Messaging:** FCM for push notifications
- **Build Config:** `firebaseProjectId`, `firebaseDefaultCollection` resolved from `local.properties` or env vars
- **Scope Pattern:** Notifications scoped by `"customer"` or `"seller"` prefix in cache keys

## Build & Dependencies

### Gradle Setup
- **Version Catalog:** `gradle/libs.versions.toml` (centralized versions)
- **Kotlin:** 2.0.21, Android API 36 (target), min API 24
- **Plugins:** KSP (annotation processor), Hilt, Compose compiler, kotlinx-serialization
- **Key Libs:**
  - Network: Retrofit 2.9.0 + Moshi/Gson, OkHttp 4.11.0, Ktor client 2.3.5
  - Firebase: BOM 33.5.1, Firestore, Auth, Messaging
  - Database: Room 2.6.1, ThreeTenABP (date/time)
  - Testing: JUnit 4, Mockk 1.13.10, Turbine 1.1.0, coroutines-test

### Build Configuration
**Source Compatibility:** Java 11 (and Kotlin JVM target 11)
- `local.properties`: Base URLs, Firebase project ID, keystore paths
- Environment Variables: Fallback for CI/CD (SHOPME_BASE_URL, SHOPME_FIREBASE_PROJECT_ID, etc.)
- **ProGuard:** Minification disabled for now; consumer rules in each module

### Launching Debug Build
```bash
# Set up local.properties with:
shopme.baseUrl=http://192.168.1.104:8080/
shopme.firebaseProjectId=your-project-id

# Build
./gradlew assembleDebug
./gradlew installDebug

# With Chucker (HTTP interceptor) for debugging
./gradlew assembleDebug -Penv=debug  # Includes chucker in debugImplementation
```

## Network & API Communication

### BaseRemoteDataSource Pattern
**File:** `data/base/BaseRemoteDataSource.kt`
- Abstract generic `request<R>()` function handles:
  - HTTP status validation (200-299 = success)
  - Error parsing from response body
  - Mapping HTTP codes to specific exception types (`ApiException.Unauthorized`, `.ServerError`, etc.)
- All data sources inherit from this base class
- **Error Handling:** Custom `ApiException` hierarchy with statusCode, errorCode, message fields

### API Response Structure
**File:** `data/remote/api/ApiResponse.kt`
```kotlin
@Serializable
data class ApiResponse<T>(
    val status: Int,
    val code: String,
    val message: String,
    val data: T? = null
)
```

### Network Setup
- **MTV Core Network Library:** Custom `NetworkRepository` from external Maven repo (`com.mtv.based.core:network`)
- Handles Retrofit interceptors, request/response logging
- Support for both Moshi and Gson serialization

## Testing Patterns

### Unit Tests (JUnit + MockK)
**Location:** `feature-*/src/test/java/`
- Use `MainDispatcherRule()` for Coroutine testing
- Mock use cases with `mockk(relaxed = true)` for simple cases
- Use `slot` to capture arguments: `slot<FoodUpsertParam>().let { ... }`
- Flow testing with `runTest { ... flowOf(...).first() }`

### Test Data
- Fake data in `data/fake/` (e.g., `FakeHomeData.kt`)
- Mock UI data in `data/mock/DataUiMock.kt`
- Example: Pre-built domain models with realistic values for snapshot testing

### Turbine for Flow Testing
```kotlin
flow.test {
    awaitItem()  // Consume emitted values
    skipItems(n) // Skip n items
    awaitComplete()
}
```

## Deployment & CI/CD

### Fastlane Integration
**Location:** `fastlane/Fastfile`, `fastlane-env.sh`
- **Environment Setup:** Scripts isolate Fastlane gems to `.vendor/bundle/` to avoid conflicts
- **Available Lanes:**
  - `fastlane firebase` - Release build → Firebase App Distribution
  - `fastlane firebase_debug` - Debug build with Chucker → Firebase
  - `fastlane deploy` - Interactive menu for upload options
  - `fastlane deploy_debug` - Alias for debug Firebase deployment

### Signing Configuration
- **Release:** Requires keystore file (path, password, alias, key password) from env vars:
  - `SHOPME_KEYSTORE_PATH`, `SHOPME_KEYSTORE_PASSWORD`, `SHOPME_KEY_ALIAS`, `SHOPME_KEY_PASSWORD`
- **Debug:** Auto-signed by Android SDK
- **Firebase Distribution:** Requires `FIREBASE_APP_ID`, `FIREBASE_SERVICE_ACCOUNT_JSON` or file

### Backend Contract Verification
**Script:** `scripts/verify_backend_contract.py`
- Validates Android DTO models match backend API contracts
- Reads existing Kotlin files and compares with backend JSON schema
- Triggered pre-build to catch API mismatches early
- Test credentials in script: buyer/seller emails and demo password

## Common Workflows

### Adding a New Feature Screen
1. Create route in `nav/route/` (e.g., `nav/route/customer/MyNewRoute.kt`)
2. Define contract in `feature-*/contract/MyNewContract.kt` (UiState, Event, Effect)
3. Create ViewModel: `feature-*/presentation/MyViewModel.kt`
   - Constructor inject use cases from domain
   - Implement event handlers that call use cases
   - Emit new state and effects
4. Create Composable screen in `feature-*/screen/`
5. Register navigation graph in `nav/customer/CustomerNavActions.kt` or seller equivalent
6. Write unit tests in `feature-*/src/test/`

### Data Flow Walkthrough (Example: Load Customer Home)
1. **Presentation Layer:**
   - User opens Home screen
   - Composable emits `SellerDashboardEvent.Load` to ViewModel
2. **ViewModel Processing:**
   - Catches event, calls `getSellerProfileUseCase.invoke()`
   - Collects Flow<Resource<SellerProfile>>, maps to UiState
3. **Use Case:**
   - Constructor-injected `SellerRepository`
   - Calls `repo.getSellerProfile()` + applies business logic
4. **Repository:**
   - Calls `BaseRemoteDataSource.request<SellerProfile>(...)`
   - Network call via MTV Core Network Library
5. **Response Handling:**
   - API returns `ApiResponse<SellerProfileDto>` with data
   - `ResponseMapper` converts DTO → Domain model
   - Room DB cache via `EntityMapper`
6. **Back to ViewModel:**
   - Resource.Success(SellerProfile) → Updates UiState
   - Composable re-renders with new data

### Modifying Database Schema
1. Edit entity in `core/database/entity/`, add fields
2. Create migration class in `core/database/migrations/`
3. Register in `AppDatabaseMigrations.ALL`
4. Update mappers in `data/mapper/EntityMapper.kt`
5. Update DAO queries if needed
6. Run tests to ensure migration path

## Key File Organization

### Naming Conventions
- **Screens:** `[Feature]Screen.kt` (Composable @Context)
- **ViewModels:** `[Feature]ViewModel.kt` extends `ViewModel()`
- **Contracts:** `[Feature]Contract.kt` (UiState, Event, Effect sealed classes)
- **Routes:** `[Feature]Route.kt` sealed class extending `Route`
- **Use Cases:** `[ActionVerb][Entity]UseCase.kt` (e.g., `GetSellerProfileUseCase`)
- **Repositories:** `[Entity]RepositoryImpl.kt`
- **Data Sources:** `[Entity]RemoteDataSource.kt`, `[Entity]LocalDataSource.kt`
- **Mappers:** `*Mapper.kt` or `[Category]Mapper.kt`

### Package Structure (Feature Module)
```
feature-customer/src/main/java/com/mtv/app/shopme/feature/customer/
├── contract/          # UiState, Event, Effect
├── presentation/      # ViewModels
├── screen/           # Composables
├── di/               # Feature-specific Hilt modules
└── entities/         # Local data classes (if not in domain)
```

## Debugging & Troubleshooting

### Common Issues
1. **Hilt Compilation Errors:** Check `@HiltViewModel` on ViewModels, `@InstallIn` on Modules
2. **Serialization Mismatch:** JSON has extra fields → Add `@SerialName` or `ignoreUnknownKeys = true` in errorJson
3. **Route Navigation Missing:** Ensure route defined in `nav/route/` AND registered in navigation graph
4. **Database Migration Failure:** Run `adb shell` and backup data before testing migrations
5. **Chucker Not Visible in Release:** Configured to use no-op in release; only debug builds include interceptor

### Debug Logging
- Use `Log.d(TAG, "message")` for general debugging
- Network: Chucker UI shows all HTTP requests/responses (debug only)
- Database: Room queries logged via KLogs (if enabled)
- Flow: Turbine `.test { }` blocks capture all emissions

## External Dependencies to Know

### MTV Packages (Internal)
- **`com.mtv.based.core:network`** - Extended Retrofit client, NetworkResponse wrapper, RequestOptions
- **`com.mtv.based.uicomponent`** - Shared Composables, theme, icons

### Critical Third-Party Libs
- **Retrofit/Moshi:** REST API calls, JSON serialization
- **Firebase:** Auth, Firestore (real-time), Messaging (FCM)
- **Room:** Local caching database
- **Hilt:** Dependency injection at compile-time
- **Jetpack Compose:** Modern declarative UI framework
- **Coroutines:** Async task management with structured concurrency

## Best Practices

1. **Always test mappers:**
   - Entity ↔ Domain conversions prone to null pointer or type casting errors
   - Write mapper unit tests for edge cases (empty strings, null addresses, etc.)

2. **Validate before database insertion:**
   - Room doesn't validate foreign keys by default
   - Check referential integrity in repository layer

3. **Use Flow for streams, not repeated emissions:**
   - Prefer `Flow<T>` over `StateFlow` for one-time operations
   - Set proper buffer strategies to avoid memory leaks

4. **Bundle parameters in sealed data classes:**
   - Use `SellerDashboardEvent.ClickOrderDetail(orderId: String)` not multiple functions

5. **Test scope mismatch prevention:**
   - When switching screens, ensure coroutines are cancelled with proper ViewModel scope
   - Hilt ViewModels auto-scoped to Activity/Fragment lifecycle

6. **Cache strategy:**
   - Check `updatedAt` timestamp before refreshing from network
   - Implement smart refresh: network calls only if cache > X minutes old

---

**Last Updated:** June 2026  
**Kotlin Version:** 2.0.21 | **Android API:** 36 | **Min API:** 24

---

## Session Context (Last: 30 Jun 2026 — End of Day)

### Backend Status
- **Running**: `http://localhost:8080` (PID 6416, started via `Start-Process` with env vars)
- **Profile**: `prod` with Flyway, DDL auto=validate
- **Media**: disabled (`SHOPME_MEDIA_ENABLED=false`) — MinIO still running but not used
- **DB**: PostgreSQL `app_db` via Docker (`localhost:5432`)
- **Redis**: `localhost:6379` via Docker
- **MinIO**: `localhost:9000` via Docker
- **Startup**: requires env vars `SPRING_DATASOURCE_URL/USERNAME/PASSWORD`, `SPRING_DATA_REDIS_HOST/PORT`, `JWT_SECRET`, `ADMIN_NAME/EMAIL/PASSWORD`, `SHOPME_MEDIA_*`

### Seed Data
- **Admin**: `admin.shopme@mtv.com` / `sEzunWapdxj62uF3By3bKmjBz2rAO6Ph`
- **Buyers**: 4 buyers (emails from seed script)
- **Sellers**: 10 sellers w/ cafes + products
- **Orders**: 10 orders seeded
- **NOTE**: `seller.demo@shopme.local` / `Demo123!` — login returning 401, might need re-seed or different password hash

### Seller Dashboard API
- **New endpoint**: `GET /api/v1/seller/dashboard` → returns `SellerDashboardResponse`
- Response includes: total/today/week/month revenue, order counts by status, weekly/monthly chart data, order status breakdown, top 5 products by sales, total sold, product stats, low stock count

### Seller Feature Improvements (Today — 30 Jun 2026)

#### Revenue & Analytics (✅ Done)
- **Backend**: `SellerController.getDashboard()` → `SellerService.getDashboard()`
  - Aggregates revenue from completed/delivering orders (today, this week via Mon, this month, all-time)
  - Weekly revenue chart data (7 days)
  - Monthly revenue chart data (last 4 months)
  - Order status breakdown (UNPAID, ORDERED, COOKING, DELIVERING, COMPLETED, CANCELLED)
  - Top 5 products by units sold
- **Android**: Redesigned `SellerDashboardScreen.kt` with:
  - Revenue cards (Today/Week/Month/Total) with colored indicators
  - Quick stats row (Total Orders, Active Products, Total Sold, Pending)
  - Weekly Revenue Bar Chart (Canvas-based)
  - Order Status Breakdown (horizontal stacked bar + legend)
  - Top Products section (#1/#2/#3 badges)
  - Low Stock Warning card
  - Keep existing: header, online toggle, notification badge, order filter chips, order list

#### Stock Management (✅ Already Implemented)
- Product list: `ModernStockBadge` (≤5 = red "Low Stock - N", >5 = blue "Stock - N")
- Product header stats: Total Products, Total Stock, Low Stock count
- Dashboard: `lowStockProducts` count + warning card
- Product form: stock quantity input field

#### Store QR Code (✅ Done)
- `StoreQrDialog.kt` — ZXing-based QR code generation
- Shows QR code bitmap (240dp) for store URL
- Share button → Android share intent
- QR icon in Store screen top bar

#### Rating Display (✅ Done)
- Added `rating: Float`, `ratingCount: Int` to `SellerStoreUiState`
- Star rating display (5 star icons + rating value + count) in Store header

#### Order CSV Export (✅ Done)
- Download icon button in Order screen header
- Exports order list as CSV to Downloads folder via `MediaStore.Downloads`
- Shows Toast on completion

#### Bulk Actions (✅ Done — Partial UI)
- Backend: `PATCH /api/v1/foods/bulk/active` (existing)
- Domain: `FoodBulkStatusParam`, `UpdateFoodBulkStatusUseCase`
- Contract: selection mode state + events (ToggleSelectionMode, ToggleProductSelection, SelectAll, DeselectAll, BulkActivate, BulkDeactivate, BulkDelete)
- ViewModel: handlers for all bulk events
- **Pending**: UI update to product list screen (selection checkboxes, bulk action bar)

### Pending Features (Need Backend Work)
| Feature | What's Needed |
|---------|---------------|
| **Promo & Diskon** | Backend: discount table, CRUD API. Frontend: discount form, badge |
| **Customer Reviews** | Backend: review table, rating API. Frontend: review list, reply |
| **Product Category Management** | Backend: category CRUD API. Frontend: category manager |
| **Operating Hours** | Backend: already has openTime/closeTime on Cafe. Frontend: display & edit UI |

### Key Learnings
1. `FoodRequest.estimate: String` is **required** (no default) — will cause 400 if missing.
2. `CafeAddRequest.image` is `@NotBlank` dan divalidasi oleh MinioMediaAssetService — must be empty or managed key when media enabled.
3. Backend application.yml uses `${SPRING_DATASOURCE_URL}` — env vars, NOT `--spring.datasource.url` args.
4. MinIO requires `SHOPME_MEDIA_PUBLIC_BASE_URL` when media enabled.
5. `Invoke-RestMethod` returns `PSCustomObject` with PascalCase props (e.g., `$resp.data.Id`).
6. API is `/api/v1/...` — Android `ApiEndPoint.kt` already updated.
7. Backend rebuild: `.\mvnw.cmd package -pl shopme-app -am -D"maven.test.skip=true"`
8. Backend start: `Start-Process -FilePath "java" -ArgumentList "-jar <jar>""` with env vars pre-set
9. Android build: `./gradlew assembleDebug`

### Android Build
- APK debug v1.0.15-x di Firebase App Distribution
- Base URL dari `local.properties`: `http://192.168.100.55:8080/`

