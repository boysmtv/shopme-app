/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DomainMapper.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 23.51
 */

package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.data.remote.response.ChatListItemResponse
import com.mtv.app.shopme.data.remote.response.ChatItem
import com.mtv.app.shopme.data.remote.response.ChatResponse
import com.mtv.app.shopme.data.remote.response.AddressResponse
import com.mtv.app.shopme.data.remote.response.AppConfigResponse
import com.mtv.app.shopme.data.remote.response.AppNotificationResponse
import com.mtv.app.shopme.data.remote.response.CafeAddressResponse
import com.mtv.app.shopme.data.remote.response.CafeResponse
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.data.remote.response.ChatListResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.FoodOptionResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.FoodVariantResponse
import com.mtv.app.shopme.data.remote.response.LoginResponse
import com.mtv.app.shopme.data.remote.response.MenuSummaryResponse
import com.mtv.app.shopme.data.remote.response.NotificationPreferencesResponse
import com.mtv.app.shopme.data.remote.response.SessionTokenResponse
import com.mtv.app.shopme.data.remote.response.SplashResponse
import com.mtv.app.shopme.data.remote.response.StatsResponse
import com.mtv.app.shopme.data.remote.response.VillageResponse
import com.mtv.app.shopme.domain.model.Address
import com.mtv.app.shopme.domain.model.AppConfig
import com.mtv.app.shopme.domain.model.Cafe
import com.mtv.app.shopme.domain.model.CafeAddress
import com.mtv.app.shopme.domain.model.Cart
import com.mtv.app.shopme.domain.model.CartVariant
import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodOption
import com.mtv.app.shopme.domain.model.FoodVariant
import com.mtv.app.shopme.domain.model.Login
import com.mtv.app.shopme.domain.model.MenuSummary
import com.mtv.app.shopme.domain.model.NotificationItem
import com.mtv.app.shopme.domain.model.NotificationPreferences
import com.mtv.app.shopme.domain.model.Register
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.model.SellerNotifItem
import com.mtv.app.shopme.domain.model.SessionToken
import com.mtv.app.shopme.domain.model.Splash
import com.mtv.app.shopme.domain.model.Stats
import com.mtv.app.shopme.domain.model.User
import com.mtv.app.shopme.data.remote.response.OrderItemResponse
import com.mtv.app.shopme.data.remote.response.OrderResponse
import com.mtv.app.shopme.data.remote.response.SellerOrderSummaryResponse
import com.mtv.app.shopme.data.remote.response.SellerPaymentMethodResponse
import com.mtv.app.shopme.data.remote.response.SellerProfileResponse
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderItem
import com.mtv.app.shopme.domain.model.SellerOrderItem
import com.mtv.app.shopme.domain.model.SellerPaymentMethod
import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.model.Village

/* =========================================================
 * RESPONSE → DOMAIN
 * ========================================================= */

fun OrderResponse.toDomain(): Order = Order(
    id = id,
    customerId = customerId,
    customerName = customerName.orEmpty(),
    cafeId = cafeId,
    cafeName = cafeName.orEmpty(),
    items = items.map { it.toDomain() },
    totalPrice = totalPrice.toDouble(),
    status = status,
    paymentStatus = paymentStatus,
    paymentMethod = paymentMethod,
    createdAt = createdAt.orEmpty(),
    deliveryAddress = deliveryAddress.orEmpty()
)

fun OrderItemResponse.toDomain(): OrderItem = OrderItem(
    id = id,
    foodId = foodId,
    foodName = foodName.orEmpty(),
    quantity = quantity,
    price = price.toDouble(),
    notes = notes,
    status = status
)

fun CafeResponse.toDomain(): Cafe = Cafe(
    id = id,
    customerId = customerId,
    name = name,
    phone = phone,
    description = description,
    minimalOrder = minimalOrder,
    openTime = openTime,
    closeTime = closeTime,
    image = image,
    isActive = isActive,
    createdAt = createdAt,
    address = address.toDomain()
)

fun CafeAddressResponse.toDomain(): CafeAddress = CafeAddress(
    id = id,
    name = name,
    block = block,
    number = number,
    rt = rt,
    rw = rw
)

fun CartItemResponse.toDomain(): Cart = Cart(
    id = id,
    customerId = customerId,
    foodId = foodId,
    cafeId = cafeId,
    cafeName = cafeName,
    name = name,
    image = image,
    price = price,
    quantity = quantity,
    notes = notes,
    variants = variants.map {
        CartVariant(
            variantId = it.variantId,
            variantName = it.variantName,
            optionId = it.optionId,
            optionName = it.optionName,
            price = it.price
        )
    }
)

fun CustomerResponse.toDomain(): Customer = Customer(
    name = name.orEmpty(),
    phone = phone.orEmpty(),
    email = email.orEmpty(),
    address = address?.toDomain(),
    photo = photo.orEmpty(),
    verified = verified ?: false,
    stats = stats?.toDomain(),
    menuSummary = menuSummary?.toDomain()
)

fun AddressResponse.toDomain(): Address = Address(
    id = id,
    village = village.orEmpty(),
    block = block,
    number = number,
    rt = rt,
    rw = rw,
    isDefault = isDefault
)

