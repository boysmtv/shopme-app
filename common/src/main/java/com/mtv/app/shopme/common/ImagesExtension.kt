/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ImagesExtension.kt
 *
 * Last modified by Dedy Wijaya on 06/02/26 09.17
 */

package com.mtv.app.shopme.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Base64
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import kotlin.apply
import kotlin.io.use
import kotlin.text.replace
import kotlin.text.substringAfter

fun uriToBase64(
    context: Context,
    uri: Uri,
    maxSize: Int = 512,
    quality: Int = 70
): String {
    val boundsOptions = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }

    context.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, boundsOptions)
    }

    var sampleSize = 1
    while (
        boundsOptions.outWidth / sampleSize > maxSize ||
        boundsOptions.outHeight / sampleSize > maxSize
    ) {
        sampleSize *= 2
    }

    val bitmapOptions = BitmapFactory.Options().apply {
        inSampleSize = sampleSize
    }

    val decodedBitmap = context.contentResolver
        .openInputStream(uri)
        ?.use {
            BitmapFactory.decodeStream(it, null, bitmapOptions)
        } ?: return ""

    val fixedBitmap = fixBitmapOrientation(context, uri, decodedBitmap)

    val output = ByteArrayOutputStream()
    fixedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)

    return Base64.encodeToString(
        output.toByteArray(),
        Base64.NO_WRAP
    )
}

fun base64ToBitmap(base64: String): Bitmap? {
    return try {
        val cleanBase64 = base64
            .substringAfter("base64,", base64)
            .replace("\n", "")

        val decoded = Base64.decode(cleanBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun fixBitmapOrientation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
    val matrix = Matrix()
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        val exif = ExifInterface(inputStream)
        when (
            exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        ) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
        }
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

