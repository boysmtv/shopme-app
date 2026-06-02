package com.mtv.app.shopme.domain.usecase

import app.cash.turbine.test
import com.mtv.app.shopme.domain.model.Cart
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.param.CartAddParam
import com.mtv.app.shopme.domain.param.CartClearByCafeParam
import com.mtv.app.shopme.domain.param.CartQuantityParam
import com.mtv.app.shopme.domain.param.CreateOrderParam
import com.mtv.app.shopme.domain.repository.CartRepository
import com.mtv.based.core.network.utils.Resource
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
class CartUseCaseTest {

    private val repository: CartRepository = mockk()
    private lateinit var getCartUseCase: GetCartUseCase
    private lateinit var createFoodToCartUseCase: CreateFoodToCartUseCase
    private lateinit var updateCartQuantityUseCase: UpdateCartQuantityUseCase
    private lateinit var deleteCartItemUseCase: DeleteCartItemUseCase
    private lateinit var deleteCartUseCase: DeleteCartUseCase
    private lateinit var deleteCartByCafeIdUseCase: DeleteCartByCafeIdUseCase
    private lateinit var createOrderUseCase: CreateOrderUseCase

    @Before
    fun setUp() {
        getCartUseCase = GetCartUseCase(repository)
        createFoodToCartUseCase = CreateFoodToCartUseCase(repository)
        updateCartQuantityUseCase = UpdateCartQuantityUseCase(repository)
        deleteCartItemUseCase = DeleteCartItemUseCase(repository)
        deleteCartUseCase = DeleteCartUseCase(repository)
        deleteCartByCafeIdUseCase = DeleteCartByCafeIdUseCase(repository)
        createOrderUseCase = CreateOrderUseCase(repository)
    }

    @Test
    fun `GetCartUseCase delegates to repository getCart`() = runTest {
        coEvery { repository.getCart() } returns flowOf(Resource.Success(emptyList()))

        getCartUseCase().test {
            assertEquals(Resource.Success(emptyList<Cart>()), awaitItem())
            awaitComplete()
        }

        coVerify { repository.getCart() }
    }

    @Test
    fun `CreateFoodToCartUseCase delegates to repository addCart`() = runTest {
        val param = CartAddParam(foodId = "food-1", variants = null, quantity = 1, note = "")
        coEvery { repository.addCart(param) } returns flowOf(Resource.Success(Unit))

        createFoodToCartUseCase(param).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.addCart(param) }
    }

    @Test
    fun `UpdateCartQuantityUseCase delegates to repository updateQuantity`() = runTest {
        val param = CartQuantityParam(cartId = "cart-1", quantity = 3)
        coEvery { repository.updateQuantity(param) } returns flowOf(Resource.Success(Unit))

        updateCartQuantityUseCase(param).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.updateQuantity(param) }
    }

    @Test
    fun `DeleteCartItemUseCase delegates to repository deleteCartItem`() = runTest {
        val cartId = "cart-1"
        coEvery { repository.deleteCartItem(cartId) } returns flowOf(Resource.Success(Unit))

        deleteCartItemUseCase(cartId).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.deleteCartItem(cartId) }
    }

    @Test
    fun `DeleteCartUseCase delegates to repository clearCart`() = runTest {
        coEvery { repository.clearCart() } returns flowOf(Resource.Success(Unit))

        deleteCartUseCase().test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.clearCart() }
    }

    @Test
    fun `DeleteCartByCafeIdUseCase delegates to repository clearCartByCafe`() = runTest {
        val param = CartClearByCafeParam(cafeId = "cafe-1")
        coEvery { repository.clearCartByCafe(param) } returns flowOf(Resource.Success(Unit))

        deleteCartByCafeIdUseCase(param).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.clearCartByCafe(param) }
    }

    @Test
    fun `CreateOrderUseCase delegates to repository createOrder`() = runTest {
        val param = CreateOrderParam(
            cartId = listOf("cart-1"),
            payment = PaymentMethod.TRANSFER,
            token = "tok_abc"
        )
        coEvery { repository.createOrder(param) } returns flowOf(Resource.Success(Unit))

        createOrderUseCase(param).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.createOrder(param) }
    }
}
