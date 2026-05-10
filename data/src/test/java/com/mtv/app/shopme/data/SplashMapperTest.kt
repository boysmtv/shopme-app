package com.mtv.app.shopme.data

import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.response.SplashResponse
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Test

class SplashMapperTest {

    @Test
    fun `toDomain should tolerate missing config from backend`() {
        val response = SplashResponse(
            isAuthenticated = false,
            user = null,
            config = null,
            versionStatus = "OK"
        )

        val domain = response.toDomain()

        assertEquals("OK", domain.versionStatus)
        assertEquals(0, domain.config.minVersion)
        assertFalse(domain.config.forceUpdate)
        assertFalse(domain.config.maintenanceMode)
    }
}
