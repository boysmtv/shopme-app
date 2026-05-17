# Android Shopme Development Brief V3

## Theme

UI/UX refresh for the customer and seller mobile experience after public tunnel, Chucker, Fastlane, and UI demo catalog data are stable.

This version focuses on reducing duplicate loading, adding pull-to-refresh in key surfaces, making chat feel realtime like WhatsApp, improving empty states, enriching notification presentation, randomizing food discovery lists, and correcting root back navigation.

## User Notes

- Add swipe refresh in home, cart, search, chat, and profile.
- Audit duplicate loading across code, features, and flows.
- Chat between customer and seller should feel like WhatsApp: no blocking loading, only checkmark states: one check, two checks, and two blue checks.
- Cart empty state must show "Tidak ada keranjang" centered.
- Empty chat text is not centered.
- Customer/seller notifications should show who triggered it, address, order item summary, time, payment/status context, or equivalent useful context.
- Home and search lists must always feel random, not fixed ascending or descending order.
- Customer home pagination shimmer currently renders two card placeholders; it should be one food-card placeholder.
- Back press in MainActivity root should show an exit confirmation dialog: "Anda ingin keluar?"

## Current Findings

### Pull-to-refresh coverage

Notifications already use pull refresh. Home, cart, search, chat, and profile still need consistent refresh behavior. Refresh must reuse each ViewModel's existing load action where possible and must not clear useful existing content during refresh.

### Duplicate loading

The app currently mixes full-screen loading, shimmer loading, pagination loading, and inline button loading. This is acceptable only when each state has a clear role:

- Initial load with no cached/usable data: skeleton or empty placeholder.
- User refresh with existing data: pull indicator only.
- Pagination: one compact footer placeholder, not a second full list state.
- Mutation/send action: optimistic state or disabled action, not a full-screen spinner.

Chat is the highest-priority cleanup because conversation should remain visible while messages are sent or refreshed.

### Chat UX

Buyer and seller chat should use the same interaction model:

- Messages stay visible during refresh.
- Sending a message appends it optimistically to the list.
- Bubble footer shows time and delivery/read status.
- Status display:
  - one gray check: sent by local client / accepted locally.
  - two gray checks: delivered to backend or recipient stream.
  - two blue checks: read by recipient.
- Send failure should keep the message visible with retry affordance; it should not clear the input unexpectedly.
- No blocking `LoadingV2` over an active conversation.

Backend support is required for an authoritative read/delivery contract. Until then, Android may map current local/realtime states as a compatibility layer, but the UI must be prepared for backend message status fields.

### Cart empty state

When cart item list is empty after load, render a centered message:

`Tidak ada keranjang`

Checkout, total, and clear actions must not appear as if there is something actionable when the cart is empty.

### Chat empty state

Empty chat/list copy must be centered within the available content area for both buyer and seller chat surfaces. It should not float near the top because that reads like a layout bug.

### Notifications

The current mobile display can only show generic title/message/date. For a useful customer/seller notification, Android should display structured context when present:

- Actor or owner name.
- Role or source: customer, seller, cafe, payment/order system.
- Address when relevant.
- Order ID and item summary when relevant.
- Payment status or order status when relevant.
- Human-friendly timestamp.

The app must keep a fallback for older backend payloads: title, message, created time.

### Home and search randomization

Random discovery must not break pagination. The preferred contract is backend-side seeded random order:

- Android sends `sort=random` plus a stable `seed` per refresh/session.
- Backend returns deterministic pages for that seed.
- A new refresh creates a new seed, changing the order.

If Android shuffles locally without backend seed support, pagination can duplicate or skip items. That fallback is acceptable only for temporary UI testing, not for production behavior.

### Home pagination shimmer

Customer home pagination currently renders two placeholder cards. Replace this with one food-card footer placeholder so the pagination load looks like the next item is coming in, not a second duplicated loading grid.

### MainActivity back behavior

At the root of app navigation, back press should show an exit confirmation dialog:

- Title/message: "Anda ingin keluar?"
- Confirm exits the app.
- Cancel dismisses the dialog.

Nested screens should still use normal back behavior before the exit dialog is considered.

## Scope

### Android-owned

- Add pull refresh to Home, Cart, Search, Chat, and Profile.
- Audit screen state rendering and remove duplicate/blocking loading.
- Implement WhatsApp-like chat bubbles, timestamps, optimistic send, and checkmark UI.
- Add centered cart and chat empty states.
- Render richer notification UI with fallback.
- Add random feed/search request parameters once backend contract exists.
- Reduce home pagination shimmer to one food-card placeholder.
- Add root back exit dialog.
- Add focused ViewModel/UI state tests where behavior is state-driven.

### Backend-dependent

- Notification metadata must be returned in API and realtime notification events.
- Random food/search order should be backend-seeded for pagination safety.
- Chat message status/read receipt fields should be exposed consistently.

## Acceptance Criteria

- Home, Cart, Search, Chat, and Profile all support pull-to-refresh without blanking existing data.
- A refresh does not show both pull indicator and full-screen loading over existing data.
- Pagination loading in customer home shows one food-card placeholder.
- Empty cart renders centered "Tidak ada keranjang".
- Empty chat copy is centered.
- Sending chat messages never blocks the conversation UI.
- Chat bubbles show time and checkmark status for local user's messages.
- Notification cards show useful actor/order/payment/address context when backend metadata exists, and fall back cleanly when it does not.
- Home and Search use seeded random ordering when backend supports it.
- Root back press shows the exit dialog and nested back navigation remains correct.
- Android unit/build pipeline passes.

## Release Gate

Before Firebase distribution:

1. Run Android unit tests and debug assemble.
2. Verify buyer and seller chat manually with Chucker enabled.
3. Verify home/search pagination with at least 50 demo products.
4. Verify notification list against both old minimal payload and new metadata payload.
5. Verify root back behavior from customer and seller main tabs.
