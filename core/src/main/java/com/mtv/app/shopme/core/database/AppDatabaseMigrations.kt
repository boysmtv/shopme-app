package com.mtv.app.shopme.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseMigrations {

    val ALL = arrayOf(
        migrationTo8(1),
        migrationTo8(2),
        migrationTo8(3),
        migrationTo8(4),
        migrationTo8(5),
        migrationTo8(6),
        migration7To8()
    )

    private fun migrationTo8(fromVersion: Int) = object : Migration(fromVersion, 8) {
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
                    `avatarUrl` TEXT,
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

    private fun migration7To8() = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS `chat_list_cache`")
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
                    `avatarUrl` TEXT,
                    `updatedAt` INTEGER NOT NULL,
                    PRIMARY KEY(`cacheKey`)
                )
                """.trimIndent()
            )
        }
    }
}
