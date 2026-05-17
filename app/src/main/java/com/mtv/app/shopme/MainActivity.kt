/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: MainActivity.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import com.mtv.app.shopme.common.notification.NotificationDeepLink
import com.mtv.app.shopme.common.notification.NotificationDeepLinkExtras
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.app.shopme.feature.firebase.NotificationRepository
import com.mtv.app.shopme.nav.AppNavigation
import com.mtv.based.core.provider.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    private val pendingNotificationDeepLink = mutableStateOf<NotificationDeepLink?>(null)

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            Log.d("LOG_BOYS_FCM", "Notification permission granted=$granted")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pendingNotificationDeepLink.value = intent.toNotificationDeepLink()
        enableEdgeToEdge()
        requestNotificationPermissionIfNeeded()
        setupFcmToken()
        setContent {
            AppNavigation(
                sessionManager = sessionManager,
                notificationDeepLink = pendingNotificationDeepLink.value,
                onNotificationDeepLinkConsumed = { pendingNotificationDeepLink.value = null }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingNotificationDeepLink.value = intent.toNotificationDeepLink()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun setupFcmToken(){
        val repository by lazy { NotificationRepository(SecurePrefs(applicationContext)) }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                repository.saveStatus(task.result.isEmpty())
                repository.saveToken(task.result)
                Log.e("LOG_BOYS_FCM", "Token: ${task.result}")
            } else {
                repository.saveStatus(false)
                Log.e("LOG_BOYS_FCM", "Token failed", task.exception)
            }
        }

        Firebase.messaging.subscribeToTopic("news")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LOG_BOYS_FCM", "Subscribed to news")
                } else {
                    Log.e("LOG_BOYS_FCM", "Failed Subscribed to news")
                }
            }
    }
}

private fun Intent?.toNotificationDeepLink(): NotificationDeepLink? {
    if (this == null) return null
    val values = buildMap {
        getStringExtra(NotificationDeepLinkExtras.TYPE)?.let { put(NotificationDeepLinkExtras.TYPE, it) }
        getStringExtra(NotificationDeepLinkExtras.ORDER_ID)?.let { put(NotificationDeepLinkExtras.ORDER_ID, it) }
        getStringExtra(NotificationDeepLinkExtras.CONVERSATION_ID)?.let { put(NotificationDeepLinkExtras.CONVERSATION_ID, it) }
        getStringExtra(NotificationDeepLinkExtras.ROLE)?.let { put(NotificationDeepLinkExtras.ROLE, it) }
        getStringExtra("type")?.let { put("type", it) }
        getStringExtra("orderId")?.let { put("orderId", it) }
        getStringExtra("conversationId")?.let { put("conversationId", it) }
        getStringExtra("chatId")?.let { put("chatId", it) }
        getStringExtra("role")?.let { put("role", it) }
        getStringExtra("targetRole")?.let { put("targetRole", it) }
        getStringExtra("userRole")?.let { put("userRole", it) }
    }
    return NotificationDeepLink.from(values)
}
