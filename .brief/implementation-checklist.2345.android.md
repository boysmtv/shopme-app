# Android 2345 Implementation Checklist

## Production Device QA

- [ ] Install Firebase App Distribution debug build on a real device.
- [ ] Verify Chucker appears and captures auth/product/order/chat/notification/media HTTP calls.
- [ ] Verify seller saved session opens seller dashboard.
- [ ] Verify customer saved session opens customer home.
- [ ] Verify expired token returns to login.
- [ ] Verify foreground order notification.
- [ ] Verify background order notification.
- [ ] Verify foreground chat notification.
- [ ] Verify background chat notification.
- [ ] Verify websocket reconnect after app background/foreground.
- [ ] Verify websocket reconnect after network change.
- [ ] Verify chat bubbles, checkmarks, online status, auto-scroll, and retry on device.
- [ ] Verify public MinIO image URLs load on mobile data.

## Order Flow

- [x] Add order timeline component.
- [x] Render timeline from backend status history when available.
- [x] Add customer cancel-before-cooking action when backend supports it.
- [x] Add seller reject/cancel with reason when backend supports it.
- [x] Improve transfer proof/payment status display.
- [x] Deep-link order notifications to the right order/detail screen.
- [x] Keep order UI labels human-readable and hide technical IDs from primary text.
- [x] Add focused ViewModel/UI-state tests for timeline and cancel/reject gates.

## Seller Product Management

- [x] Build full edit product flow for name, description, category, status, stock, variants, prices, and images.
- [x] Add image preview before upload.
- [x] Validate empty/broken image states before submit.
- [x] Add seller product search.
- [x] Add seller product filters.
- [ ] Add bulk enable/disable if backend supports it.
- [ ] Add product stats display only after backend exposes real stats.
- [x] Ensure list pagination and pull refresh remain stable after edits.
- [x] Add tests for edit form state and submit payload mapping.

## Customer Discovery

- [ ] Improve Favorite screen filtering by cafe/category/status.
- [ ] Add recent search UI.
- [ ] Add search suggestions when data source is available.
- [ ] Add recommendation sections after backend contract is ready.
- [ ] Add Terlaris section after backend contract is ready.
- [ ] Add Dekat kamu section after backend contract is ready.
- [ ] Add Promo section after backend contract is ready.
- [ ] Add Baru ditambahkan section after backend contract is ready.
- [ ] Add rating display only after backend exposes ratings.

## Scale-Facing UX

- [ ] Audit all list screens for pagination/refresh/empty/error/append-loading consistency.
- [ ] Verify active tab/menu taps refresh instead of duplicating navigation.
- [ ] Add retry UI for failed order mutations.
- [ ] Add retry UI for failed cart mutations.
- [ ] Add retry UI for failed product mutations.
- [ ] Keep Chucker enabled in debug and absent from release if release flavor is added later.

## Verification

- [x] Targeted compile for changed modules.
- [x] Focused unit tests for changed ViewModels/mappers.
- [x] Full `.\gradlew.bat testDebugUnitTest assembleDebug`.
- [x] Contract verifier against backend OpenAPI when API usage changes.
- [x] Fastlane upload to Firebase App Distribution group `internal-tester`.
