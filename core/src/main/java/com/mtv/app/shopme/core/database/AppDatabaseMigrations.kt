package com.mtv.app.shopme.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseMigrations {

    val ALL = arrayOf(
        migrationTo9(1),
        migrationTo9(2),
        migrationTo9(3),
        migrationTo9(4),
        migrationTo9(5),
        migrationTo9(6),
        migration7To9(),
        migration8To9()
    )

    private fun migrationTo9(fromVersion: Int) = object : Migration(fromVersion, 9) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS `chat_list_cache`")
            db.execSQL("DROP TABLE IF EXISTS `chat_message_cache`")
            db.execSQL("DROP TABLE IF EXISTS `app_notification_cache`")
            db.execSQL("DROP TABLE IF EXISTS `payload_cache`")
            db.execSQL("DROP TABLE IF EXISTS `pending_mutation`")
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
            createChatMessageCache(db)
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

    private fun migration7To9() = object : Migration(7, 9) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS `chat_list_cache`")
            db.execSQL("DROP TABLE IF EXISTS `chat_message_cache`")
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
            createChatMessageCache(db)
        }
    }

    private fun migration8To9() = object : Migration(8, 9) {
        override fun migrate(db: SupportSQLiteDatabase) {
            createChatMessageCache(db)
        }
    }

    private fun createChatMessageCache(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `chat_message_cache` (
                `cacheKey` TEXT NOT NULL,
                `scope` TEXT NOT NULL,
                `conversationId` TEXT NOT NULL,
                `messageId` TEXT NOT NULL,
                `message` TEXT NOT NULL,
                `time` TEXT NOT NULL,
                `isFromUser` INTEGER NOT NULL,
                `isRead` INTEGER NOT NULL,
                `position` INTEGER NOT NULL,
                `updatedAt` INTEGER NOT NULL,
                PRIMARY KEY(`cacheKey`)
            )
            """.trimIndent()
        )
    }
}
