package com.mtv.app.shopme.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseMigrations {

    val ALL = arrayOf(
        migrationTo7(1),
        migrationTo7(2),
        migrationTo7(3),
        migrationTo7(4),
        migrationTo7(5),
        migrationTo7(6)
    )

    private fun migrationTo7(fromVersion: Int) = object : Migration(fromVersion, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `chat_list_cache` (
                    `cacheKey` TEXT NOT NULL,
                    `scope` TEXT NOT NULL,
                    `conversationId` TEXT NOT NULL,
                    `name` TEXT NOT NULL,
                    `lastMessage` TEXT NOT NULL,
                    `time` TEXT NOT NULL,
                    `unreadCount` INTEGER NOT NULL,
                    `avatarBase64` TEXT,
                    `updatedAt` INTEGER NOT NULL,
                    PRIMARY KEY(`cacheKey`)
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `app_notification_cache` (
                    `cacheKey` TEXT NOT NULL,
                    `scope` TEXT NOT NULL,
                    `notificationId` TEXT NOT NULL,
                    `title` TEXT NOT NULL,
                    `message` TEXT NOT NULL,
                    `createdAt` TEXT NOT NULL,
                    `isRead` INTEGER NOT NULL,
                    `updatedAt` INTEGER NOT NULL,
                    PRIMARY KEY(`cacheKey`)
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `payload_cache` (
                    `cacheKey` TEXT NOT NULL,
                    `payload` TEXT NOT NULL,
                    `updatedAt` INTEGER NOT NULL,
                    PRIMARY KEY(`cacheKey`)
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `pending_mutation` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `actionType` TEXT NOT NULL,
                    `payload` TEXT NOT NULL,
                    `createdAt` INTEGER NOT NULL,
                    `updatedAt` INTEGER NOT NULL,
                    `attemptCount` INTEGER NOT NULL,
                    `lastError` TEXT
                )
                """.trimIndent()
            )
        }
    }
}
