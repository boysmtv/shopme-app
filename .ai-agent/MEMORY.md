# Shopme Android Agent Memory

Last refreshed: 2026-05-16 on Windows.

## Project Identity

- Android root: `D:\Boys\Android\shopme-app`
- Backend root: `D:\Boys\Backend\shopme-backend`
- Architecture: multi-module Kotlin Android app using Compose, Hilt, Flow, repository/use-case layers, Room cache, Retrofit-like endpoint abstraction from `android-mtv-based-core`, and backend-backed buyer/seller flows.
- Active code wins over `.brief` if they disagree.

## Codebase Understanding Status

This memory is the active operational map for the Android Shopme codebase. The codebase has been inventoried across all tracked Kotlin modules and mapped at the levels needed to reason about end-to-end behavior: modules, endpoints, repositories, data sources, DTOs, media contract, error/session behavior, ViewModels, buyer flow, seller flow, and backend counterpart dependencies.

Tracked Kotlin file distribution at the time of this refresh:

- `app`: 17
- `common`: 26
- `core`: 28
- `data`: 120
- `domain`: 151
- `feature-auth`: 25
- `feature-customer`: 85
- `feature-firebase`: 5
- `feature-seller`: 57
- `nav`: 43

2026-05-16 re-inventory: source distribution still matches this memory, with 557 tracked Kotlin source files outside build/generated folders. Android worktree was clean during this check.

Operational confidence:

- I can simulate the primary buyer and seller flows end-to-end from UI event to backend endpoint using this memory.
- I can identify the correct module/layer to inspect for a change request.
- Before changing a specific function, I must still reopen the exact file and update this memory with any newly discovered implementation detail.

## Current Media Contract

The active source code no longer uses base64 as the image contract.

Android upload path:

- `common/ImagesExtension.kt`
  - `uriToCompressedJpegFile(context, uri, maxSize=1280, quality=82)` decodes a `content://` URI, fixes EXIF orientation, compresses to temporary JPEG in cache.
  - `fixBitmapOrientation` applies EXIF rotation/flip.
- `data/remote/datasource/MediaRemoteDataSource.kt`
  - `createUploadTicket(scope, contentType)` -> `POST /api/media/presign-upload?scope=...&contentType=...`
  - `uploadToPresignedUrl(uploadUrl, file, contentType)` -> raw HTTP `PUT` with JPEG body.
  - `uploadImage(file, scope)` -> multipart `POST /api/media/upload`, currently available but repository uses presigned flow.
- `data/repository/MediaRepositoryImpl.kt`
  - `uploadImage(localUri, scope)` prepares JPEG, creates presigned ticket, PUTs file, returns `UploadedMedia`.
- `data/mapper/MediaMapper.kt`
  - maps `MediaUploadResponse` to `UploadedMedia`.
- `common/SmartImage.kt`
  - renders non-blank URL/string with Coil `AsyncImage`; uses placeholder if empty.

Media DTO:

- `MediaUploadResponse`: `key`, `originalUrl`, `mediumUrl`, `thumbnailUrl`, optional `uploadUrl`, optional `uploadMethod`.
- `UploadedMedia`: same semantic fields in domain.

Seller image submit behavior:

- `SellerCreateCafeViewModel.resolveUploadedReference`
  - blank/null or non-`content://` is reused as-is.
  - `content://` is uploaded with scope `cafes`, result `originalUrl` goes into `CafeAddParam.image`.
- `SellerEditStoreViewModel.resolveUploadedReference`
  - same behavior for store/cafe image update.
- `SellerProductFormViewModel.resolveUploadedReferences`
  - each `content://` image is uploaded with scope `products`.
  - `buildUpsertParam` currently keeps only resolved images starting with `http://` or `https://`.
  - product form fills five image slots from remote `Food.images`.

Customer profile image submit behavior:

- `EditProfileViewModel.resolveUploadedReference`
  - non-`content://` photo references are reused as-is.
  - `content://` photo references are uploaded with scope `customer-profile`.
  - upload success sends `UploadedMedia.originalUrl` through `CustomerUpdateParam.photo`.
  - upload failure sets `updateProfile` to `LoadState.Error`, shows the mapped error dialog, and aborts profile update.

Important rule: do not reintroduce base64 assumptions. The backend rejects inline `data:` payloads.

## Module Map

- `app`: application bootstrap, Hilt modules, network config, Firebase config, `MainActivity`, `AppNavGraphBuilder`.
- `common`: theme constants, destination constants, base Compose shells, serializers, image helpers, `SmartImage`, bottom navigation.
- `core`: Room database/entities/DAO, cache policies, realtime gateway interface, `ResultFlowFactory`, `FlowExecutor`, `DefaultErrorMapper`, `ApiException`.
- `data`: remote endpoints, request/response DTOs, data sources, mappers, repositories, Room cache mapping, offline mutation sync, realtime implementation.
- `domain`: domain models, repository interfaces, use cases, params.
- `feature-auth`: splash, login, register, forgot/OTP/reset, change password, change PIN.
- `feature-customer`: buyer/home/profile/cart/order/search/favorite/chat/support flows.
- `feature-seller`: seller dashboard/store/create/edit/product/order/payment/chat/notification flows.
- `nav`: concrete route wrappers and navigation actions.
- `scripts`: backend contract verifier.

## API Endpoint Map

Defined in `data/remote/api/ApiEndPoint.kt`.

- Auth: `POST /api/auth/register`, `/login`, `/forgot-password`, `/verify-otp`, `/reset-password`, `/change-password`.
- Splash: `POST /api/splash`.
- Customer: `GET/PUT/DELETE /api/customer`, notification preferences, PIN, favorites.
- Address/Village: `GET/POST /api/address`, `DELETE /api/address/{id}`, `PATCH /api/address/{id}/default`, `GET /api/village`.
- Cafe/Seller: `GET/POST /api/cafe`, `GET/PUT /api/cafe/{id}`, `GET /api/cafe/address/{id}`, `POST /api/cafe/address`, seller profile/payment/orders/availability.
- Foods: list/create/detail/update/delete/search/by cafe.
- Cart: list/add/update quantity/clear/clear by cafe.
- Order: list/detail/session/create/confirm transfer/update status.
- Chat: list/detail/ensure conversation/send/read/clear, with `asSeller` flag in datasource.
- Notifications: list/unread/read-all.
- Support: center, chat list, send support chat.
- Media: presigned upload and multipart upload.

## Android Repository And Datasource Map

Repositories:

- `AppRepositoryImpl`: splash, support center, support chat, send support chat.
- `AuthRepositoryImpl`: login, register, forgot password, verify OTP, reset password, change password, change PIN.
- `ProfileRepositoryImpl`: customer profile, update/delete account, notification preferences, address CRUD/default, villages, favorites.
- `CafeRepositoryImpl`: cafe detail, create/update cafe, cafe address get/upsert.
- `FoodRepositoryImpl`: foods list/by cafe/detail/similar/search/create/update/delete.
- `CartRepositoryImpl`: cart list/add/update/clear, clear by cafe, create order, verify PIN, checkout session token.
- `OrderRepositoryImpl`: buyer orders, order detail, confirm transfer.
- `SellerRepositoryImpl`: seller profile, payment methods, seller orders/detail, update order status, availability, payment methods update.
- `ChatRepositoryImpl`: customer/seller chat list/detail, ensure conversation, send/read/clear.
- `NotificationRepositoryImpl`: customer/seller notifications, unread count, clear/read-all.
- `MediaRepositoryImpl`: current image upload flow.

