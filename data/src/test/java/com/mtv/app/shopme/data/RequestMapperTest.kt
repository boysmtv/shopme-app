package com.mtv.app.shopme.data

import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.domain.param.VerifyPinParam
import org.junit.Assert.assertEquals
import org.junit.Test

class RequestMapperTest {

    @Test
    fun `verify pin request should map checkout token into trxId`() {
        val request = VerifyPinParam(
            token = "trx-session-123",
            pin = "123456"
        ).toRequest()

        assertEquals("trx-session-123", request.trxId)
        assertEquals("123456", request.pin)
    }
}
