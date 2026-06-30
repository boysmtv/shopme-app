package com.mtv.app.shopme.feature.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mtv.app.shopme.common.ConstantPreferences.ACCESS_TOKEN
import com.mtv.based.core.provider.utils.SecurePrefs
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

@Serializable
data class FirestoreChatMessage(
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val senderRole: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val read: Boolean = false
)

@Serializable
data class PendingMessage(
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val senderRole: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "pending"
)

sealed class FirestoreSyncEvent {
    data class MessageReceived(val message: FirestoreChatMessage) : FirestoreSyncEvent()
    data class MessageSent(val message: FirestoreChatMessage) : FirestoreSyncEvent()
    data class OfflineQueued(val message: PendingMessage) : FirestoreSyncEvent()
    data class Error(val throwable: Throwable, val context: String) : FirestoreSyncEvent()
}

@Singleton
class FirestoreChatSync @Inject constructor(
    private val securePrefs: SecurePrefs,
    @ApplicationContext private val context: Context
) {
    private val firestore = FirebaseFirestore.getInstance()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val json = Json { ignoreUnknownKeys = true }

    private val _isOnline = MutableStateFlow(true)
    val isOnline = _isOnline.asStateFlow()

    var enableFirestoreFallback: Boolean = true

    private val activeListeners = mutableMapOf<String, com.google.firebase.firestore.ListenerRegistration>()

    fun listenToConversation(conversationId: String): Flow<FirestoreSyncEvent> = callbackFlow {
        val registration = firestore
            .collection("chats")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(FirestoreSyncEvent.Error(error, "listen:$conversationId"))
                    return@addSnapshotListener
                }
                snapshot?.documentChanges?.forEach { change ->
                    when (change.type) {
                        com.google.firebase.firestore.DocumentChange.Type.ADDED -> {
                            val msg = change.document.toObject(FirestoreChatMessage::class.java)
                            trySend(FirestoreSyncEvent.MessageReceived(msg))
                        }
                        else -> {}
                    }
                }
            }

        activeListeners[conversationId] = registration

        awaitClose {
            registration.remove()
            activeListeners.remove(conversationId)
        }
    }

    suspend fun syncMessage(message: FirestoreChatMessage): Result<Unit> = runCatching {
        firestore
            .collection("chats")
            .document(message.conversationId)
            .collection("messages")
            .document(message.id)
            .set(message)
            .await()
    }

    suspend fun queueOfflineMessage(message: PendingMessage): Result<Unit> = runCatching {
        firestore
            .collection("chats")
            .document(message.conversationId)
            .collection("pending")
            .document(message.id)
            .set(message)
            .await()
    }

    suspend fun processOfflineQueue(conversationId: String): List<PendingMessage> = runCatching {
        val snapshot = firestore
            .collection("chats")
            .document(conversationId)
            .collection("pending")
            .whereEqualTo("status", "pending")
            .get()
            .await()

        val messages = snapshot.documents.mapNotNull { it.toObject(PendingMessage::class.java) }

        for (msg in messages) {
            firestore
                .collection("chats")
                .document(conversationId)
                .collection("pending")
                .document(msg.id)
                .update("status", "synced")
                .await()
        }

        messages
    }.getOrDefault(emptyList())

    fun removeListener(conversationId: String) {
        activeListeners[conversationId]?.remove()
        activeListeners.remove(conversationId)
    }

    fun removeAllListeners() {
        activeListeners.values.forEach { it.remove() }
        activeListeners.clear()
    }

    fun setOnlineStatus(online: Boolean) {
        _isOnline.value = online
    }
}
