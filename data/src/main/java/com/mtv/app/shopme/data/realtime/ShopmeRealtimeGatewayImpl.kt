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

    @Volatile
    private var retainCount: Int = 0

    override fun retain() {
        synchronized(lock) {
            retainCount += 1
        }
        ensureConnected()
    }

    override fun release() {
        val shouldClose = synchronized(lock) {
            retainCount = (retainCount - 1).coerceAtLeast(0)
            retainCount == 0
        }
        if (shouldClose) {
            closeActiveSocket("inactive")
        }
    }

    override fun ensureConnected() {
        val token = securePrefs.getString(ACCESS_TOKEN).orEmpty().trim()

        if (token.isBlank()) {
            closeActiveSocket("missing-token")
            return
        }

        synchronized(lock) {
            if (activeSocket != null && activeToken == token) {
                return
            }
            if (isConnecting && activeToken == token) {
                return
            }

            closeActiveSocket("reset")
            activeToken = token
            isConnecting = true
        }

        val request = Request.Builder()
            .url(buildRealtimeUrl(BuildConfig.BASE_URL))
            .addHeader("Authorization", "Bearer $token")
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
            if (retainCount <= 0 || reconnectScheduled || activeToken != token) {
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
        closeActiveSocket("reset")
    }

    private fun closeActiveSocket(reason: String) {
        synchronized(lock) {
            activeSocket?.close(1000, reason)
            activeSocket = null
            isConnecting = false
            reconnectScheduled = false
            if (reason != "reset") {
                activeToken = null
            }
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

internal fun buildRealtimeUrl(baseUrl: String): String {
    val normalizedBase = baseUrl.removeSuffix("/")
    val websocketBase = when {
        normalizedBase.startsWith("https://") -> "wss://${normalizedBase.removePrefix("https://")}"
        normalizedBase.startsWith("http://") -> "ws://${normalizedBase.removePrefix("http://")}"
        normalizedBase.startsWith("wss://") || normalizedBase.startsWith("ws://") -> normalizedBase
        else -> "ws://$normalizedBase"
    }

    return "$websocketBase/ws/realtime"
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
