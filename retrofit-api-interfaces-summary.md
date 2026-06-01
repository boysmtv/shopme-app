# Shopme App - Retrofit API Interface Analysis

## Important Finding

**This project does NOT use standard Retrofit annotations** (`@GET`, `@POST`, `@PUT`, `@DELETE`, `@PATCH`, `@FormUrlEncoded`) or `retrofit2.Call`.

Instead, it uses a **custom networking layer** built on `com.mtv.based.core.network` which provides:
- `IApiEndPoint` interface (with `path`, `method`, `type` properties)
- `HttpMethod` enum (Get, Post, Put, Delete, Patch)
- `EndpointType` enum (Json, Multipart)
- `NetworkRepository` class for executing requests
- `RequestOptions` for query parameters and other options

There are **no Retrofit interface files** (no `.kt` files with `interface XxxApi` containing annotation-based endpoint definitions).

---

## Equivalent: ApiEndPoint Definitions

The file `data/src/main/java/.../data/remote/api/ApiEndPoint.kt` contains all endpoint definitions using the `IApiEndPoint` pattern.

### Auth Endpoints

| HTTP Method | Endpoint Path                   | Request Body / Params                    | Response Body                   |
|-------------|--------------------------------|------------------------------------------|----------------------------------|
| POST        | api/auth/register              | RegisterRequest                          | ApiResponse<Unit>                |
| POST        | api/auth/login                 | LoginRequest                             | ApiResponse<LoginResponse>       |
| POST        | api/auth/forgot-password       | ForgotPasswordRequest                    | ApiResponse<Unit>                |
| POST        | api/auth/verify-otp            | VerifyOtpRequest                         | ApiResponse<Unit>                |
| POST        | api/auth/reset-password        | ResetPasswordRequest                     | ApiResponse<Unit>                |
| POST        | api/auth/change-password       | ChangePasswordRequest                    | ApiResponse<Unit>                |

### Customer Endpoints

| HTTP Method | Endpoint Path                                    | Request Body / Params           | Response Body                       |
|-------------|--------------------------------------------------|---------------------------------|--------------------------------------|
| GET         | api/customer                                     | -                               | ApiResponse<CustomerResponse>        |
| PUT         | api/customer                                     | CustomerUpdateRequest           | ApiResponse<Unit>                    |
| DELETE      | api/customer                                     | -                               | ApiResponse<Unit>                    |
| GET         | api/customer/notification-preferences            | -                               | ApiResponse<NotificationPreferences> |
| PUT         | api/customer/notification-preferences            | NotificationPreferencesRequest  | ApiResponse<NotificationPreferences> |
| POST        | api/customer/pin                                 | VerifyPinRequest                | ApiResponse<VerifyPinResponse>       |
| PUT         | api/customer/pin                                 | ChangePinRequest                | ApiResponse<Unit>                    |
| GET         | api/customer/favorites                           | -                               | ApiResponse<List<String>>            |
| GET         | api/customer/favorites/items                     | -                               | ApiResponse<List<FoodResponse>>      |
| POST        | api/customer/favorites/{foodId}                  | foodId (path param)             | ApiResponse<Unit>                    |
| DELETE      | api/customer/favorites/{foodId}                  | foodId (path param)             | ApiResponse<Unit>                    |

### Foods Endpoints

| HTTP Method | Endpoint Path                            | Request Body / Params                                | Response Body                           |
|-------------|------------------------------------------|------------------------------------------------------|------------------------------------------|
| GET         | api/foods                                | -                                                    | ApiResponse<PageResponse<FoodResponse>>  |
| POST        | api/foods                                | FoodUpsertRequest                                    | ApiResponse<Unit>                        |
| GET         | api/foods/search                         | query: name, page, size, sort, seed                  | ApiResponse<PageResponse<FoodResponse>>  |
| GET         | api/foods/discovery/{section}            | section (path param), query: page, size, seed        | ApiResponse<PageResponse<FoodResponse>>  |
| GET         | api/foods/{foodId}                       | foodId (path param)                                  | ApiResponse<FoodResponse>                |
| PUT         | api/foods/{foodId}                       | foodId (path param), FoodUpsertRequest               | ApiResponse<Unit>                        |
| DELETE      | api/foods/{foodId}                       | foodId (path param)                                  | ApiResponse<Unit>                        |
| GET         | api/foods/cafe/{cafeId}                  | cafeId (path param)                                  | ApiResponse<PageResponse<FoodResponse>>  |
| GET         | api/foods/cafe/{cafeId}/page             | cafeId (path param), query: page, size, q, category, status, active | ApiResponse<PageResponse<FoodResponse>>  |
| GET         | api/foods/cafe/{cafeId}/stats            | cafeId (path param)                                  | ApiResponse<FoodStatsResponse>           |

### Cart Endpoints