Remote data sources mirror these repositories and call `ApiEndPoint`.

## DTO And Contract Memory

Request DTOs:

- `CafeAddRequest`: `name`, `phone`, `description`, `minimalOrder`, `openTime`, `closeTime`, `image`, `isActive`.
- `CafeAddressUpsertRequest`: `cafeId`, `villageId`, `block`, `rt`, `rw`, `number`.
- `FoodUpsertRequest`: `cafeId`, optional `image: List<FoodImageRequest>`, `name`, `description`, `category`, `estimate`, `isActive`, `price`, `quantity`, `status`, optional variants/options.
- `CustomerUpdateRequest`: `name`, `phone`, nullable `photo`, nullable `fcmToken`.
- `CreateOrderRequest`: `cartId: List<String>`, `payment`, `token`.
- `SellerPaymentMethodRequest`: booleans and account numbers for cash, bank, ovo, dana, gopay.
- `SplashRequest`: device/session metadata including app version and device info.

Response DTOs:

- `CafeResponse`: includes `image: String` and non-null Android `address`; backend response address is nullable, mapper must defend.
- `FoodResponse`: `images: List<String>`, `variants`, `createdAt: LocalDateTime`.
- `CustomerResponse`: nullable `name`, `phone`, `email`, `address`, `photo`, `verified`, `stats`, `menuSummary`.
- `SellerProfileResponse`: nullable `cafeId`, seller/store identity, photos, `isOnline`, `hasCafe`.
- `OrderResponse`: summary/detail DTOs with nullable `createdAt`, `deliveryAddress`, names, item list.
- `PageResponse<T>`: content, page, size, totalElements, totalPages, last.
- `AppNotificationResponse`, `ChatListResponse`, `ChatResponse`, support DTOs.

Enum alignment:

- Food category/status, order status/item status, payment method/status must use backend names.
- `OrderStatus.fromValue` exists for string conversion.

## Error And Session Memory

- `ApiException`: Unauthorized 401, Forbidden 403, Conflict 409, ServerError 500, Validation, EmptyBody, Unknown.
- `DefaultErrorMapper` maps IO/timeout to network, unauthorized/forbidden distinctly, conflict to validation, server to server, unknown fallback.
- Many ViewModels call `handleSessionError`; for `401` this should clear session/navigate login where wired.
- `403` must remain access denied/business role boundary, not token expiry.

## ViewModel Flow Map

Auth:

- `SplashViewModel`: calls splash, handles authenticated vs login, maintenance, force update, fatal.
- `LoginViewModel`: email/password -> login -> home.
- `RegisterViewModel`: name/email/password -> register -> success/login.
- `ResetViewModel`: forgot -> verify OTP -> reset password staged flow.
- `PasswordViewModel`: change current password.
- `ChangePinViewModel`: old/new/confirm PIN.

Buyer/customer:

- `HomeViewModel`: load foods, customer profile, favorites, paging, category/search/detail navigation.
- `SearchViewModel`: paged search, favorite toggle, detail navigation.
- `DetailViewModel`: food detail, similar foods, favorite, ensure profile/address/PIN readiness before add to cart, chat/cafe/cart navigation.
- `CafeViewModel`: cafe detail, cafe foods, favorite, open chat/WhatsApp/search/detail.
- `CartViewModel`: cart load, checkout session, profile readiness, PIN verify, create order, quantity change, clear cart.
- `OrderViewModel`: order list, confirm transfer, open chat, detail.
- `OrderDetailViewModel`: detail, confirm transfer, open chat.
- `OrderHistoryViewModel`: profile order history projection/filter.
- `ProfileViewModel`: customer profile, seller eligibility/tnc decision, logout, navigation.
- `EditProfileViewModel`: customer profile, address/village load, update profile, add/delete/default address.
- `FavoriteViewModel`: favorite IDs/items, toggle, detail.
- `NotificationViewModel`: preferences load/toggle/persist.
- `NotifViewModel`: notification list, clear, realtime notification delta.
- `ChatListViewModel` and `ChatViewModel`: customer chat list/detail/send/read/realtime deltas.
- `SupportViewModel` and `ChatSupportViewModel`: support center, intents, support chat.
- `SettingsViewModel`, `SecurityViewModel`, `HelpViewModel`: settings/security/help flows.

Seller:

- `SellerCreateCafeTncViewModel`: seller terms from support center, all terms required before next.
- `SellerCreateCafeViewModel`: multi-step cafe form, village validation, image upload, create cafe, then attach address.
- `SellerDashboardViewModel`: profile/orders/unread notification, online toggle, filters/sort, navigation.
- `SellerStore/ProfileViewModel`: seller profile/store summary, online toggle, logout/back to customer.
- `SellerEditStoreViewModel`: load cafe/profile/address, image upload, update cafe, save address.
- `SellerPaymentMethodViewModel`: load/update payment methods, validation.
- `SellerProductListViewModel`: seller profile -> cafe products, delete, add/edit.
- `SellerProductFormViewModel`: create/edit/delete product, upload product images, variants/options.
- `SellerOrderViewModel`: seller orders, filter, online toggle, detail.
- `SellerOrderDetailViewModel`: order detail, status update.
- `SellerChatListViewModel`, `SellerChatDetailViewModel`: seller chat list/detail/send/read/realtime deltas.
- `SellerNotifViewModel`: seller notifications and realtime deltas.

## End-To-End Simulation Memory

Buyer happy path:

1. Splash posts device/app metadata to `/api/splash`.
2. If unauthenticated, login posts credentials to `/api/auth/login`; token is stored by underlying network/session layer.
3. Home loads `/api/foods`, `/api/customer`, favorites; list images are URL strings.
4. Buyer opens food detail `/api/foods/{id}`, optional similar by cafe, toggles favorite through `/api/customer/favorites/{foodId}`.
5. Add to cart checks customer readiness/address/PIN, then posts `/api/cart`.
6. Cart loads `/api/cart`, gets checkout token from `/api/order/session`, verifies PIN through `/api/customer/pin`, creates order via `/api/order`.
7. Orders load `/api/order`; detail loads `/api/order/{id}`; transfer confirmation posts `/api/order/{id}/confirm-transfer`.
8. Notifications load `/api/notifications`; chat uses `/api/chat/conversation`, `/api/chat`, `/api/chat/message`, `/api/chat/read`.

Seller happy path:

1. Customer profile decides seller state. If no cafe, seller terms load from support center then create cafe flow starts.
2. Create cafe validates village, uploads selected local image through media presign/PUT if needed, posts `/api/cafe`, then attaches address with `/api/cafe/address`.
3. Seller dashboard/profile loads `/api/seller/profile`, orders, unread notifications.
4. Edit store loads cafe/detail/address, uploads changed image if `content://`, PUTs `/api/cafe/{id}`, then updates address.
5. Product list loads seller profile then `/api/foods/cafe/{cafeId}`.
6. Product form uploads any local `content://` images, creates/updates `/api/foods`, deletes with `DELETE /api/foods/{id}`.
7. Seller order detail loads `/api/seller/orders/{id}`, status update patches `/api/order/{id}/{status}`.
8. Seller payment methods GET/PUT `/api/seller/payment-methods`; availability PUT `/api/seller/availability`.
9. Seller chat uses same chat endpoints with `asSeller=true`.

