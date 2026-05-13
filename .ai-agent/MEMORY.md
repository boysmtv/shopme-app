# Shopme Android Agent Memory

Last refreshed: 2026-05-13 on Windows.

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

## Open Gaps

- Need run Android assemble/build gate after unit-test-only changes if release/debug APK verification is required.
- Need rerun backend Docker smoke and Android contract verifier on Windows.
- Need inspect mappers (`DomainMapper`, `RequestMapper`, `ResponseMapper`, `EntityMapper`) before any DTO-changing work.
