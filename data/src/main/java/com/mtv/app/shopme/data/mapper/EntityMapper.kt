/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EntityMapper.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 23.55
 */

package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.core.database.entity.AppNotificationCacheEntity
import com.mtv.app.shopme.core.database.entity.ChatListCacheEntity
import com.mtv.app.shopme.core.database.entity.CustomerEntity
import com.mtv.app.shopme.core.database.entity.FoodEntity
import com.mtv.app.shopme.domain.model.Address
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.model.MenuSummary
import com.mtv.app.shopme.domain.model.NotificationItem
import com.mtv.app.shopme.domain.model.Stats
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.domain.model.SellerNotifItem
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import java.math.BigDecimal
import kotlin.text.ifEmpty
import org.threeten.bp.LocalDateTime

/* =========================================================
 * ENTITY → DOMAIN
 * ========================================================= */

fun CustomerEntity.toDomain(): Customer = Customer(
    name = name,
    phone = phone,
    email = email,
    address = if (addressVillage.isBlank()) {
        null
    } else {
        Address(
            id = "cached-address",
            village = addressVillage,
            block = addressBlock,
            number = addressNumber,
            rt = addressRt,
            rw = addressRw,
            isDefault = true
        )
    },
    photo = photo,
    verified = verified,
    stats = Stats(
        totalOrders = totalOrders,
        activeOrders = activeOrders,
        membership = membership
    ),
    menuSummary = MenuSummary(
        ordered = ordered,
        cooking = cooking,
        shipping = shipping,
        completed = completed,
        cancelled = cancelled
    )
)

fun FoodEntity.toDomain(): Food = Food(
    id = id,
    cafeId = EMPTY_STRING,
    name = name,
    cafeName = cafeName,
    cafeAddress = EMPTY_STRING,
    description = EMPTY_STRING,
    price = BigDecimal.valueOf(price),
    category = FoodCategory.FOOD,
    status = FoodStatus.UNKNOWN,
    quantity = 0,
    estimate = EMPTY_STRING,
    isActive = isActive,
    createdAt = LocalDateTime.parse("1970-01-01T00:00:00"),
    images = listOf(image),
    variants = emptyList()
)

/* =========================================================
 * DOMAIN → ENTITY
 * ========================================================= */

fun Customer.toEntity(): CustomerEntity = CustomerEntity(
    id = email.ifEmpty { "self" },
    name = name,
    phone = phone,
    email = email,
    addressVillage = address?.village.orEmpty(),
    addressBlock = address?.block.orEmpty(),
    addressNumber = address?.number.orEmpty(),
    addressRt = address?.rt.orEmpty(),
    addressRw = address?.rw.orEmpty(),
    photo = photo,
    verified = verified,
    totalOrders = stats?.totalOrders ?: 0,
    activeOrders = stats?.activeOrders ?: 0,
    membership = stats?.membership.orEmpty(),
    ordered = menuSummary?.ordered ?: 0,
    cooking = menuSummary?.cooking ?: 0,
    shipping = menuSummary?.shipping ?: 0,
    completed = menuSummary?.completed ?: 0,
    cancelled = menuSummary?.cancelled ?: 0,
    updatedAt = System.currentTimeMillis()
)

fun Food.toEntity(): FoodEntity = FoodEntity(
    id = id,
    name = name,
    price = price.toDouble(),
    image = images.firstOrNull().orEmpty(),
    cafeName = cafeName,
    isActive = isActive
)

fun ChatListItem.toEntity(scope: String): ChatListCacheEntity = ChatListCacheEntity(
    cacheKey = "$scope:$id",
    scope = scope,
    conversationId = id,
    name = name,
    lastMessage = lastMessage,
    time = time,
    unreadCount = unreadCount,
    avatarUrl = avatarUrl,
    updatedAt = System.currentTimeMillis()
)

fun ChatListCacheEntity.toDomain(): ChatListItem = ChatListItem(
    id = conversationId,
    name = name,
    lastMessage = lastMessage,
    time = time,
    unreadCount = unreadCount,
    avatarUrl = avatarUrl
)

fun NotificationItem.toEntity(scope: String, notificationId: String): AppNotificationCacheEntity = AppNotificationCacheEntity(
    cacheKey = "$scope:$notificationId",
    scope = scope,
    notificationId = notificationId,
    title = title,
    message = message,
    createdAt = "${signatureDate}T${signatureTime}",
    isRead = isRead,
    updatedAt = System.currentTimeMillis()
)

fun SellerNotifItem.toEntity(scope: String): AppNotificationCacheEntity = AppNotificationCacheEntity(
    cacheKey = "$scope:$orderId",
    scope = scope,
    notificationId = orderId,
    title = title,
    message = message,
    createdAt = "${date}T${time}",
    isRead = isRead,
    updatedAt = System.currentTimeMillis()
)

fun AppNotificationCacheEntity.toCustomerNotification(): NotificationItem = NotificationItem(
    title = title,
    message = message,
    photo = "",
    signatureName = title,
    signatureDate = createdAt.substringBefore("T"),
    signatureTime = createdAt.substringAfter("T", "").substringBefore("."),
    isRead = isRead
)

fun AppNotificationCacheEntity.toSellerNotification(): SellerNotifItem = SellerNotifItem(
    title = title,
    message = message,
    orderId = notificationId,
    buyerName = title,
    date = createdAt.substringBefore("T"),
    time = createdAt.substringAfter("T", "").substringBefore("."),
    isRead = isRead
)
