package com.mtv.app.shopme.data.remote.api

import com.mtv.based.core.network.endpoint.EndpointType
import com.mtv.based.core.network.endpoint.IApiEndPoint
import com.mtv.based.core.network.utils.HttpMethod

object ApiEndPoint {

    private const val API = "api"
    private const val AUTH = "auth"
    private const val ADDRESS = "address"
    private const val VILLAGE = "village"
    private const val FOODS = "foods"
    private const val CART = "cart"
    private const val CAFE = "cafe"
    private const val CHAT = "chat"
    private const val CUSTOMER = "customer"
    private const val ORDER = "order"

    object Auth {

        object Register : IApiEndPoint {
            override val path = "$API/$AUTH/register"
            override val method = HttpMethod.Post
            override val type = EndpointType.Json
        }

        object Login : IApiEndPoint {
            override val path = "$API/$AUTH/login"
            override val method = HttpMethod.Post
            override val type = EndpointType.Json
        }
    }

    object Customer {

        object Get : IApiEndPoint {
            override val path = "$API/$CUSTOMER"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        object Update : IApiEndPoint {
            override val path = "$API/$CUSTOMER"
            override val method = HttpMethod.Put
            override val type = EndpointType.Json
        }

        object VerifyPin : IApiEndPoint {
            override val path = "$API/$CUSTOMER/pin"
            override val method = HttpMethod.Post
            override val type = EndpointType.Json
        }
    }

    object Foods {

        object GetAll : IApiEndPoint {
            override val path = "$API/$FOODS"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        object Search : IApiEndPoint {
            override val path = "$API/$FOODS/search"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        class Detail(foodId: String) : IApiEndPoint {
            override val path = "$API/$FOODS/$foodId"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        class GetByCafeId(cafeId: String) : IApiEndPoint {
            override val path = "$API/$FOODS/$CAFE/$cafeId"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        class GetSimilarByCafe(cafeId: String) : IApiEndPoint {
            override val path = "$API/$FOODS/$CAFE/$cafeId"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }
    }

    object Cart {

        object Get : IApiEndPoint {
            override val path = "$API/$CART"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        object Add : IApiEndPoint {
            override val path = "$API/$CART"
            override val method = HttpMethod.Post
            override val type = EndpointType.Json
        }

        class Quantity(cartId: String) : IApiEndPoint {
            override val path = "$API/$CART/$cartId"
            override val method = HttpMethod.Put
            override val type = EndpointType.Json
        }

        object Clear : IApiEndPoint {
            override val path = "$API/$CART/clear"
            override val method = HttpMethod.Delete
            override val type = EndpointType.Json
        }

        class DeleteByCafeId(cafeId: String) : IApiEndPoint {
            override val path = "$API/$CART/$CAFE/$cafeId"
            override val method = HttpMethod.Delete
            override val type = EndpointType.Json
        }
    }

    object Order {

        object GetList : IApiEndPoint {
            override val path = "$API/$ORDER"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        class Detail(orderId: String) : IApiEndPoint {
            override val path = "$API/$ORDER/$orderId"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        object GetSession : IApiEndPoint {
            override val path = "$API/$ORDER/session"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        object Create : IApiEndPoint {
            override val path = "$API/$ORDER"
            override val method = HttpMethod.Post
            override val type = EndpointType.Json
        }
    }

    object Cafe {

        class Detail(cafeId: String) : IApiEndPoint {
            override val path = "$API/$CAFE/$cafeId"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }
    }

    object Chat {

        object GetList : IApiEndPoint {
            override val path = "$API/$CHAT/list"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        object Get : IApiEndPoint {
            override val path = "$API/$CHAT"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        object SendMessage : IApiEndPoint {
            override val path = "$API/$CHAT/message"
            override val method = HttpMethod.Post
            override val type = EndpointType.Json
        }

        object MarkAllRead : IApiEndPoint {
            override val path = "$API/$CHAT/read"
            override val method = HttpMethod.Post
            override val type = EndpointType.Json
        }
    }

    object Address {

        object Get : IApiEndPoint {
            override val path = "$API/$ADDRESS"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }

        object Create : IApiEndPoint {
            override val path = "$API/$ADDRESS"
            override val method = HttpMethod.Post
            override val type = EndpointType.Json
        }

        class Delete(id: String) : IApiEndPoint {
            override val path = "$API/$ADDRESS/$id"
            override val method = HttpMethod.Delete
            override val type = EndpointType.Json
        }

        class SetDefault(id: String) : IApiEndPoint {
            override val path = "$API/$ADDRESS/$id/default"
            override val method = HttpMethod.Patch
            override val type = EndpointType.Json
        }
    }

    object Village {

        object Get : IApiEndPoint {
            override val path = "$API/$VILLAGE"
            override val method = HttpMethod.Get
            override val type = EndpointType.Json
        }
    }

    object Misc {

        object Splash : IApiEndPoint {
            override val path = "$API/splash"
            override val method = HttpMethod.Post
            override val type = EndpointType.Json
        }
    }
}