fun StatsResponse.toDomain(): Stats = Stats(
    totalOrders = totalOrders,
    activeOrders = activeOrders,
    membership = membership
)

fun MenuSummaryResponse.toDomain(): MenuSummary = MenuSummary(
    ordered = ordered,
    cooking = cooking,
    shipping = shipping,
    completed = completed,
    cancelled = cancelled
)

fun NotificationPreferencesResponse.toDomain(): NotificationPreferences = NotificationPreferences(
    orderNotification = orderNotification,
    promoNotification = promoNotification,
    chatNotification = chatNotification,
    pushEnabled = pushEnabled,
    emailEnabled = emailEnabled
)

fun FoodResponse.toDomain(): Food = Food(
    id = id,
    cafeId = cafeId,
    name = name,
    cafeName = cafeName.orEmpty(),
    cafeAddress = cafeAddress.orEmpty(),
    description = description,
    price = price,
    category = category,
    status = status,
    quantity = quantity,
    estimate = estimate,
    isActive = isActive,
    createdAt = createdAt,
    images = images,
    variants = variants.map { it.toDomain() }
)

fun FoodVariantResponse.toDomain(): FoodVariant = FoodVariant(
    id = id,
    name = name,
    options = options.map { it.toDomain() }
)

fun FoodOptionResponse.toDomain(): FoodOption = FoodOption(
    id = id,
    name = name,
    price = price
)

fun SessionTokenResponse.toDomain(): SessionToken =
    SessionToken(token = token)

fun FoodResponse.toSearchDomain(): SearchFood = SearchFood(
    id = id,
    name = name,
    price = price,
    image = images.firstOrNull().orEmpty(),
    cafeName = cafeName.orEmpty()
)

fun ChatListResponse.toDomain(): ChatList {
    return ChatList(
        chatList = chatList.map { it.toDomain() }
    )
}

fun ChatListItemResponse.toDomain(): ChatListItem {
    return ChatListItem(
        id = id,
        name = name,
        lastMessage = lastMessage,
        time = time,
        unreadCount = unreadCount,
        avatarBase64 = avatarBase64
    )
}

fun ChatResponse.toDomain(): List<ChatListItem> {
    return chatList.map { it.toDomain() }
}

fun ChatItem.toDomain(): ChatListItem {
    return ChatListItem(
        id = id,
        name = "",
        lastMessage = message,
        time = "",
        unreadCount = 0,
        avatarBase64 = null,
        isFromUser = isFromUser
    )
}

fun SellerProfileResponse.toDomain(): SellerProfile = SellerProfile(
    cafeId = cafeId,
    sellerName = sellerName,
    sellerPhoto = sellerPhoto,
    email = email,
    phone = phone,
    storeName = storeName,
    storePhoto = storePhoto,
    storeAddress = storeAddress,
    isOnline = isOnline,
    hasCafe = hasCafe
)

fun SellerPaymentMethodResponse.toDomain(): SellerPaymentMethod = SellerPaymentMethod(
    cashEnabled = cashEnabled,
    bankEnabled = bankEnabled,
    bankNumber = bankNumber.orEmpty(),
    ovoEnabled = ovoEnabled,
    ovoNumber = ovoNumber.orEmpty(),
    danaEnabled = danaEnabled,
    danaNumber = danaNumber.orEmpty(),
    gopayEnabled = gopayEnabled,
    gopayNumber = gopayNumber.orEmpty()
)

fun SellerOrderSummaryResponse.toDomain(): SellerOrderItem = SellerOrderItem(
    id = orderId,
    invoice = orderId,
    customer = customerName,
    total = total,
    date = date,
    time = time,
    paymentMethod = paymentMethod,
    status = status,
    location = location
)

fun VillageResponse.toDomain(): Village {
    return Village(
        id = id,
        name = name,
    )
}

fun SplashResponse.toDomain() = Splash(
    isAuthenticated = isAuthenticated,
    versionStatus = versionStatus,
    user = user?.let {
        User(
            id = it.id,
            name = it.name.orEmpty(),
            email = it.email.orEmpty(),
            phone = it.phone.orEmpty(),
            photo = it.photo.orEmpty()
        )
    },
    config = (config ?: AppConfigResponse()).let {
        AppConfig(
            minVersion = it.minVersion,
            latestVersion = it.latestVersion,
            forceUpdate = it.forceUpdate,
            maintenanceMode = it.maintenanceMode,
            maintenanceMessage = it.maintenanceMessage
        )
    }
)

fun LoginResponse.toDomain() = Login(
    accessToken = accessToken
)

fun Unit.toDomain() = Register(success = true)


fun AppNotificationResponse.toCustomerNotification() = NotificationItem(
    title = title,
    message = message,
    photo = "",
    signatureName = title,
    signatureDate = createdAt.substringBefore("T"),
    signatureTime = createdAt.substringAfter("T", "").substringBefore("."),
    isRead = isRead
)

fun AppNotificationResponse.toSellerNotification() = SellerNotifItem(
    title = title,
    message = message,
    orderId = id,
    buyerName = title,
    date = createdAt.substringBefore("T"),
    time = createdAt.substringAfter("T", "").substringBefore("."),
    isRead = isRead
)
