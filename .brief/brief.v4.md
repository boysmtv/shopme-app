# Android Shopme Development Brief V4

## Theme

Customer UI/UX correction and cross-flow behavior cleanup after V3. This version focuses on order/profile navigation, payment timing, seller/customer chat reliability, loading consistency, chat scroll/presence, favorite entry points, product image validity, repeated-tab refresh behavior, PIN error handling, and mutation loading feedback.

## User Notes

- Profile no longer shows active count on the "Pesanan saya" icon.
- Tapping a profile/order shortcut icon should navigate to the order list/detail and select the matching tab such as "Dipesan" or "Order"; other icons should behave the same way.
- Transfer payment confirmation should appear only after order status becomes cooking or delivering.
- Seller still cannot chat customer; backend returns "conversation not found".
- Login loading looks bad; it should show only `LoadingV2` centered.
- Customer home shimmer still looks like two food items instead of per-item/one expected placeholder.
- Chat detail should auto-scroll to the newest sent/received message like WhatsApp.
- Chat header should show seller active/online status when available.
- Search page has a love/favorite button but it does not navigate to Favorite.
- Ensure every product rendered in Android has a valid image.
- When customer taps the same open menu/tab such as cart again, do not recreate/open the same menu again; refresh that tab's data instead.
- PIN full input has no loading feedback.
- Add loading feedback when adding to cart from detail.
- Wrong PIN currently logs user out; it should show a wrong-PIN error and keep the session.
- Order push/system notification does not appear even though new data exists in the notification menu.
- Active orders and shopping history need pull/active refresh.
- Active order cards and notifications must not show cafe id or order id as primary text; show store name, product name(s), quantity, customer name, and delivery address.
- Food detail "menu lainnya" should support pagination and shimmer placeholders.
- Cafe detail food list should show shimmer while loading.
- Login must route directly by account role: customer to customer home, seller to seller dashboard. Seller should not be able to act as customer, and the customer-to-seller feature should be removed.
- Active order chat currently sends a bad `cafeId` request parameter.
- Food detail chat cannot be opened after the chat conversation was deleted from the chat menu.

## Current Findings

### Profile order count and shortcut routing

Profile shortcuts need a single source of truth for counts and destination routing. "Pesanan saya" should display an active order count from the order list/domain state, not a stale local value. Shortcut taps should pass a target order tab/status into the order destination so the order screen opens directly on the relevant tab instead of defaulting to the first tab.

Android-owned work:

- Add active order count state to Profile ViewModel or shared order summary source.
- Keep profile count resilient when order API is loading or fails.
- Add navigation arguments for order tab/status.
- Ensure all profile shortcuts either deep-link to the correct screen/tab or refresh when already selected.

Backend-dependent work:

- If active order count is expensive or absent, backend should expose a lightweight summary/count endpoint, or Android can derive it temporarily from the paged/list order response.

### Transfer confirmation timing

The current buyer flow allows transfer confirmation too early. For transfer payment orders, the confirmation UI should be hidden until the backend order status is `COOKING` or `DELIVERING`. If backend still accepts early confirmation, Android must still gate the button to prevent bad UX, and backend must enforce the rule for correctness.

### Seller to customer chat

Seller chat sending fails with "conversation not found". Android should not assume the seller has a conversation id just because an order/customer row exists. Seller chat entry should either:

- Open an existing conversation id supplied by backend metadata/order detail/chat list.
- Or call a backend endpoint that can create/find the conversation by order/customer/cafe before navigating to chat detail.

The UI must surface a clear retryable error if the conversation cannot be resolved, not a dead chat screen.

### Login loading

Login submit should show a single centered `LoadingV2` overlay or inline center state. It should not combine multiple loading indicators, shift form layout, or show a visually noisy spinner inside the button and screen at the same time.

### Customer home shimmer

Home initial/pagination shimmer still reads like duplicate food cards. The final behavior should be:

- Initial load with no content: skeleton sized like actual home content.
- Pagination: one compact next-item placeholder only.
- Refresh with existing content: pull indicator only.

### Chat auto-scroll and presence

Chat detail should scroll to the newest message after:

- Initial message list load.
- Local optimistic send.
- Server acknowledgement/replacement.
- Realtime incoming message.

Chat header should show presence such as "Seller online" only when the backend/realtime source says the seller is active. If presence is unavailable, avoid fake status text.

### Search favorite action

Search product love/favorite action must connect to the Favorite feature. If the icon means "toggle favorite", it should update favorite state and stay on Search. If it means "open favorites", it should navigate to Favorite. The UI must not expose a dead action.

Preferred behavior:

- Product card heart toggles favorite for that product.
- A dedicated favorite shortcut navigates to Favorite list.

### Product image validity

Android should render a stable fallback image state when backend image data is missing or invalid, but the production/demo data should also be fixed so product cards are not filled with broken URLs. Contract should require at least one valid display image per product in list/detail responses.

### Same-tab click behavior

Bottom navigation and menu shortcuts should detect when the destination is already selected:

- If different destination: navigate normally.
- If same destination: do not push another copy of the route; call refresh on the active ViewModel/screen.

This is especially important for cart/search/profile/order tabs so the stack does not grow when the user repeatedly taps the same item.

