package com.mtv.app.shopme.common

import android.util.Log
import com.mtv.app.shopme.common.BuildConfig

object AppLogger {

    private const val DEFAULT_TAG = "Shopme"

    fun d(message: String, tag: String = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun d(throwable: Throwable, message: String? = null, tag: String = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message.orEmpty(), throwable)
        }
    }

    fun i(message: String, tag: String = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    fun w(message: String, tag: String = DEFAULT_TAG) {
        Log.w(tag, message)
    }

    fun w(throwable: Throwable, message: String? = null, tag: String = DEFAULT_TAG) {
        Log.w(tag, message.orEmpty(), throwable)
    }

    fun e(message: String, tag: String = DEFAULT_TAG) {
        Log.e(tag, message)
    }

    fun e(throwable: Throwable, message: String? = null, tag: String = DEFAULT_TAG) {
        Log.e(tag, message.orEmpty(), throwable)
    }
}