| HTTP Method | Endpoint Path                     | Request Body / Params              | Response Body                       |
|-------------|-----------------------------------|------------------------------------|--------------------------------------|
| GET         | api/cart                          | -                                  | ApiResponse<List<CartItemResponse>>  |
| POST        | api/cart                          | FoodAddToCartRequest               | ApiResponse<Unit>                    |
| PUT         | api/cart/{cartId}                 | cartId (path param), CartQuantityRequest | ApiResponse<Unit>               |
| DELETE      | api/cart/clear                    | -                                  | ApiResponse<Unit>                    |
| DELETE      | api/cart/cafe/{cafeId}            | cafeId (path param)                | ApiResponse<Unit>                    |

### Order Endpoints

| HTTP Method | Endpoint Path                              | Request Body / Params              | Response Body                                |
|-------------|--------------------------------------------|------------------------------------|----------------------------------------------|
| GET         | api/order                                  | -                                  | ApiResponse<List<OrderSummaryResponse>>      |
| GET         | api/order/page                             | query: page, size                  | ApiResponse<PageResponse<OrderSummaryResponse>> |
| GET         | api/order/{orderId}                        | orderId (path param)               | ApiResponse<OrderResponse>                   |
| POST        | api/order/{orderId}/confirm-transfer       | orderId (path param)               | ApiResponse<Unit>                            |
| PATCH       | api/order/{orderId}/cancel                 | orderId (path param), CancelOrderRequest | ApiResponse<Unit>                       |
| PATCH       | api/order/{orderId}/{status}               | orderId, status (path params)      | ApiResponse<Unit>                            |
| GET         | api/order/session                          | -                                  | ApiResponse<SessionTokenResponse>            |
| POST        | api/order                                  | CreateOrderRequest                 | ApiResponse<List<OrderSummaryResponse>>      |

### Cafe Endpoints

| HTTP Method | Endpoint Path                  | Request Body / Params          | Response Body                       |
|-------------|--------------------------------|--------------------------------|--------------------------------------|
| POST        | api/cafe                       | CafeAddRequest                 | ApiResponse<Unit>                    |
| GET         | api/cafe/{cafeId}              | cafeId (path param)            | ApiResponse<CafeResponse>            |
| PUT         | api/cafe/{cafeId}              | cafeId (path param), CafeAddRequest | ApiResponse<Unit>                |
| GET         | api/cafe/address/{cafeId}      | cafeId (path param)            | ApiResponse<CafeAddressResponse>     |
| POST        | api/cafe/address               | CafeAddressUpsertRequest       | ApiResponse<Unit>                    |

### Chat Endpoints

| HTTP Method | Endpoint Path                        | Request Body / Params                    | Response Body                          |
|-------------|--------------------------------------|------------------------------------------|-----------------------------------------|
| GET         | api/chat/list                        | query: asSeller                          | ApiResponse<ChatListResponse>          |
| GET         | api/chat                             | query: id, asSeller                     | ApiResponse<ChatResponse>              |
| GET         | api/chat/page                        | query: id, asSeller, page, size          | ApiResponse<PageResponse<ChatItem>>    |
| POST        | api/chat/conversation                | query: cafeId                            | ApiResponse<ChatConversationResponse>  |
| POST        | api/chat/conversation/order          | query: orderId                           | ApiResponse<ChatConversationResponse>  |
| POST        | api/chat/conversation/seller         | query: orderId                           | ApiResponse<ChatConversationResponse>  |
| POST        | api/chat/message                     | ChatMessageSendRequest, query: asSeller  | ApiResponse<Unit>                       |
| POST        | api/chat/read                        | ChatMessageMarkAsReadRequest, query: asSeller | ApiResponse<Unit>                  |
| DELETE      | api/chat                             | query: asSeller                          | ApiResponse<Unit>                       |

### Seller Endpoints

| HTTP Method | Endpoint Path                             | Request Body / Params           | Response Body                              |
|-------------|-------------------------------------------|----------------------------------|--------------------------------------------|
| GET         | api/seller/profile                        | -                                | ApiResponse<SellerProfileResponse>         |
| GET         | api/seller/payment-methods                | -                                | ApiResponse<SellerPaymentMethodResponse>   |
| PUT         | api/seller/payment-methods                | SellerPaymentMethodRequest       | ApiResponse<SellerPaymentMethodResponse>   |
| GET         | api/seller/orders                         | -                                | ApiResponse<List<SellerOrderSummaryResponse>> |
| GET         | api/seller/orders/page                    | query: page, size, status        | ApiResponse<PageResponse<SellerOrderSummaryResponse>> |
| GET         | api/seller/orders/{orderId}               | orderId (path param)             | ApiResponse<OrderResponse>                 |
| PUT         | api/seller/availability                   | SellerAvailabilityRequest        | ApiResponse<SellerProfileResponse>         |

### Notification Endpoints

| HTTP Method | Endpoint Path                     | Request Body / Params     | Response Body                                 |
|-------------|-----------------------------------|---------------------------|------------------------------------------------|
| GET         | api/notifications                 | -                         | ApiResponse<List<AppNotificationResponse>>     |
| GET         | api/notifications/page            | query: page, size         | ApiResponse<PageResponse<AppNotificationResponse>> |
| GET         | api/notifications/unread-count    | -                         | ApiResponse<Long>                              |
| PUT         | api/notifications/read-all        | -                         | ApiResponse<Unit>                              |

### Media Endpoints

