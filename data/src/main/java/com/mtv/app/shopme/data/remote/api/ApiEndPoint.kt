package com.mtv.app.shopme.data.remote.api

import com.mtv.based.core.network.endpoint.EndpointType
import com.mtv.based.core.network.endpoint.IApiEndPoint
import com.mtv.based.core.network.utils.HttpMethod

class ApiEndPoint {

    companion object {
        const val API = "api"
        const val CART_MOVIE = "cart"
    }

    object GetHomeFood : IApiEndPoint {
        override val path = "$API/$CART_MOVIE/now_playing"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostSplashScreen : IApiEndPoint {
        override val path = "$API/splash"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object PostRegister : IApiEndPoint {
        override val path = "$API/auth/register"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object PostLogin : IApiEndPoint {
        override val path = "$API/auth/login"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object GetCustomer : IApiEndPoint {
        override val path = "$API/customer"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostVerifyPin : IApiEndPoint {
        override val path = "$API/customer/pin"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object GetCart : IApiEndPoint {
        override val path = "$API/cart"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostQuantityCart : IApiEndPoint {
        override val path = "$API/cart/quantity"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object PostInquiryCart : IApiEndPoint {
        override val path = "$API/cart/inquiry"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object PostValidateCart : IApiEndPoint {
        override val path = "$API/cart/validate"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object GetSearch : IApiEndPoint {
        override val path = "$API/foods/search"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object GetChatList : IApiEndPoint {
        override val path = "$API/chat/list"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object GetChat : IApiEndPoint {
        override val path = "$API/chat"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostChatMessage : IApiEndPoint {
        override val path = "$API/chat/message"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object PostChatMessageAllRead : IApiEndPoint {
        override val path = "$API/chat/read"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object GetAddress : IApiEndPoint {
        override val path = "$API/address"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostAddress : IApiEndPoint {
        override val path = "$API/address"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object DeleteAddress : IApiEndPoint {
        override val path = "$API/address"
        override val method = HttpMethod.Delete
        override val type = EndpointType.Json
    }

    object PatchAddress : IApiEndPoint {
        override val path = "$API/address"
        override val method = HttpMethod.Patch
        override val type = EndpointType.Json
    }

}