## Verification Memory

Known test locations:

- Data repository tests under `data/src/test`.
- Auth ViewModel tests under `feature-auth/src/test`.
- Customer ViewModel tests under `feature-customer/src/test`.
- Seller ViewModel tests under `feature-seller/src/test`.
- Contract verifier: `scripts/verify_backend_contract.py`.
- Integration gate: `verify-backend-integration.sh`.

Latest Windows runtime/build status:

- 2026-05-14: `.\gradlew.bat testDebugUnitTest` ran on Windows with Android Studio JBR. Result: failed. 34 tests completed, 1 failed in `:feature-customer:testDebugUnitTest`.
- Failing test: `com.mtv.app.shopme.feature.customer.ChatViewModelTest.load should infer active chat and mark it as read`.
- Failure detail: expected active conversation id `conv-1`, actual empty string. Report path: `feature-customer/build/reports/tests/testDebugUnitTest/index.html`.
- 2026-05-14: Fixed buyer and seller chat detail state collection by using `observeIndependentDataFlow` for flows whose `onState` expects `LoadState`. Focused `:feature-customer:testDebugUnitTest --tests com.mtv.app.shopme.feature.customer.ChatViewModelTest` passed.
- 2026-05-14: Full Android `.\gradlew.bat testDebugUnitTest` passed on Windows with Android Studio JBR.
- 2026-05-16: Static re-inventory and cross-codebase audit completed. `.\gradlew.bat testDebugUnitTest` passed on Windows with Android Studio JBR. Endpoint/media/realtime spot checks still match backend controller/media/realtime contract except cross-audit gaps listed below.
- 2026-05-16: Full live pipeline rerun after brief update. Android `.\gradlew.bat assembleDebug testDebugUnitTest` passed. Backend Docker app/postgres/redis/minio started, `seed-demo-data.sh` passed with temporary PNG media fixtures, backend `test-api.sh --ci --cleanup` passed with `114 passed, 0 failed`, and `scripts\verify_backend_contract.py` passed against `http://127.0.0.1:8080/v3/api-docs` using `buyer.demo@shopme.local` / `seller.demo@shopme.local`.
- 2026-05-16: Chucker debug inspection installed for Android. Active laptop Wi-Fi IP was detected as `192.168.100.37`, and `local.properties` now uses `shopme.baseUrl=http://192.168.100.37:8080/`. Debug `common.BuildConfig.USE_KTOR` is now `false` so normal API traffic goes through the core Retrofit/OkHttp client that has `ChuckerInterceptor`; Ktor traffic is not Chucker-visible. Manual OkHttp paths in `MediaRemoteDataSource` presigned uploads and `ShopmeRealtimeGatewayImpl` now add a Chucker interceptor with `Authorization`/`Cookie` redaction. Chucker is pinned to `4.0.0` because `4.2.0` pulled Kotlin metadata `2.2.0` and failed against this project's Kotlin `2.0.21`. Debug classpaths exclude `library-no-op` to avoid duplicate Chucker classes; release keeps the no-op artifact.
- 2026-05-16: `.\gradlew.bat :data:clean :app:assembleDebug` passed on Windows with Android Studio JBR after the Chucker/IP changes.
- 2026-05-16: Login invalid-credential UX regression fixed. `LoginViewModel` now snapshots submitted email/password and preserves/restores them while applying `LoadState` updates, so wrong credentials do not clear the login form. Regression test added at `feature-auth/src/test/java/com/mtv/app/shopme/feature/auth/LoginViewModelTest.kt`; focused `.\gradlew.bat :feature-auth:testDebugUnitTest --tests com.mtv.app.shopme.feature.auth.LoginViewModelTest` passed, and `.\gradlew.bat :app:assembleDebug` passed.

## CI/CD Memory

- Android CI/CD scaffold uses GitHub Actions at `.github/workflows/android-firebase.yml`.
- Firebase App Distribution is configured through Gradle plugin `com.google.firebase.appdistribution` on the `release` build type.
- CI secrets expected in GitHub repository settings: `FIREBASE_APP_ID`, `FIREBASE_SERVICE_ACCOUNT_JSON`, `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`.
- Optional GitHub Actions variable: `FIREBASE_APP_DISTRIBUTION_GROUPS`, defaulting to `internal-testers`.
- Release signing and Firebase service account files must remain outside git; CI reconstructs them from GitHub Secrets under `$RUNNER_TEMP`.
- Local generated release keystore is intentionally git-ignored at `keystore/shopme-release.jks`; base64 helper output is `keystore/KEYSTORE_BASE64.txt`. Current release key alias is `shopme`; SHA-256 fingerprint is `5B:08:80:FD:C4:F8:90:7F:E1:86:73:94:EA:F1:D4:94:7C:C4:28:B2:FF:DB:59:B2:76:36:CB:34:5F:9F:BF:FB`.
- Current CI resolves internal `com.mtv.based.*` libraries from GitHub Packages instead of rebuilding them in the Shopme workflow. `android-mtv-based-uicomponent` publishes `component`, `ui`, and `theme-ui` to `https://maven.pkg.github.com/boysmtv/android-mtv-based-uicomponent`; `android-mtv-based-core` publishes `network` and `provider` to `https://maven.pkg.github.com/boysmtv/android-mtv-based-core`.
- Package publish order is UI component first, then core, then Shopme. Core needs UI component packages when publishing.
- GitHub Actions passes `GITHUB_ACTOR`, `GITHUB_TOKEN`, and optional custom secrets `PACKAGES_USER` / `PACKAGES_TOKEN` to Gradle. Local Windows development can use `mavenLocal()` or set `PACKAGES_USER` and `PACKAGES_TOKEN` for GitHub Packages resolution.
- Fastlane deployment support lives under `fastlane/`. Lane `android firebase` and alias `android deploy` build `:app:assembleRelease` and upload `app/build/outputs/apk/release/app-release.apk` to Firebase App Distribution via `fastlane-plugin-firebase_app_distribution`.
- Fastlane expects the same environment contract as CI: `FIREBASE_APP_ID`, either `FIREBASE_SERVICE_ACCOUNT_FILE` or `FIREBASE_SERVICE_ACCOUNT_JSON`, signing env vars `SHOPME_KEYSTORE_PATH`, `SHOPME_KEYSTORE_PASSWORD`, `SHOPME_KEY_ALIAS`, `SHOPME_KEY_PASSWORD`, plus optional `FIREBASE_APP_DISTRIBUTION_GROUPS`, `FIREBASE_RELEASE_NOTES`, `PACKAGES_USER`, and `PACKAGES_TOKEN`.
- Windows local Fastlane runtime is installed outside the Android repo at `D:\Boys\Ai\ai-software-factory\tools\ruby-devkit-3.4.7-1-x64`; gems are installed at `D:\Boys\Ai\ai-software-factory\tools\ruby-devkit-gems`.
- Chocolatey Ruby install failed because of a Chocolatey lock/permission issue, so use the RubyInstaller+Devkit local runtime above. Helper script `scripts/fastlane-env.ps1` configures `GEM_HOME`, `GEM_PATH`, `HOME`, `USERPROFILE`, and `PATH`, then prints the command `bundle exec fastlane android deploy`.
- Fastlane must not write to `C:\Users\dedyw\.fastlane` in this Codex runtime; `scripts/fastlane-env.ps1` redirects `HOME`/`USERPROFILE` to `D:\Boys\Ai\ai-software-factory\tools\fastlane-home`.
- `bundle install` succeeded on Windows with Ruby 3.4.7 + DevKit and generated/updated `Gemfile.lock`.
- 2026-05-16 Fastlane Firebase local run:
  - Direct `scripts\fastlane-env.ps1` is blocked by PowerShell execution policy unless called with `-ExecutionPolicy Bypass` or equivalent manual env setup.
  - Local service account exists at `app/shopme-app-service-account.json` and matches Firebase project `shopme-app-360b4`; keep it untracked/secret.
  - `fastlane/.env` resolves required Firebase app id, signing passwords, package credentials, and default group, but its `SHOPME_KEYSTORE_PATH=../keystore/shopme-release.jks` points outside this repo; local successful run overrode `SHOPME_KEYSTORE_PATH=D:\Boys\Android\shopme-app\keystore\shopme-release.jks`.
  - Local successful run also overrode `FIREBASE_SERVICE_ACCOUNT_FILE=D:\Boys\Android\shopme-app\app\shopme-app-service-account.json` and set `JAVA_HOME=C:\Program Files\Android\Android Studio\jbr`.
  - `bundle exec fastlane android deploy` with old default `internal-testers` group built release but failed at Firebase distribute step with `Invalid request`; correct Firebase App Distribution group alias is `internal-tester`.
  - `bundle exec fastlane android deploy groups:""` passed: `:app:assembleRelease` succeeded, Firebase App Distribution found the same APK in release `1.0 (1)`, set release notes, skipped testers/groups, and finished successfully.
  - `bundle exec fastlane android deploy groups:"internal-tester"` passed: release `1.0 (1)` was distributed to group `internal-tester`.
  - Release APK path: `app/build/outputs/apk/release/app-release.apk`.
