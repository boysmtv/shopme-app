package com.mtv.app.shopme.data.remote.api

import com.mtv.app.shopme.data.remote.request.SplashRequest
import com.mtv.based.core.network.endpoint.EndpointType
import com.mtv.based.core.network.endpoint.IApiEndPoint
import com.mtv.based.core.network.utils.HttpMethod

class ApiEndPoint {

    companion object {
        const val API_VERSION = 1
        const val CART_MOVIE = "cart"
    }

    object GetHomeFood : IApiEndPoint {
        override val path = "$API_VERSION/$CART_MOVIE/now_playing"
        override val method = HttpMethod.Get
        override val type = EndpointType.Json
    }

    object PostSplashScreen : IApiEndPoint {
        override val path = "$API_VERSION/$CART_MOVIE/popular"
        override val method = HttpMethod.Post
        override val type = EndpointType.Json
    }

}