package com.mtv.app.shopme.data

import com.mtv.app.shopme.data.fake.fakeCustomerResponse
import com.mtv.app.shopme.data.local.home.HomeLocalDataSource
import com.mtv.app.shopme.data.remote.datasource.HomeRemoteDataSource
import com.mtv.app.shopme.data.repository.HomeRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HomeRepositoryImplTest {

    private val local: HomeLocalDataSource = mockk(relaxed = true)
    private val remote: HomeRemoteDataSource = mockk()

    private val repo = HomeRepositoryImpl(local, remote)

    @Test
    fun `should fetch and save`() = runTest {
        coEvery { remote.getCustomer() } returns fakeCustomerResponse()

        repo.getCustomer().collect {}

        coVerify { local.saveCustomer(any()) }
    }
}