package com.mtv.app.shopme.data.remote.api

import com.mtv.based.core.network.endpoint.EndpointType
import com.mtv.based.core.network.endpoint.IApiEndPoint
import com.mtv.based.core.network.utils.HttpMethod

class ApiEndPoint {

    companion object {
        const val API = "api"
        const val AUTH = "auth"
        const val ADDRESS = "address"
        const val FOODS = "foods"
        const val CART = "cart"
        const val CHAT = "chat"
        const val CUSTOMER = "customer"
    }

    object GetHomeFood : IApiEndPoint {
        override val path = "$API/$FOODS"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostSplashScreen : IApiEndPoint {
        override val path = "$API/splash"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object PostRegister : IApiEndPoint {
        override val path = "$API/$AUTH/register"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object PostLogin : IApiEndPoint {
        override val path = "$API/$AUTH/login"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object GetCustomer : IApiEndPoint {
        override val path = "$API/$CUSTOMER"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostVerifyPin : IApiEndPoint {
        override val path = "$API/$CUSTOMER/pin"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object GetCart : IApiEndPoint {
        override val path = "$API/$CART"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostQuantityCart : IApiEndPoint {
        override val path = "$API/$CART/quantity"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object PostInquiryCart : IApiEndPoint {
        override val path = "$API/$CART/inquiry"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object PostValidateCart : IApiEndPoint {
        override val path = "$API/$CART/validate"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object GetSearch : IApiEndPoint {
        override val path = "$API/$FOODS/search"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object GetChatList : IApiEndPoint {
        override val path = "$API/$CHAT/list"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object GetChat : IApiEndPoint {
        override val path = "$API/$CHAT"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostChatMessage : IApiEndPoint {
        override val path = "$API/$CHAT/message"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object PostChatMessageAllRead : IApiEndPoint {
        override val path = "$API/$CHAT/read"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object GetAddress : IApiEndPoint {
        override val path = "$API/$ADDRESS"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostAddress : IApiEndPoint {
        override val path = "$API/$ADDRESS"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

    object DeleteAddress : IApiEndPoint {
        override val path = "$API/$ADDRESS"
        override val method = HttpMethod.Delete
        override val type = EndpointType.Json
    }

    object PatchAddress : IApiEndPoint {
        override val path = "$API/$ADDRESS"
        override val method = HttpMethod.Patch
        override val type = EndpointType.Json
    }

    class FoodsDetail(foodId: String) : IApiEndPoint {
        override val path = "$API/$FOODS/$foodId"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    class FoodsSimilar(cafeId: String) : IApiEndPoint {
        override val path = "$API/$FOODS/cafe/$cafeId"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

}