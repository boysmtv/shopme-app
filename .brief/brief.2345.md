# Android Shopme Development Brief 2345

## Theme

Production-readiness pass after V5. This brief focuses on real-device QA, a more complete order experience, seller product editing, customer discovery, scale-facing UX behavior, and future admin/backoffice readiness. Android should stay role-strict: customer sessions stay in customer UI, seller sessions stay in seller UI.

## User Intent

- Save the post-V5 development analysis as the next direction.
- Use this backlog later under the label `2345`.
- Prioritize the features that make Shopme usable for real users and sellers before adding broad new modules.

## Priority 1: Real-Device Production QA

The current automated pipeline is green, but device-only smoke still needs a connected phone.

Android-owned work:

- Verify the Firebase debug build installs and launches from App Distribution.
- Verify Chucker appears and records auth, product, order, chat, notification, media, and websocket-related HTTP calls.
- Verify saved-login routing:
  - Seller saved session opens seller dashboard.
  - Customer saved session opens customer home.
  - Expired token still goes to login without clearing typed login input on invalid credential.
- Verify foreground/background push notifications for order, chat, and payment/status events.
- Verify websocket behavior after idle, app background/foreground, screen lock, and network change.
- Verify chat UX on device: right/left bubble mapping, checkmarks, online status, auto-scroll, retry.
- Verify product/cafe images load from public MinIO/domain URLs on mobile network.

Backend-dependent checks:

- Public API and MinIO tunnel/domain must be reachable from device.
- Push payload must include enough notification/data fields for Android routing and display.
- Websocket presence and chat events must stay stable through reconnect.

## Priority 2: Production Order Flow

Order screens should feel like a reliable transaction system, not just a status list.

Android-owned work:

- Add an order timeline UI with readable steps:
  - Dipesan
  - Cooking
  - Delivering
  - Completed
  - Cancelled/Rejected when applicable
- Show timestamps for each transition when backend provides them.
- Add customer cancel action before seller moves the order to `COOKING`.
- Add seller reject/cancel action with reason entry.
- Improve transfer payment display:
  - show uploaded proof clearly,
  - show payment status history,
  - keep confirm transfer visible only for allowed statuses.
- Make every order notification deep-link to the relevant order/detail tab.
- Keep technical IDs hidden from primary UI; use store, customer, items, quantity, address, status, payment, and time.

Backend-dependent work:

- Backend should expose status history/timeline data.
- Backend should support cancel/reject reason semantics.
- Backend should return stable deep-link metadata in notification data.

## Priority 3: Seller Product Management

Seller product management should support day-to-day seller operations.

Android-owned work:

- Full edit product screen:
  - name,
  - description,
  - category,
  - status,
  - stock/quantity,
  - variants,
  - prices,
  - images.
- Preview selected images before upload.
- Validate empty/broken image states before submit.
- Add seller product search and filters.
- Add bulk enable/disable if backend supports it.
- Add simple product stats display when backend supports it:
  - sold count,
  - view count,
  - favorite count.
- Keep seller list pagination and pull refresh stable after edits.

Backend-dependent work:

- Product update contract must support variants, prices, stock/status, and media changes.
- Product list/search should support seller filtering.
- Product stats endpoint or fields should be defined before Android renders stats as real numbers.

## Priority 4: Customer Discovery

Customer browsing should help users find food faster.

Android-owned work:

- Strengthen Favorite screen with filters by cafe/category/status.
- Add recent search UI and search suggestions when backend/local data is available.
- Add recommendation sections:
  - favorite-based,
  - category-based,
  - order-history-based,
  - same-cafe/similar products.
- Add sections for:
  - Terlaris,
  - Dekat kamu,
  - Promo,
  - Baru ditambahkan.
- Add rating display if backend supports ratings.

Backend-dependent work:

- Define discovery APIs or query params for recommendations, best-selling, nearby, promo, new products, and ratings.
- Keep pagination deterministic and duplicate-free.

## Priority 5: Scale-Facing Mobile Behavior

Android should avoid unnecessary network and UI churn as data grows.

Android-owned work:

- Audit all list screens for pagination, refresh, empty, error, and append-loading consistency.
- Avoid duplicate navigation when tapping the active tab/menu.
- Keep cache/fallback behavior scoped to safe list displays only.
- Add retry UI for important failed mutations: order, chat, cart, product update, payment.
- Keep API calls visible through Chucker in debug builds.

Backend-dependent work:

- Backend should add indexes/caching/observability/rate limiting; Android should consume resulting contracts without assuming data order unless documented.

## Priority 6: Admin/Backoffice Readiness

Admin is not Android primary scope yet, but Android should not block it later.

Android-owned work:

- Keep role routing extensible if future `ADMIN` role is returned.
- Do not expose admin-only behavior in customer/seller UI.
- Keep notification metadata generic enough to support moderation/broadcast later.

## Acceptance Criteria

- Existing V5 behavior remains intact.
- Seller/customer saved sessions never cross into the wrong menu.
- Device smoke has a written result for Chucker, notification, websocket, chat, image loading, and role routing.
- New order/product/discovery work is backed by explicit backend contracts before Android displays fake production data.
- Full Android pipeline passes before publishing:
  - targeted compile for changed modules,
  - focused unit tests,
  - `testDebugUnitTest assembleDebug`,
  - Android/backend contract verifier when API usage changes,
  - Fastlane upload to `internal-tester` only after green pipeline.
