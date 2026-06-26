package com.mtv.app.shopme.core.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mtv.app.shopme.core.database.entity.CustomerEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {
    private companion object {
        const val TEST_DB = "migration-test"
        const val SCHEMA_DIR = "core/schemas"
    }

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        listOf(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun validateV9Schema() {
        helper.createDatabase(TEST_DB, 9).close()
    }

    @Test
    @Throws(IOException::class)
    fun validateV9WithData() {
        val db = helper.createDatabase(TEST_DB, 9)
        db.execSQL("INSERT INTO customer (id, name, phone, email, photo, addressVillage, addressBlock, addressNumber, addressRt, addressRw, verified, totalOrders, activeOrders, membership, ordered, cooking, shipping, completed, cancelled, updatedAt) VALUES ('test1', 'Test User', '08123456789', 'test@shopme.local', '', '', '', '', '', '', 0, 0, 0, '', 0, 0, 0, 0, 0, 0)")
        val cursor = db.query("SELECT * FROM customer WHERE id = 'test1'")
        cursor.use {
            assertEquals(1, it.count)
            it.moveToFirst()
            assertEquals("Test User", it.getString(it.getColumnIndexOrThrow("name")))
        }
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun migrationPathNotTestable() {
        // Migration paths 1→9, 2→9, etc. cannot be tested because database schemas
        // for versions 1-8 are not committed in the repository.
        // Only version 9 schema validation is performed in validateV9Schema().
        //
        // LIMITATION: Without schema exports for versions 1-8, Room MigrationTestHelper
        // cannot simulate upgrading from those versions. The CREATE TABLE IF NOT EXISTS
        // safety net added to migrations 7→9 and 8→9 ensures idempotent schema creation
        // for customer/food/cart/cart_variant tables, but the actual 1→9→2→9→3→9→4→9→5→9→6→9
        // paths remain untestable without exporting the historical schemas.
    }
}
