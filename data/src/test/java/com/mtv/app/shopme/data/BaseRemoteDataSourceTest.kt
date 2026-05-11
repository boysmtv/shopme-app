package com.mtv.app.shopme.data

import com.mtv.app.shopme.core.error.ApiException
import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.based.core.network.datasource.NetworkClientSelector
import com.mtv.based.core.network.datasource.NetworkDataSource
import com.mtv.based.core.network.endpoint.EndpointType
import com.mtv.based.core.network.endpoint.IApiEndPoint
import com.mtv.based.core.network.handler.RequestHandler
import com.mtv.based.core.network.handler.RequestHandlerResolver
import com.mtv.based.core.network.header.HeaderMerger
import com.mtv.based.core.network.model.NetworkResponse
import com.mtv.based.core.network.model.RequestOptions
import com.mtv.based.core.network.repository.NetworkRepository
import com.mtv.based.core.network.utils.HttpMethod
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BaseRemoteDataSourceTest {

    private val selector: NetworkClientSelector = mockk()
    private val headerMerger: HeaderMerger = mockk()
    private val resolver: RequestHandlerResolver = mockk()
    private val handler: RequestHandler = mockk()
    private val client: NetworkDataSource = mockk()
    private val network = NetworkRepository(selector, headerMerger, resolver)
    private val dataSource = TestRemoteDataSource(network)

    init {
        every { selector.get() } returns client
        every { headerMerger.build() } returns emptyMap()
        every { resolver.resolve(EndpointType.Json) } returns handler
    }

    @Test
    fun `request should return parsed response for success body`() = runTest {
        val expected = ApiResponse(
            timestamp = "2026-05-11T00:00:00",
            status = 200,
            code = "OK",
            message = "Success",
            traceId = "trace-1",
            data = "hello"
        )
        coEvery {
            handler.handle<ApiResponse<String>>(
                client = client,
                endpoint = any(),
                body = any(),
                headers = any(),
                serializer = any(),
                options = any()
            )
        } returns NetworkResponse(
            httpCode = 200,
            data = expected,
            rawBody = null
        )

        val actual = dataSource.execute<ApiResponse<String>>()

        assertEquals(expected, actual)
    }

    @Test
    fun `request should expose backend forbidden message and code`() = runTest {
        coEvery {
            handler.handle<ApiResponse<String>>(
                client = client,
                endpoint = any(),
                body = any(),
                headers = any(),
                serializer = any(),
                options = any()
            )
        } returns NetworkResponse(
            httpCode = 403,
            data = null,
            rawBody = """{"status":403,"code":"FORBIDDEN","message":"Access Denied","traceId":"trace-2","data":null}"""
        )

        val throwable = runCatching {
            dataSource.execute<ApiResponse<String>>()
        }.exceptionOrNull()

        assertTrue(throwable is ApiException.Forbidden)
        throwable as ApiException.Forbidden
        assertEquals(403, throwable.statusCode)
        assertEquals("FORBIDDEN", throwable.errorCode)
        assertEquals("Access Denied", throwable.message)
    }

    @Test
    fun `request should expose backend conflict message and code`() = runTest {
        coEvery {
            handler.handle<ApiResponse<String>>(
                client = client,
                endpoint = any(),
                body = any(),
                headers = any(),
                serializer = any(),
                options = any()
            )
        } returns NetworkResponse(
            httpCode = 409,
            data = null,
            rawBody = """{"status":409,"code":"CONFLICT","message":"Village is still referenced by other data","traceId":"trace-3","data":null}"""
        )

        val throwable = runCatching {
            dataSource.execute<ApiResponse<String>>()
        }.exceptionOrNull()

        assertTrue(throwable is ApiException.Conflict)
        throwable as ApiException.Conflict
        assertEquals(409, throwable.statusCode)
        assertEquals("CONFLICT", throwable.errorCode)
        assertEquals("Village is still referenced by other data", throwable.message)
    }

    private class TestRemoteDataSource(
        network: NetworkRepository
    ) : BaseRemoteDataSource(network) {

        suspend inline fun <reified R : Any> execute(): R = request(
            endpoint = DummyEndpoint,
            options = RequestOptions()
        )
    }

    private object DummyEndpoint : IApiEndPoint {
        override val path: String = "api/test"
        override val method: HttpMethod = HttpMethod.Get
        override val type: EndpointType = EndpointType.Json
    }
}
