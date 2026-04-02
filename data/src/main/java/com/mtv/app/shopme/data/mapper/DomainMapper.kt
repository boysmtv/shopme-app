/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DomainMapper.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 23.51
 */

package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.data.remote.response.ChatListItemResponse
import com.mtv.app.shopme.data.remote.response.AddressResponse
import com.mtv.app.shopme.data.remote.response.CafeAddressResponse
import com.mtv.app.shopme.data.remote.response.CafeResponse
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.data.remote.response.ChatListResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.FoodOptionResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.FoodVariantResponse
import com.mtv.app.shopme.data.remote.response.MenuSummaryResponse
import com.mtv.app.shopme.data.remote.response.SessionTokenResponse
import com.mtv.app.shopme.data.remote.response.StatsResponse
import com.mtv.app.shopme.data.remote.response.VillageResponse
import com.mtv.app.shopme.domain.model.Address
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
import com.mtv.app.shopme.domain.model.MenuSummary
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.model.SessionToken
import com.mtv.app.shopme.domain.model.Stats
import com.mtv.app.shopme.domain.model.Village

/* =========================================================
 * RESPONSE → DOMAIN
 * ========================================================= */

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
    village = village,
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

fun FoodResponse.toDomain(): Food = Food(
    id = id,
    cafeId = cafeId,
    name = name,
    cafeName = cafeName,
    cafeAddress = cafeAddress,
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
    cafeName = cafeName
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

fun VillageResponse.toDomain(): Village {
    return Village(
        id = id,
        name = name,
    )
}