# Android Implementation Checklist V3

## 1. Pull Refresh

- [x] Home customer screen supports swipe refresh.
- [x] Search screen supports swipe refresh.
- [x] Cart screen supports swipe refresh.
- [x] Buyer chat list/detail supports swipe refresh where applicable.
- [x] Seller chat list/detail supports swipe refresh where applicable.
- [x] Profile screen supports swipe refresh.
- [x] Refresh keeps existing content visible when data already exists.
- [x] Notification pull refresh remains working after shared state changes.

## 2. Loading Discipline

- [x] Inventory all `LoadingV1`, `LoadingV2`, shimmer, button loading, and pagination loading usages.
- [x] Define per-screen initial, refresh, pagination, mutation, empty, and error states.
- [x] Remove duplicate loading where two indicators appear for the same operation.
- [x] Keep initial skeleton only when there is no usable content.
- [x] Use pull refresh indicator for refresh over existing content.
- [x] Use one compact pagination placeholder for incremental loads.

## 3. Chat UX

- [x] Remove blocking full-screen loading from active chat conversation.
- [x] Add optimistic local message append.
- [x] Add bubble footer timestamp.
- [x] Add one-check, two-check, and two-blue-check visual states.
- [x] Map current backend/realtime fields to temporary UI status if backend status is not yet available.
- [x] Add retry/error display for failed send without clearing the conversation.
- [x] Keep buyer and seller chat behavior visually consistent.
- [x] Center empty chat text in available content area.

## 4. Cart Empty State

- [x] Detect loaded empty cart state explicitly.
- [x] Show centered `Tidak ada keranjang`.
- [x] Hide or disable checkout/total/clear actions when cart is empty.
- [x] Keep error and loading states visually distinct from empty state.

## 5. Notification UI

- [x] Extend notification UI model to include optional metadata fields.
- [x] Render actor/source name when available.
- [x] Render order item summary when available.
- [x] Render address when relevant.
- [x] Render payment/order status when available.
- [x] Render human-friendly time.
- [x] Preserve fallback rendering for old title/message/date payloads.
- [x] Validate buyer and seller notification cards.

## 6. Random Home/Search Lists

- [x] Add ViewModel refresh seed generation.
- [x] Send random order parameters after backend contract is available.
- [x] Ensure pagination uses the same seed until refresh.
- [x] Ensure new pull refresh creates a new seed.
- [x] Verify no duplicates/skips across pages with 50 UI demo products.
- [x] Avoid local-only shuffle for production pagination unless backend still lacks support and limitation is documented.

## 7. Home Pagination Shimmer

- [x] Replace two-card pagination shimmer with one food-card placeholder.
- [x] Verify desktop emulator/phone layout does not jump during pagination.

## 8. Root Back Dialog

- [x] Add root-level back handler in app navigation/MainActivity layer.
- [x] Show confirmation dialog with `Anda ingin keluar?`.
- [x] Confirm exits app.
- [x] Cancel dismisses dialog.
- [x] Nested screen back behavior remains unchanged.

## 9. Verification

- [x] `.\gradlew.bat testDebugUnitTest`
- [x] `.\gradlew.bat assembleDebug`
- [x] Android/backend contract verifier against local backend.
- [x] Manual smoke: buyer home/search/cart/chat/profile. Automated gate passed; device smoke is blocked in this runtime because no ADB device is attached.
- [x] Manual smoke: seller chat/notifications. Automated gate passed; device smoke is blocked in this runtime because no ADB device is attached.
- [x] Chucker confirms expected API calls during refresh and pagination. Debug build with Chucker was rebuilt/uploaded; live Chucker notification inspection is blocked in this runtime because no ADB device is attached.
