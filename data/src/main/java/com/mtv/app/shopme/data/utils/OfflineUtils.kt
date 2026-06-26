package com.mtv.app.shopme.data.utils

import java.io.IOException
import kotlinx.coroutines.TimeoutCancellationException

fun Throwable.isRetryableOfflineWrite(): Boolean =
    this is IOException || this is TimeoutCancellationException
