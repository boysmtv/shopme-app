package com.mtv.app.shopme.data.realtime

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.mtv.app.shopme.common.BuildConfig
import com.mtv.app.shopme.common.ConstantPreferences.ACCESS_TOKEN
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEvent
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.based.core.provider.utils.SecurePrefs
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

@Singleton
class ShopmeRealtimeGatewayImpl @Inject constructor(
    private val securePrefs: SecurePrefs,
    @ApplicationContext context: Context
) : ShopmeRealtimeGateway {

    private val _events = MutableSharedFlow<ShopmeRealtimeEvent>(extraBufferCapacity = 64)
    override val events = _events.asSharedFlow()
    private val client = OkHttpClient.Builder()
        .addInterceptor(
            ChuckerInterceptor.Builder(context)
                .redactHeaders("Authorization", "Cookie")
                .build()
        )
        .build()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val lock = Any()

    @Volatile
    private var activeSocket: WebSocket? = null

    @Volatile
    private var activeToken: String? = null

    @Volatile
    private var isConnecting: Boolean = false

    @Volatile
    private var reconnectScheduled: Boolean = false

    override fun ensureConnected() {
        val token = securePrefs.getString(ACCESS_TOKEN).orEmpty().trim()

        if (token.isBlank()) {
            closeActiveSocket()
            return
        }

        synchronized(lock) {
            if (activeSocket != null && activeToken == token) {
                return
            }
            if (isConnecting && activeToken == token) {
                return
            }

            closeActiveSocket()
            activeToken = token
            isConnecting = true
        }

        val request = Request.Builder()
            .url(buildRealtimeUrl(BuildConfig.BASE_URL, token))
            .build()

        val socket = client.newWebSocket(request, listenerFor(token))
        synchronized(lock) {
            activeSocket = socket
        }
    }

    private fun listenerFor(token: String) = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            synchronized(lock) {
                isConnecting = false
                reconnectScheduled = false
                activeSocket = webSocket
                activeToken = token
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            parseRealtimeEvent(text)?.let { event ->
                _events.tryEmit(event)
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            handleSocketClosed(token)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            handleSocketClosed(token)
        }
    }

    private fun handleSocketClosed(token: String) {
        synchronized(lock) {
            isConnecting = false
            if (activeToken == token) {
                activeSocket = null
            }
            if (reconnectScheduled || activeToken != token) {
                return
            }
            reconnectScheduled = true
        }

        scope.launch {
            delay(2_000)
            synchronized(lock) {
                reconnectScheduled = false
            }
            ensureConnected()
        }
    }

    private fun closeActiveSocket() {
        synchronized(lock) {
            activeSocket?.close(1000, "reset")
            activeSocket = null
            isConnecting = false
        }
    }
}

@Serializable
private data class RealtimeEventPayloadDto(
    val type: String? = null,
    val conversationId: String? = null,
    val customerId: String? = null,
    val cafeId: String? = null,
    val notificationId: String? = null,
    val title: String? = null,
    val message: String? = null,
    val actorId: String? = null,
    val onlineCustomerId: String? = null,
    val onlineCafeId: String? = null,
    val online: Boolean? = null,
    val lastSeenAt: String? = null,
    val occurredAt: String? = null
)

internal fun parseRealtimeEvent(raw: String): ShopmeRealtimeEvent? = runCatching {
    RealtimeJson.format
        .decodeFromString<RealtimeEventPayloadDto>(raw)
        .toDomain()
}.getOrNull()

internal fun buildRealtimeUrl(baseUrl: String, token: String): String {
    val normalizedBase = baseUrl.removeSuffix("/")
    val websocketBase = when {
        normalizedBase.startsWith("https://") -> "wss://${normalizedBase.removePrefix("https://")}"
        normalizedBase.startsWith("http://") -> "ws://${normalizedBase.removePrefix("http://")}"
        normalizedBase.startsWith("wss://") || normalizedBase.startsWith("ws://") -> normalizedBase
        else -> "ws://$normalizedBase"
    }

    return "$websocketBase/ws/realtime?token=${URLEncoder.encode(token, Charsets.UTF_8.name())}"
}

private fun RealtimeEventPayloadDto.toDomain() = ShopmeRealtimeEvent(
    type = ShopmeRealtimeEventType.from(type),
    conversationId = conversationId,
    customerId = customerId,
    cafeId = cafeId,
    notificationId = notificationId,
    title = title,
    message = message,
    actorId = actorId,
    onlineCustomerId = onlineCustomerId,
    onlineCafeId = onlineCafeId,
    online = online,
    lastSeenAt = lastSeenAt,
    occurredAt = occurredAt
)

private object RealtimeJson {
    val format = Json { ignoreUnknownKeys = true }
}
