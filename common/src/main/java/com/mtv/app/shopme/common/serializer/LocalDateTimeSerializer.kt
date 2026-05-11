/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LocalDateTimeSerializer.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 23.26
 */

package com.mtv.app.shopme.common.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val rawValue = decoder.decodeString()

        return runCatching {
            LocalDateTime.parse(rawValue)
        }.recoverCatching {
            OffsetDateTime.parse(rawValue).toLocalDateTime()
        }.recoverCatching {
            Instant.parse(rawValue).atOffset(ZoneOffset.UTC).toLocalDateTime()
        }.getOrThrow()
    }
}
