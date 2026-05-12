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
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import kotlin.apply
import kotlin.io.use

fun uriToCompressedJpegFile(
    context: Context,
    uri: Uri,
    maxSize: Int = 1280,
    quality: Int = 82
): File? {
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
        } ?: return null

    val fixedBitmap = fixBitmapOrientation(context, uri, decodedBitmap)
    val tempFile = File.createTempFile("shopme-media-", ".jpg", context.cacheDir)

    FileOutputStream(tempFile).use { output ->
        fixedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
    }

    return tempFile
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