- 2026-05-16 Fastlane rerun after Chucker integration:
  - Nested PowerShell `-Command` quoting can strip `$env:` assignments before Fastlane runs; prefer setting Ruby/Fastlane/JAVA/Firebase env vars directly in the same PowerShell command process.
  - Successful command used local Ruby/GEM env, `JAVA_HOME=C:\Program Files\Android\Android Studio\jbr`, `SHOPME_KEYSTORE_PATH=D:\Boys\Android\shopme-app\keystore\shopme-release.jks`, `FIREBASE_SERVICE_ACCOUNT_FILE=D:\Boys\Android\shopme-app\app\shopme-app-service-account.json`, and `bundle exec fastlane android deploy groups:"internal-tester"`.
  - `:app:assembleRelease` passed in Fastlane, then Firebase App Distribution uploaded APK and distributed release `1.0 (1)` to `internal-tester`.
- 2026-05-16 Fastlane debug rerun for Chucker:
  - The prior release upload was wrong for API inspection: release uses Chucker no-op and, without `shopme.releaseBaseUrl`, fell back to `https://api.prod.com/`, causing splash startup failure against the wrong backend.
  - `local.properties` now includes both `shopme.baseUrl` and `shopme.releaseBaseUrl` set to `http://192.168.100.37:8080/`.
  - `fastlane/Fastfile` now has `android firebase_debug` and alias `android deploy_debug`, which builds `:app:assembleDebug` and uploads `app/build/outputs/apk/debug/app-debug.apk`; release/deploy still builds release.
  - Fastlane Gradle actions are pinned to `gradlew.bat` for Windows.
  - `bundle exec fastlane android deploy_debug groups:"internal-tester"` passed after running outside sandbox, uploaded debug release `1.0 (1)` with Chucker to Firebase App Distribution group `internal-tester`.
- 2026-05-16 Fastlane debug rerun after login-form fix:
  - `bundle exec fastlane android deploy_debug groups:"internal-tester"` passed and uploaded a debug APK with Chucker plus the invalid-credential input preservation fix.
  - Release notes used: `Shopme debug build - login input preserved on invalid credentials; demo data ready`.
- 2026-05-16 Fastlane debug rerun for Cloudflare Quick Tunnel public API/MinIO:
  - `local.properties` was updated so `shopme.baseUrl` and `shopme.releaseBaseUrl` point to `https://fwd-consists-webster-clinton.trycloudflare.com/`.
  - Backend Quick Tunnel pair at upload time: API `https://fwd-consists-webster-clinton.trycloudflare.com`, MinIO API `https://ruled-accurate-stake-adventure.trycloudflare.com`.
  - Backend presign smoke passed after backend patch: media read URLs use the API Quick Tunnel HTTPS base and presigned PUT URLs use the MinIO Quick Tunnel HTTPS base.
  - `bundle exec fastlane android deploy_debug groups:"internal-tester"` passed outside sandbox and uploaded debug release `1.0 (1)` to Firebase App Distribution group `internal-tester`.
  - Release notes used: `Shopme debug build - Cloudflare Quick Tunnel public API and MinIO`.
  - Quick Tunnel URLs are temporary/random `trycloudflare.com` hostnames; if cloudflared restarts and URLs change, rebuild/reupload Android or switch to a named Cloudflare Tunnel with a stable domain.
- 2026-05-16 Fastlane debug rerun for stable named Cloudflare Tunnel:
  - `local.properties` now points both `shopme.baseUrl` and `shopme.releaseBaseUrl` to `https://api.dsrv-developer.my.id/`.
  - Backend stable public endpoints are `https://api.dsrv-developer.my.id` for app API/media reads and `https://minio.dsrv-developer.my.id` for MinIO presigned uploads.
  - Docker verification passed for API `/v3/api-docs` and MinIO `/minio/health/live`; backend presign smoke returned stable-domain URLs.
  - `bundle exec fastlane android deploy_debug groups:"internal-tester"` passed outside sandbox and uploaded debug release `1.0 (1)` to Firebase App Distribution group `internal-tester`.
  - Release notes used: `Shopme debug build - stable Cloudflare Tunnel API and MinIO domains`.

## Open Gaps

- Need run Android assemble/build gate after unit-test-only changes if release/debug APK verification is required.
- Need rerun backend Docker smoke and Android contract verifier on Windows.
- Need inspect mappers (`DomainMapper`, `RequestMapper`, `ResponseMapper`, `EntityMapper`) before any DTO-changing work.

## Cross-Codebase Audit Notes

2026-05-16 Android/backend static audit:

- Active endpoint map is broadly aligned from `ApiEndPoint.kt` to backend controllers for auth, splash, customer/profile/address/village, cafe/seller, foods, cart, order, chat/support, notifications, media, and realtime `/ws/realtime`.
- High-risk numeric contract gap: Android `BigDecimalSerializer` decodes only JSON strings, while backend response DTOs expose many `BigDecimal` fields and current Jackson config does not write BigDecimal as strings. Check food, cart, order, and seller order totals before relying on kotlinx serialization for these responses.
- Cafe address nullability gap remains: backend `CafeResponse.address` is nullable, but Android `CafeResponse.address` and `DomainMapper` expect non-null. Create/edit cafe flows post cafe first and address second, so partial backend state can break list/detail mapping.
- `FoodStatus.UNKNOWN` exists only on Android as local/cache/UI fallback. Product form normally sends only backend-supported `READY`, `PREORDER`, or `JASTIP`, so do not send `UNKNOWN` to backend.
- Seller notification/order semantic gap: backend notification payload/response contains notification id, title, message, read state, and timestamp only. Android currently maps seller notification `orderId` from notification id, so `Order #...` in seller notifications is not a real order id until backend includes order metadata.
- Notification clear UX is semantic only: Android clear calls backend `PUT /api/notifications/read-all`; backend marks read and does not delete. Keep wording/state consistent with read-all unless delete endpoints are intentionally wired.
- Cache fidelity limitation: compact `FoodEntity` cache omits cafe id, description, category/status, quantity, creation time, and variants, then maps placeholders (`UNKNOWN`, empty/default values). It is acceptable for list fallback, not for authoritative detail/business decisions.
- Realtime payload is aligned for current Android consumers, including `CONNECTED`, `CHAT_MESSAGE`, `CHAT_READ`, and `NOTIFICATION_CREATED`.
- Pipeline asset note: backend seed/smoke scripts default to drawable names from the old Android asset path. On this Windows run, `jq` was installed via Chocolatey and temp PNG fixtures were supplied through env vars/`ANDROID_DRAWABLE_DIR` so media upload/finalize paths could run without adding binary assets to either repo.

