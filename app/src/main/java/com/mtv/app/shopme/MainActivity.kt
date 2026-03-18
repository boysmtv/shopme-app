/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: MainActivity.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupFcmToken()
        setContent {
            AppNavigation(sessionManager)
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