### PIN flow

PIN input should show loading once the PIN length is complete and verification starts. Wrong PIN should be treated as a validation error, not an auth/session failure. The app must not logout or clear token on wrong PIN unless backend explicitly returns an expired/invalid token error unrelated to PIN.

### Add to cart from detail

Food detail add-to-cart needs a mutation loading state. Button should be disabled while the request is running and show clear loading feedback, without covering the whole detail screen.

### Notification delivery and order display

Notification data exists in the notification menu, but push/system notification delivery is unreliable. Android should verify FCM token registration/update and notification permission/channel setup, while backend should verify notification metadata and FCM send path if present. Order cards and notification cards should prioritize human-readable information: store name, product summary, quantity, customer name, delivery address, payment/order status, and time. Technical IDs may remain available for diagnostics but should not be primary UI labels.

### Role-specific login and seller/customer split

After login the app should route by account role. Customer accounts land on the customer home. Seller accounts land on the seller dashboard. Seller accounts should not browse as a customer in this V4 UX, and the customer-to-seller/create-seller entry should be removed or hidden from the app. Any existing seller setup flow should only remain reachable from an explicit admin/onboarding path if still needed.

### Detail and cafe product lists

Food detail "menu lainnya" and cafe detail product lists need list-state polish for larger data sets: shimmer during first load, pagination/next-page loading where supported, and stable empty/error states.

### Chat entry reliability

Any chat entry from active order/detail/food/cafe must resolve a valid conversation before opening chat. If a user deleted/cleared a conversation from the chat menu, detail chat must recreate/find it instead of navigating to a stale id or sending a bad `cafeId`.

## Scope

### Android-owned

- Restore active order count badge in Profile.
- Add order/profile shortcut navigation arguments and default tab selection.
- Hide transfer confirmation until order status is cooking/delivering.
- Resolve seller chat entry with valid conversation id or create/find call before detail.
- Replace login submit loading with a centered `LoadingV2` presentation.
- Correct home initial and pagination shimmer quantities.
- Auto-scroll chat detail to bottom for initial load, send, ack, and realtime incoming.
- Display seller online/presence when backend state exists.
- Wire Search favorite action to favorite toggle/list navigation.
- Add valid image fallback rendering and avoid showing broken cards.
- Change same-tab menu clicks to refresh instead of duplicate navigation.
- Add PIN verification loading and wrong-PIN dialog/state.
- Add add-to-cart loading from detail.
- Verify notification permission/channel/token registration and visible system notification behavior.
- Add refresh to active order and shopping history screens.
- Replace technical ids in active order and notification cards with store/product/quantity/customer/address/status/time.
- Add pagination and shimmer for food detail related/menu list.
- Add shimmer for cafe detail food list.
- Route login by role and remove/hide customer-to-seller flow for regular users.
- Fix active-order chat parameter and detail-chat recreation after chat deletion.
- Add focused ViewModel tests for profile count, PIN wrong error, order transfer gate, seller chat conversation resolution, same-tab refresh, and detail add-cart loading.

### Backend-dependent

- Enforce transfer confirmation only after allowed order statuses.
- Provide reliable seller-customer conversation find/create semantics.
- Provide realtime/user presence for seller online state.
- Ensure product list/detail media URLs are valid.
- Return wrong PIN as validation/400/422, not 401 session-invalid.
- Optionally provide lightweight active order count/summary for Profile.

## Acceptance Criteria

- Profile "Pesanan saya" shows the current active order count.
- Profile/order shortcut icons open the correct order tab/status.
- Transfer confirmation is not shown or accepted before cooking/delivering.
- Seller can open and send chat to customer without "conversation not found".
- Login submit shows one centered `LoadingV2`.
- Home shimmer no longer shows duplicated two-item food placeholders in pagination.
- Chat detail always lands at the latest message after send/receive.
- Chat header shows "Seller online" only from real presence state.
- Search favorite action is not dead and reaches the Favorite feature behavior.
- Product cards/details have valid image URLs or a polished fallback.
- Re-tapping the active customer menu refreshes data and does not push duplicate navigation.
- PIN verification shows loading when complete.
- Wrong PIN shows an error and does not logout.
- Detail add-to-cart shows loading and prevents duplicate taps.
- Order system notifications are visible when backend creates a new notification and device permission/token are valid.
- Active orders and shopping history refresh without leaving the screen.
- Active orders and notifications show human-readable store/product/customer/address data, not cafe/order ids as primary labels.
- Food detail related menus paginate and show shimmer.
- Cafe detail foods show shimmer during loading.
- Login role routing sends sellers directly to seller dashboard and customers to customer home.
- Customer-to-seller entry is hidden/removed from regular customer UX.
- Order/detail chat opens a valid conversation even after chat was cleared.

## Release Gate

1. Android focused ViewModel/state tests pass.
2. Android `testDebugUnitTest assembleDebug` passes.
3. Backend tests pass for changed order/chat/PIN/media contracts.
4. Android/backend contract verifier passes against local OpenAPI.
5. Manual device smoke with Chucker confirms login loading, profile badge, order tab deep-link, transfer gate, seller chat, chat auto-scroll, PIN wrong error, and add-cart loading.