## V3 UI/UX Brief Memory

- 2026-05-16: Android V3 UI/UX brief created from user notes at `.brief/brief.v3.md` with checklist `.brief/implementation-checklist.v3.android.md`.
- V3 Android scope covers swipe refresh for home/cart/search/chat/profile, duplicate-loading cleanup, WhatsApp-like chat receipts, centered cart/chat empty states, richer notifications, seeded random home/search lists, one-card home pagination shimmer, and root back exit dialog.
- 2026-05-16: Android V3 baseline pipeline passed: `.\gradlew.bat testDebugUnitTest assembleDebug` succeeded with Android Studio JBR.
- 2026-05-16: Static Android/backend contract verifier passed with `--skip-runtime` against `http://127.0.0.1:8080/v3/api-docs`.
- Runtime contract upload and backend smoke media upload remain blocked from the Windows host while backend presigned upload URLs point to public `https://minio.dsrv-developer.my.id`; this is an environment/connectivity issue, not an Android compile/test failure.
- 2026-05-16 19:03: V3 pipeline rerun on request. Android `testDebugUnitTest assembleDebug` passed again; static Android/backend contract verifier passed again with `--skip-runtime`.
- 2026-05-16: Android V3 implementation stage 1 completed. Added pull refresh with content-preserving `isRefreshing` state to customer Home, Cart, Search, Profile, buyer chat list/detail, and seller chat list/detail. Fixed cart empty state to centered `Tidak ada keranjang`, centered empty chat text, reduced customer home pagination shimmer from two placeholders to one, and added root main-tab back confirmation dialog `Anda ingin keluar?` in `AppNavigation`.
- 2026-05-16: Android V3 stage 1 verification passed. Compile gate `.\gradlew.bat :feature-customer:compileDebugKotlin :feature-seller:compileDebugKotlin :app:compileDebugKotlin` passed, then full `.\gradlew.bat testDebugUnitTest assembleDebug` passed. Existing coroutine opt-in warnings remain in tests.
- 2026-05-16: Android V3 chat stage completed for buyer and seller detail screens. Removed blocking chat detail loading, removed send-button spinners, added optimistic local message append, bubble timestamp footer, and checkmark states. Temporary status mapping until backend adds official receipt fields: local pending messages show one gray check, failed/local-unread messages show two gray checks, and server-returned own messages show two blue checks. Full `.\gradlew.bat testDebugUnitTest assembleDebug` passed after the chat changes.
- 2026-05-16: Android V3 backend-contract consumption completed. `SearchParam` now carries optional `seed`; Home and Search ViewModels send `sort=random` plus a stable seed for pagination, then generate a new seed on refresh/new search so home/search lists change without local-only shuffling. Notification response/domain models now accept backend `data: Map<String,String>` metadata; buyer and seller notification cards render structured order ID, actor/source, item summary, delivery address, order status, payment status, and fallback to old title/message/date when metadata is absent. Chat response parsing now accepts backend `messageId`, `time`, and `isRead` so the existing checkmark UI can use official read state when present. Verification passed: compile gate for data/domain/customer/seller/app, full `.\gradlew.bat testDebugUnitTest assembleDebug`, and `python scripts\verify_backend_contract.py --backend-root D:\Boys\Backend\shopme-backend --openapi-url http://127.0.0.1:8080/v3/api-docs --skip-runtime`.
- 2026-05-16 20:47: V3 runtime/Fastlane pipeline completed. Backend Docker app was rebuilt/recreated and Flyway `V12` was confirmed successful before Android verification. Runtime API smoke passed for seeded random food search over 50+ UI demo products and chat notification metadata. Android contract verifier passed against live local `/v3/api-docs`; `.\gradlew.bat testDebugUnitTest assembleDebug` passed. `bundle exec fastlane android deploy_debug groups:"internal-tester"` uploaded debug release `1.0 (1)` to Firebase App Distribution using release notes `Shopme debug build - V3 random home/search rich notifications and Chucker`. This debug build should contain Chucker and uses the stable public API configured in `local.properties`.
- 2026-05-16 21:06: V3 cleanup pass completed after request to leave no remaining code gaps. Android now accepts backend `BigDecimal` as JSON number or string in `BigDecimalSerializer`, treats `CafeResponse.address` as nullable with a safe empty-domain fallback, and buyer/seller chat failed-send rows now remain in the conversation with a visible `Kirim ulang` retry action instead of only showing a blocking dialog. Buyer and seller chat retry removes the failed local row and resends through the active conversation id. Checklist items for notification pull refresh, duplicate-loading inventory/state definition, compact pagination placeholder, chat retry/error state, and cart loading/error/empty distinction were closed. Verification passed: targeted Android Kotlin compile for common/data/domain/customer/seller, full `.\gradlew.bat testDebugUnitTest assembleDebug`, backend full `.\mvnw.cmd test`, and Android contract verifier against `http://127.0.0.1:8080/v3/api-docs --skip-runtime`. Fastlane debug upload passed again to Firebase App Distribution group `internal-tester` with notes `Shopme debug build - V3 cleanup retry chat numeric contract nullable cafe address`. ADB was checked through the local SDK path and no device was attached, so device-only Chucker/manual visual smoke could not be executed in this runtime; the uploaded debug build contains Chucker for tester-side confirmation.
- 2026-05-16 21:22: Login invalid-credential fix completed. `LoginViewModel` now uses independent data-flow observation for login so backend `401 Unauthorized` / invalid email-password responses are shown as a login dialog instead of being swallowed by shared session handling, while preserving the typed email and password. Dialog title is `Login gagal` and message uses the backend error text. Focused `LoginViewModelTest` now asserts the dialog appears for invalid credentials; focused auth test, `:app:assembleDebug`, and Fastlane debug upload to Firebase App Distribution group `internal-tester` passed with notes `Shopme debug build - login invalid credential dialog fix`.
- 2026-05-16 21:42: Android V4 UI/UX brief created from latest user notes at `.brief/brief.v4.md` with checklist `.brief/implementation-checklist.v4.android.md`. Scope covers profile active order badge, profile/order shortcut tab routing, transfer confirmation visibility after cooking/delivering, seller chat conversation resolution, centered login `LoadingV2`, home shimmer quantity, chat auto-scroll and seller online presence, Search favorite navigation/toggle, valid product image fallback, same-tab refresh instead of duplicate navigation, PIN loading/wrong-PIN handling, and detail add-to-cart loading. Baseline pipeline after brief creation passed: `.\gradlew.bat testDebugUnitTest assembleDebug`, backend `.\mvnw.cmd test`, and Android contract verifier against local OpenAPI with `--skip-runtime`. This was a brief/baseline pipeline only; V4 implementation work remains open.
- 2026-05-16 22:06: Android V4 implementation pass completed for the non-schema UI/flow items. Changes include centered login `LoadingV2` overlay with no button spinner, transfer confirmation hidden until order status is `COOKING` or `DELIVERING` on list/detail screens, buyer/seller chat detail auto-scroll on load/send/ack/realtime list changes, PIN verification `LoadingV2`, detail add-to-cart loading/duplicate-tap disable, Search favorite header navigation to Favorites, profile active-order fallback count, and same-tab bottom-nav duplicate prevention via `refreshTick`. Verification passed: `.\gradlew.bat compileDebugKotlin`, full `.\gradlew.bat testDebugUnitTest assembleDebug`, backend `.\mvnw.cmd test`, and Android contract verifier with `--skip-runtime`. Remaining V4 brief items that still require a deeper contract/UI pass: true backend-driven presence instead of static online text, profile shortcut initial order-tab routing, full same-tab refresh observers, seller order-to-chat find/create entry point, and manual device/Chucker smoke.
- 2026-05-16 22:42: Android V4 continuation completed for remaining routing/chat refresh gaps. Seller order detail now opens chat by calling `POST /api/chat/conversation/seller` with `orderId`, then navigates to the returned conversation id instead of relying on a possibly missing conversation id. Chat endpoints now keep query parameters in `RequestOptions` so static contract paths match OpenAPI. Profile order shortcut icons route to filtered order tabs (`ORDERED`, `COOKING`, `DELIVERING`, `COMPLETED`). Same active bottom-tab taps now trigger `refreshTick` observers on Home, Cart, Search, Chat, and Profile route layers. Verification passed after patching the affected seller ViewModel test constructor: `.\gradlew.bat testDebugUnitTest assembleDebug`, backend Docker rebuild/restart, and Android contract verifier against live local `/v3/api-docs`. Remaining V4 items are true backend-driven presence, explicit seller-chat retry UX tests, and manual device/Chucker smoke.
- 2026-05-16 23:23: Android V4 continuation for latest user notes completed. Login now consumes backend `role` and routes `SELLER` directly to seller dashboard while customer accounts go to customer home; visible customer-to-seller and seller-back-to-customer menu entries were removed. Customer active order chat and order detail chat now use order-scoped conversation creation via `POST /api/chat/conversation/order?orderId=...`, so missing/invalid `cafeId` no longer blocks chat and deleted chat can be recreated from the order. Active order and order history screens now support pull refresh and hide raw cafe/order ids, showing store, product, quantity, customer, and address instead. Notification cards hide order ids and emphasize actor, items, address, status/payment metadata; FCM foreground handling now accepts notification/data payloads, builds an order-aware fallback message, and requests Android 13 `POST_NOTIFICATIONS`. Detail “Menu lainnya” now has loading shimmer and client-side load-more pagination; cafe food grid has shimmer. Verification passed: `.\gradlew.bat compileDebugKotlin "-Pksp.incremental=false" --no-daemon` and full `.\gradlew.bat testDebugUnitTest "-Pksp.incremental=false" --no-daemon`. Remaining items: true websocket-backed online presence and manual device/Chucker visual smoke.
- 2026-05-17 00:27: Android V4 websocket presence completed. Realtime parsing now supports `PRESENCE_CHANGED` with `onlineCustomerId`, `onlineCafeId`, `online`, and `lastSeenAt`; buyer and seller chat detail state now tracks peer online/offline from websocket events instead of static header text. Buyer chat matches seller presence by the conversation cafe id; seller chat matches customer presence by the conversation customer id. Added realtime payload unit coverage for presence. Verification passed: targeted `:data:testDebugUnitTest :feature-customer:compileDebugKotlin :feature-seller:compileDebugKotlin`, full `.\gradlew.bat testDebugUnitTest "-Pksp.incremental=false" --no-daemon`, and `.\gradlew.bat assembleDebug "-Pksp.incremental=false" --no-daemon`. Fastlane debug upload to Firebase App Distribution group `internal-tester` passed with notes `Shopme debug build - V4 websocket presence and chat online status`. ADB checked via local SDK and returned no attached devices, so install/launch manual device smoke remains blocked by missing device.
- 2026-05-17 01:32: Android V5 seller pass completed from latest seller notes. Seller bottom navigation now behaves like customer: tapping the already selected seller tab sets `refreshTick` and route observers refresh Dashboard, Orders, Chat, Product Management, and Profile/Store instead of pushing duplicate screens. Added pull-to-refresh state/UI to seller Dashboard, Order list, Order detail, Product Management, Store/Profile, plus existing seller chat refresh. Seller chat detail now preserves a route conversation id for direct/order chat opens, fixes seller/customer bubble side mapping (`isFromSeller = item.isFromUser` in seller context), and retains WhatsApp-like seller/right vs customer/left behavior. Seller order detail can advance `UNPAID -> COOKING`, matching transfer flow before confirmation. Seller order list and seller product list now call paged APIs (`/api/seller/orders/page`, `/api/foods/cafe/{id}/page`) and append load-more results with inline pagination loading. Product management UI was cleaned up with safer icon empty state, category text, and stock copy without broken glyphs. Verification passed: targeted Kotlin compile for data/domain/feature-seller/nav/common; `:feature-seller:testDebugUnitTest`; full `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`; backend Docker health `/v3/api-docs` returned 200 after backend rebuild; Fastlane debug upload to Firebase App Distribution group `internal-tester` passed with notes `V5 seller refresh, swipe refresh, chat fix, order status notification, product/order pagination, product UI cleanup`.
- 2026-05-17 01:51: V5 follow-up verification completed after upload. Android/backend static contract verifier passed against live local `http://127.0.0.1:8080/v3/api-docs` with `--skip-runtime`. ADB was checked at `C:\Users\dedyw\AppData\Local\Android\Sdk\platform-tools\adb.exe devices` and still returned no attached devices, so manual install/launch/Chucker smoke remains blocked by missing device. Runtime seller pagination smoke passed using `seller.demo@shopme.local / Demo123!`: login returned role `SELLER`, `GET /api/seller/orders/page?page=0&size=5` returned 5 orders with `last=false`, seller profile returned cafe `1b280081-2e68-4762-9b41-27455527ad20`, and `GET /api/foods/cafe/{cafeId}/page?page=0&size=5` returned 5 foods with `last=false`.
- 2026-05-17 02:22: Android follow-up completed for saved-login role routing and detail similar pagination. Login now persists `USER_ROLE`; splash routes authenticated saved sessions to seller dashboard or customer home based on stored role, with JWT role decode fallback for older saved tokens that lack `USER_ROLE`. Detail food `Menu lainnya` now uses paged cafe food loading through `GetFoodSimilarUseCase(cafeId,page,size)` and auto-loads more when scrolling near the bottom; the visible `Muat lagi` button was removed and replaced with an inline pagination spinner. Verification passed: targeted auth/customer/nav compile, auth+customer unit tests, full `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`. Fastlane debug upload to Firebase App Distribution group `internal-tester` passed with notes `Shopme debug build - role-aware saved login routing and auto similar pagination`; Firebase reported the APK binary matched the existing release but still updated release notes and distribution.
- 2026-05-17: Future backlog saved from post-V5 analysis. Recommended V6 direction is Production QA + Order Flow + Seller Product Edit before adding larger features. Priority areas: real-device QA for Chucker/API visibility, saved-login role routing, push notification foreground/background, websocket idle/network behavior, chat realtime/checklists/online/auto-scroll, and public MinIO image loading; production-ready order timeline with timestamps, cancel/reject reasons, transfer proof clarity, and notification deep links; fuller seller product management with edit variants/prices/stock/status, bulk enable/disable, image preview/validation, seller product search/filter, and simple product stats; stronger customer discovery with favorite filtering, recommendations, recent search, ratings, best-selling/nearby/promo/new sections; performance/scale work for DB indexes, Redis caching, pagination consistency, websocket Redis pub/sub, observability, and rate limiting; later admin/backoffice for seller approval, order/user/product oversight, moderation, transaction dashboard, and broadcast notifications. User noted: save all of this because we will make `2345` later.
- 2026-05-17: Android `2345` planning brief created at `.brief/brief.2345.md` with checklist `.brief/implementation-checklist.2345.android.md`. Scope covers production real-device QA, order timeline/cancel-reject/payment clarity/deep links, full seller product edit/search/filter/stats readiness, stronger customer discovery, scale-facing mobile UX, and admin/backoffice role-readiness. Baseline pipeline after brief creation passed: `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`. No Android API implementation was changed in this step, so Fastlane upload was not needed.
- 2026-05-17 03:28: User requested pipeline for `2345`; Android full pipeline passed again with `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`. Backend full tests also passed, Android/backend contract verifier passed against live local `http://127.0.0.1:8080/v3/api-docs --skip-runtime`, and Docker showed `shopme-app`, `shopme-cloudflared`, Postgres, Redis, and MinIO running. No code/API implementation changed in this run, so no Fastlane upload was performed.
- 2026-05-17 03:50: Android `2345` implementation stage 1 completed for order timeline consumption. Android now parses backend `OrderResponse.timeline` into `Order.timeline` and renders a `Timeline Pesanan` section on customer order detail, falling back to current status/createdAt when older backend responses have no timeline. Customer order detail no longer uses raw order id as primary summary text. Verification passed: targeted data/domain/customer compile, full `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`, backend full tests, Docker backend rebuild, live OpenAPI 200, Android/backend contract verifier, and Fastlane upload to Firebase App Distribution group `internal-tester` with notes `Shopme debug build - 2345 order timeline and cancel contract stage 1`. Manual device/Chucker smoke remains blocked unless a device is attached/tested by user.
- 2026-05-17 04:43: Android `2345` implementation stage 2 completed and uploaded for order cancel/reject UI wiring. Customer order detail now shows `Batalkan Pesanan` only for `UNPAID`/`ORDERED`, opens a reason dialog, calls backend `PATCH /api/order/{id}/cancel`, clears order cache, and reloads detail/timeline after success. Seller order detail now shows `Tolak / Batalkan Pesanan` for non-final statuses, sends optional reason through the same cancel endpoint via seller repository cache clearing, and reloads detail after success. Added `CancelOrderRequest`, customer/seller cancel use cases, repository/data-source API wiring, and updated affected ViewModel tests. Verification passed: targeted data/domain/customer/seller compile, focused customer/seller unit tests, full `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`, and Android/backend contract verifier against live local OpenAPI. Fastlane/Firebase note: `fastlane/.env` now points `FIREBASE_SERVICE_ACCOUNT_FILE` to `D:\Boys\Android\shopme-app\app\shopme-app-service-account.json`; upload required network escalation to reach `www.googleapis.com:443` and then succeeded to Firebase App Distribution group `internal-tester` with release notes `Shopme debug build - 2345 stage 2 customer seller order cancel reason UI`. Release links: console `https://console.firebase.google.com/project/shopme-app-360b4/appdistribution/app/android:com.mtv.app.shopme/releases/0p705t9vjv0j8?utm_source=fastlane`, tester `https://appdistribution.firebase.google.com/testerapps/1:870455241065:android:76c3f0351cddb70a5f75f8/releases/0p705t9vjv0j8?utm_source=fastlane`.
- 2026-05-17 05:02: Android `2345` implementation stage 3 completed and uploaded for notification deep links. Added common `NotificationDeepLink` parser and tests for order/chat payloads; FCM foreground notifications now create a `PendingIntent` with order/chat extras. `MainActivity` now reads notification extras on cold start and `onNewIntent`, and `AppNavigation` waits until after splash/login before routing to the correct detail screen. Routing uses saved `USER_ROLE` first so customer notifications from seller do not accidentally open seller screens; seller accounts route order notifications to seller order detail and chat notifications to seller chat detail, while customers route to customer order detail/chat. Verification passed: targeted common test + common/feature-firebase/app compile, full `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`, Android/backend contract verifier against live local OpenAPI, and Firebase App Distribution upload to group `internal-tester` with notes `Shopme debug build - 2345 stage 3 notification deep links order chat`. Release links: console `https://console.firebase.google.com/project/shopme-app-360b4/appdistribution/app/android:com.mtv.app.shopme/releases/794sa5nh3qnt8?utm_source=fastlane`, tester `https://appdistribution.firebase.google.com/testerapps/1:870455241065:android:76c3f0351cddb70a5f75f8/releases/794sa5nh3qnt8?utm_source=fastlane`.
- 2026-05-17: Android `2345` implementation stage 4 started for seller product management. Seller product list now calls the paged cafe-food API with search/filter parameters, keeps pagination and pull refresh state, and exposes filters for active/hidden, status, and category. Seller product form now distinguishes add/edit title, shows category selection, keeps image preview, disables save while saving, and validates required name/description/price/stock plus at least one valid `content://` or HTTP(S) image before submit. Submit payload tests now cover category, image upload URL mapping, variant group/option payload, and seller list reload tests cover search/filter parameters. Backend `/api/foods/cafe/{id}/page` now accepts optional `q`, `category`, `status`, and `active` query parameters and returns filtered paged responses.
- 2026-05-17 05:33: Android/backend `2345` stage 4 pipeline completed and uploaded. Verification passed: targeted backend `.\mvnw.cmd -pl shopme-core,shopme-menu -am test`, targeted Android `:feature-seller:testDebugUnitTest :feature-seller:compileDebugKotlin`, backend full `cmd.exe /c .\mvnw.cmd test`, Android full `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`, Docker `docker compose up -d --build app`, live `/v3/api-docs` 200, Android/backend contract verifier, and runtime seller product filter smoke using `seller.demo@shopme.local / Demo123!` with `q=ayam&category=FOOD&status=READY&active=true` returning 4 foods. Firebase App Distribution upload to group `internal-tester` succeeded with notes `Shopme debug build - 2345 stage 4 seller product edit search filters`. Release links: console `https://console.firebase.google.com/project/shopme-app-360b4/appdistribution/app/android:com.mtv.app.shopme/releases/51f92pukfad3g?utm_source=fastlane`, tester `https://appdistribution.firebase.google.com/testerapps/1:870455241065:android:76c3f0351cddb70a5f75f8/releases/51f92pukfad3g?utm_source=fastlane`.
- 2026-05-17 13:43: Android `2345` stage 5 completed and uploaded for order payment clarity/human-readable order labels. Customer active order, order history, and order detail now show localized order/payment labels instead of enum/raw technical values; order detail explains when transfer confirmation is available after seller starts processing. Seller order list/detail no longer uses raw order/invoice IDs as primary text, uses localized status/payment labels, and shows seller-facing payment notes. Verification passed: targeted customer/seller Kotlin compile and full `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`. Firebase App Distribution upload to group `internal-tester` succeeded with notes `Shopme debug build - 2345 stage 5 order payment labels and hidden technical IDs`. Release links: console `https://console.firebase.google.com/project/shopme-app-360b4/appdistribution/app/android:com.mtv.app.shopme/releases/3rmq9jjk790c8?utm_source=fastlane`, tester `https://appdistribution.firebase.google.com/testerapps/1:870455241065:android:76c3f0351cddb70a5f75f8/releases/3rmq9jjk790c8?utm_source=fastlane`.
- 2026-05-17 15:27: Android `2345` contract sync completed for backend order payment response. Android `OrderResponse` DTO now accepts backend `payment` with method, status, transfer confirmation availability, seller verification requirement, proof requirement, nullable proof URL, instruction, and payment history. Current UI remains backward-compatible and still maps existing order fields; this change prevents contract mismatch after backend added richer payment clarity. Verification passed: Android/backend contract verifier against live local `/v3/api-docs`, targeted `:data:compileDebugKotlin :domain:compileDebugKotlin`, and full `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`. Fastlane Firebase App Distribution upload to `internal-tester` succeeded with notes `Shopme debug build - 2345 order payment contract and transition validation`; console `https://console.firebase.google.com/project/shopme-app-360b4/appdistribution/app/android:com.mtv.app.shopme/releases/0uin3k7d8bp2g?utm_source=fastlane`, tester `https://appdistribution.firebase.google.com/testerapps/1:870455241065:android:76c3f0351cddb70a5f75f8/releases/0uin3k7d8bp2g?utm_source=fastlane`.
- 2026-05-17 16:20: Backend `2345` Customer Discovery endpoints were added without Android DTO/API changes in this pass. Android static contract verifier passed against live local OpenAPI with `--skip-runtime`; full runtime verifier remains blocked by the known Windows/public-MinIO media upload connectivity issue, while backend runtime smoke covered discovery/search/recent/suggestions/favorites directly. No Android build artifact changed, so no Fastlane upload was needed.
- 2026-05-17 16:58: Backend `2345` remaining foundation endpoints were added without Android source changes in this pass. New backend contracts include seller bulk product active toggle, seller product stats, paged customer order/notification/chat detail alternatives, Redis-backed rate-limit envelopes, and admin capability/security-boundary draft endpoints. Android verification passed: `python scripts\verify_backend_contract.py --backend-root D:\Boys\Backend\shopme-backend --openapi-url http://127.0.0.1:8080/v3/api-docs --skip-runtime` and full `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`. Full runtime contract verifier with demo credential overrides reached media upload and failed only on Windows socket access to public MinIO (`minio.dsrv-developer.my.id:443`), same known environment limitation. No Android code or APK artifact changed, so no Fastlane upload was needed.
- 2026-05-17 18:12: Backend 10k-user scale cleanup pass was completed without Android source changes. Backend capped legacy `GET /api/foods` to 50 active items, moved food search/random/discovery/cafe filters to DB-paged queries, fixed `sort=random` pageable handling, optimized chat list to DB conversation summaries, and fixed Redis/HTTP JavaTime JSON handling. Android verification passed against the live rebuilt backend: static contract verifier with `--skip-runtime` and full `.\gradlew.bat testDebugUnitTest assembleDebug "-Pksp.incremental=false" --no-daemon`. No Fastlane upload was performed because Android code/APK did not change.
- 2026-05-17 19:18: Backend 10k-user pressure follow-up completed without Android source changes. A light 5-worker backend load exposed Redis cache deserialization 500s on volatile food home/discovery cache entries, so backend disabled those `@Cacheable` reads and now serves them from capped DB-paged indexed queries. Final live backend pressure test covered foods, search, discovery, chat list, order pages, and notifications with 0 errors; Android/backend contract verifier still passed with `--skip-runtime`. No Fastlane upload was performed because the Android APK did not change.
- 2026-05-17 19:37: Android scale fallback cleanup completed. Home already used paged random search, but old fallback `FoodRemoteDataSource.getFoods()` still targeted legacy `/api/foods` and old cafe fallback used legacy `/api/foods/cafe/{id}`. These now use paged backend paths instead: `getFoods()` calls `/api/foods/search?page=0&size=20&sort=random` and `getFoodsByCafe(id)` calls `/api/foods/cafe/{id}/page?page=0&size=30`. Backend also reduced legacy payload caps to 20 home items and 30 cafe items. Verification passed: targeted `:data:testDebugUnitTest :feature-customer:testDebugUnitTest`, full Android `testDebugUnitTest assembleDebug`, full backend tests, Docker backend rebuild, runtime smoke showing `foodsCount=20` and `cafeFoodsCount=30`, Android/backend contract verifier, and backend pressure smoke with 0 errors. Fastlane debug upload is required because Android source/APK changed in this pass.
- 2026-05-17 19:52: Fastlane upload completed for the Android scale fallback cleanup. Local `fastlane/.env` credential path was normalized to `D:/Boys/Android/shopme-app/app/shopme-app-service-account.json` so Ruby/Fastlane can read it on Windows. Firebase App Distribution upload to group `internal-tester` succeeded with notes `Shopme debug build - scale fallback paged food endpoints legacy payload cap`; console `https://console.firebase.google.com/project/shopme-app-360b4/appdistribution/app/android:com.mtv.app.shopme/releases/7lb6hehb9hmeo?utm_source=fastlane`, tester `https://appdistribution.firebase.google.com/testerapps/1:870455241065:android:76c3f0351cddb70a5f75f8/releases/7lb6hehb9hmeo?utm_source=fastlane`.
- 2026-05-17 20:43: Android customer discovery consumption completed. Added `DiscoveryParam`, `GetDiscoveryFoodUseCase`, repository/data-source support for `/api/foods/discovery/{section}`, and endpoint wiring. Customer Home now loads paged backend `recommendations` discovery instead of reusing blank search, while Search with an empty query loads backend `newest` discovery; typed search still uses `/api/foods/search` so recent-search recording remains intact. Verification passed: targeted domain/data/customer compile + customer tests, full Android `testDebugUnitTest assembleDebug`, backend full tests, Docker backend rebuild, live discovery runtime smoke (`recommendations=5`, `newest=5`), Android/backend contract verifier, and 40-request light backend pressure smoke with 0 errors. Fastlane upload is required because Android APK changed in this pass.
- 2026-05-17 20:44: Fastlane upload completed for backend aggregate seller stats + Android discovery consumption. Firebase App Distribution upload to group `internal-tester` succeeded with notes `Shopme debug build - backend aggregate seller stats and Android discovery endpoints`; console `https://console.firebase.google.com/project/shopme-app-360b4/appdistribution/app/android:com.mtv.app.shopme/releases/72f5t34qkth9g?utm_source=fastlane`, tester `https://appdistribution.firebase.google.com/testerapps/1:870455241065:android:76c3f0351cddb70a5f75f8/releases/72f5t34qkth9g?utm_source=fastlane`.
