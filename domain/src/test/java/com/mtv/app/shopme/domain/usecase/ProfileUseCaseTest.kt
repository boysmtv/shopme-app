package com.mtv.app.shopme.domain.usecase

import app.cash.turbine.test
import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.app.shopme.domain.repository.ProfileRepository
import com.mtv.app.shopme.domain.model.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileUseCaseTest {

    private val repository: ProfileRepository = mockk()
    private lateinit var getCustomerUseCase: GetCustomerUseCase
    private lateinit var updateCustomerUseCase: UpdateCustomerUseCase
    private lateinit var addFavoriteFoodUseCase: AddFavoriteFoodUseCase
    private lateinit var removeFavoriteFoodUseCase: RemoveFavoriteFoodUseCase
    private lateinit var getFavoriteFoodsUseCase: GetFavoriteFoodsUseCase
    private lateinit var getFavoriteFoodIdsUseCase: GetFavoriteFoodIdsUseCase
    private lateinit var deleteAccountUseCase: DeleteAccountUseCase

    @Before
    fun setUp() {
        getCustomerUseCase = GetCustomerUseCase(repository)
        updateCustomerUseCase = UpdateCustomerUseCase(repository)
        addFavoriteFoodUseCase = AddFavoriteFoodUseCase(repository)
        removeFavoriteFoodUseCase = RemoveFavoriteFoodUseCase(repository)
        getFavoriteFoodsUseCase = GetFavoriteFoodsUseCase(repository)
        getFavoriteFoodIdsUseCase = GetFavoriteFoodIdsUseCase(repository)
        deleteAccountUseCase = DeleteAccountUseCase(repository)
    }

    @Test
    fun `GetCustomerUseCase delegates to repository getCustomer`() = runTest {
        coEvery { repository.getCustomer(false) } returns flowOf(Resource.Loading)

        getCustomerUseCase().test {
            assertEquals(Resource.Loading, awaitItem())
            awaitComplete()
        }

        coVerify { repository.getCustomer(false) }
    }

    @Test
    fun `GetCustomerUseCase passes forceRefresh param`() = runTest {
        coEvery { repository.getCustomer(true) } returns flowOf(Resource.Loading)

        getCustomerUseCase(forceRefresh = true).test {
            assertEquals(Resource.Loading, awaitItem())
            awaitComplete()
        }

        coVerify { repository.getCustomer(true) }
    }

    @Test
    fun `UpdateCustomerUseCase delegates to repository updateProfile`() = runTest {
        val param = CustomerUpdateParam(name = "Budi", phone = "081234", photo = null, fcmToken = null)
        coEvery { repository.updateProfile(param) } returns flowOf(Resource.Success(Unit))

        updateCustomerUseCase(param).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.updateProfile(param) }
    }

    @Test
    fun `AddFavoriteFoodUseCase delegates to repository addFavoriteFood`() = runTest {
        coEvery { repository.addFavoriteFood("food-1") } returns flowOf(Resource.Success(Unit))

        addFavoriteFoodUseCase("food-1").test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.addFavoriteFood("food-1") }
    }

    @Test
    fun `RemoveFavoriteFoodUseCase delegates to repository removeFavoriteFood`() = runTest {
        coEvery { repository.removeFavoriteFood("food-1") } returns flowOf(Resource.Success(Unit))

        removeFavoriteFoodUseCase("food-1").test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.removeFavoriteFood("food-1") }
    }

    @Test
    fun `GetFavoriteFoodsUseCase delegates to repository getFavoriteFoods`() = runTest {
        coEvery { repository.getFavoriteFoods() } returns flowOf(Resource.Loading)

        getFavoriteFoodsUseCase().test {
            assertEquals(Resource.Loading, awaitItem())
            awaitComplete()
        }

        coVerify { repository.getFavoriteFoods() }
    }

    @Test
    fun `GetFavoriteFoodIdsUseCase delegates to repository getFavoriteFoodIds`() = runTest {
        coEvery { repository.getFavoriteFoodIds() } returns flowOf(Resource.Loading)

        getFavoriteFoodIdsUseCase().test {
            assertEquals(Resource.Loading, awaitItem())
            awaitComplete()
        }

        coVerify { repository.getFavoriteFoodIds() }
    }

    @Test
    fun `DeleteAccountUseCase delegates to repository deleteAccount`() = runTest {
        coEvery { repository.deleteAccount() } returns flowOf(Resource.Success(Unit))

        deleteAccountUseCase().test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.deleteAccount() }
    }
}