| HTTP Method | Endpoint Path                        | Request Body / Params                    | Response Body                      |
|-------------|--------------------------------------|------------------------------------------|-------------------------------------|
| POST        | api/media/presign-upload             | query: scope, contentType                 | ApiResponse<MediaUploadResponse>   |
| POST        | api/media/upload                     | Multipart: file, scope (Multipart type)   | ApiResponse<MediaUploadResponse>   |

### Address Endpoints

| HTTP Method | Endpoint Path                  | Request Body / Params          | Response Body                       |
|-------------|--------------------------------|--------------------------------|--------------------------------------|
| GET         | api/address                    | -                              | ApiResponse<List<AddressResponse>>  |
| POST        | api/address                    | AddressAddRequest              | ApiResponse<Unit>                    |
| DELETE      | api/address/{id}               | id (path param)                | ApiResponse<Unit>                    |
| PATCH       | api/address/{id}/default       | id (path param)                | ApiResponse<Unit>                    |

### Misc Endpoints

| HTTP Method | Endpoint Path           | Request Body / Params        | Response Body                      |
|-------------|-------------------------|------------------------------|-------------------------------------|
| GET         | api/village             | -                            | ApiResponse<List<VillageResponse>> |
| POST        | api/splash              | SplashRequest                | ApiResponse<SplashResponse>        |
| GET         | api/support             | -                            | ApiResponse<SupportCenterResponse>  |
| GET         | api/support/chat        | -                            | ApiResponse<SupportChatResponse>    |
| POST        | api/support/chat        | SupportChatMessageRequest    | ApiResponse<SupportChatResponse>    |

---

## Remote Data Sources (callers of ApiEndPoint)

All remote data source files are in `data/src/main/java/.../data/remote/datasource/`:

| File                           | Uses Endpoints From           |
|-------------------------------|-------------------------------|
| AuthRemoteDataSource.kt       | Auth                          |
| AppRemoteDataSource.kt        | Misc (Splash, Support)        |
| CafeRemoteDataSource.kt       | Cafe                          |
| CartRemoteDataSource.kt       | Cart, Order, Customer         |
| ChatRemoteDataSource.kt       | Chat                          |
| FoodRemoteDataSource.kt       | Foods                         |
| MediaRemoteDataSource.kt      | Media                         |
| NotificationRemoteDataSource.kt | Notifications              |
| OrderRemoteDataSource.kt      | Order                         |
| ProfileRemoteDataSource.kt    | Customer, Address, Village    |
| SearchRemoteDataSource.kt     | Foods (Search)                |
| SellerRemoteDataSource.kt     | Seller, Order                 |

---

## Request Body Classes

Located in `data/src/main/java/.../data/remote/request/`:

- LoginRequest, RegisterRequest, ForgotPasswordRequest, VerifyOtpRequest, ResetPasswordRequest, ChangePasswordRequest
- ChangePinRequest, VerifyPinRequest
- CustomerUpdateRequest
- AddressAddRequest, AddressDeleteRequest, AddressDefaultRequest
- CafeAddRequest, CafeAddressUpsertRequest
- FoodAddToCartRequest, FoodVariantAddToCartRequest, FoodUpsertRequest
- CartQuantityRequest, CartClearByCafeRequest, CartInquiryRequest, CartValidateRequest
- CreateOrderRequest, CancelOrderRequest
- SearchFoodRequest
- ChatMessageSendRequest, ChatMessageMarkAsReadRequest, SupportChatMessageRequest
- NotificationPreferencesRequest
- SellerAvailabilityRequest, SellerPaymentMethodRequest
- SplashRequest

## Response Body Classes

Located in `data/src/main/java/.../data/remote/response/`:

- ApiResponse<T> (generic wrapper: timestamp, status, code, message, traceId, data)
- LoginResponse, SessionTokenResponse, VerifyPinResponse
- AppConfigResponse, SplashResponse
- UserResponse, CustomerResponse
- AddressResponse, VillageResponse
- CafeResponse, CafeAddressResponse
- SellerResponse, SellerProfileResponse, SellerPaymentMethodResponse
- FoodResponse, FoodStatsResponse, SimilarItem
- CartItemResponse, CartItemVariantResponse, CartInquiryResponse, CartValidateResponse
- OrderResponse, OrderItemResponse, OrderSummaryResponse
- PageResponse<T> (generic paginated response)
- ChatResponse, ChatListResponse, ChatListItemResponse, ChatConversationResponse
- ChatResponse, SupportCenterResponse, SupportChatResponse
- MediaUploadResponse
- AppNotificationResponse, NotificationPreferencesResponse

---

## HTTP Method Mapping

| HttpMethod enum | Equivalent HTTP Method |
|-----------------|----------------------|
| HttpMethod.Get    | GET                  |
| HttpMethod.Post   | POST                 |
| HttpMethod.Put    | PUT                  |
| HttpMethod.Delete | DELETE               |
| HttpMethod.Patch  | PATCH                |

## EndpointType Mapping

| EndpointType enum | Description                           |
|-------------------|---------------------------------------|
| EndpointType.Json     | JSON request/response body            |
| EndpointType.Multipart | Multipart form upload (used by Media) |
