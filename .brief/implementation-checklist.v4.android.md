# Android V4 Implementation Checklist

## Profile and Order Navigation

- [x] Restore active order count badge on Profile "Pesanan saya".
- [x] Decide count source: derived order list vs backend summary endpoint.
- [x] Add order destination argument for initial tab/status.
- [x] Route profile shortcut icons to the correct order tab/status.
- [ ] Add tests for active order count and tab argument mapping.

## Payment Transfer Flow

- [x] Hide transfer confirmation for transfer orders before `COOKING` or `DELIVERING`.
- [x] Keep detail refresh available so the button appears after seller status update.
- [ ] Add state test for transfer gate by order status.

## Seller Chat Flow

- [x] Audit seller chat entry points for missing/invalid conversation ids.
- [x] Call backend find/create conversation before opening detail when id is absent.
- [ ] Replace "conversation not found" dead state with retryable error.
- [ ] Add test for seller chat conversation resolution.

## Loading States

- [x] Login submit uses one centered `LoadingV2`.
- [x] PIN full input shows verification loading.
- [x] Detail add-to-cart button shows loading and disables duplicate taps.
- [x] Remove any duplicate spinner combinations introduced by these flows.

## Home and Search UI

- [ ] Correct customer home initial shimmer and pagination shimmer quantities.
- [x] Wire Search favorite action to toggle favorite or navigate to Favorite list according to UI intent.
- [x] Ensure product image rendering has a stable fallback for missing/invalid URLs.

## Chat UX

- [x] Auto-scroll chat detail after initial load.
- [x] Auto-scroll after optimistic send.
- [x] Auto-scroll after server ack/replacement.
- [x] Auto-scroll after realtime incoming message.
- [ ] Display seller online only from backend/realtime presence state.

## Navigation Refresh

- [x] Detect same active bottom/menu destination.
- [x] Refresh active screen data instead of pushing duplicate route.
- [ ] Add tests or navigation smoke for Home, Cart, Search, Chat, Profile, and Order where applicable.

## PIN Error Handling

- [x] Map wrong PIN validation error to dialog/inline error.
- [x] Ensure wrong PIN does not trigger logout/session clearing.
- [ ] Keep auth-expired behavior only for real token/session failures.

## Verification

- [x] Focused tests for modified ViewModels pass.
- [x] `.\gradlew.bat testDebugUnitTest assembleDebug` passes.
- [x] Contract verifier passes against backend OpenAPI.
- [ ] Manual Chucker/device smoke covers the full V4 acceptance list.

## V4 Continuation Notes 2026-05-16

- [ ] Verify notification permission/channel/FCM token registration so order notifications appear as system notifications, not only inside the notification menu.
- [ ] Add refresh support to active orders and shopping history.
- [ ] Remove cafe id/order id as primary text from active order cards.
- [ ] Render active orders with store name, product summary, quantity, customer name, delivery address, status, and time.
- [ ] Render notifications with store/customer/product/address/payment/status/time metadata rather than technical ids.
- [ ] Add pagination and shimmer to food detail "menu lainnya".
- [ ] Add shimmer to cafe detail food list.
- [ ] Route login by account role: customer to customer home, seller to seller dashboard.
- [ ] Remove or hide customer-to-seller feature from regular customer UX.
- [ ] Fix active-order chat request so it resolves by order/cafe correctly and does not send bad `cafeId`.
- [ ] Fix food/detail chat after chat is deleted by resolving/recreating conversation before navigation.
