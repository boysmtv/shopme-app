package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CartInquiryResponse(
    var cartId: String,
)