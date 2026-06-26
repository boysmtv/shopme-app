package com.mtv.app.shopme.nav.customer

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.mtv.app.shopme.common.navbar.auth.AuthDestinations
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavItem
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations
import com.mtv.app.shopme.common.navbar.seller.SellerDestinations
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class CustomerNavActionsTest {

    private val nav: NavController = mockk(relaxed = true)

    @Test
    fun `toLogin navigates to LOGIN_GRAPH`() {
        val route = slot<String>()
        val opts = slot<NavOptionsBuilder.() -> Unit>()

        CustomerNavActions.toLogin(nav)

        verify { nav.navigate(capture(route), capture(opts)) }
        assertEquals(AuthDestinations.LOGIN_GRAPH, route.captured)
    }

    @Test
    fun `toHome navigates to HOME_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toHome(nav)

        verify { nav.navigate(capture(route), any<NavOptionsBuilder.() -> Unit>()) }
        assertEquals(CustomerDestinations.HOME_GRAPH, route.captured)
    }

    @Test
    fun `toRegister navigates to REGISTER_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toRegister(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(AuthDestinations.REGISTER_GRAPH, route.captured)
    }

    @Test
    fun `toForgetPassword navigates to RESET_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toForgetPassword(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(AuthDestinations.RESET_GRAPH, route.captured)
    }

    @Test
    fun `toSearch without query navigates to Search bottom nav route`() {
        val route = slot<String>()

        CustomerNavActions.toSearch(nav)

        verify { nav.navigate(capture(route), any<NavOptionsBuilder.() -> Unit>()) }
        assertEquals(CustomerBottomNavItem.Search.route, route.captured)
    }

    @Test
    fun `toSearch with query navigates to SEARCH slash query`() {
        val route = slot<String>()

        CustomerNavActions.toSearch(nav, "kopi")

        verify { nav.navigate(capture(route), any<NavOptionsBuilder.() -> Unit>()) }
        assertEquals("SEARCH/kopi", route.captured)
    }

    @Test
    fun `toSearch with blank query navigates to Search bottom nav route`() {
        val route = slot<String>()

        CustomerNavActions.toSearch(nav, "   ")

        verify { nav.navigate(capture(route), any<NavOptionsBuilder.() -> Unit>()) }
        assertEquals(CustomerBottomNavItem.Search.route, route.captured)
    }

    @Test
    fun `toCart navigates to CART route`() {
        val route = slot<String>()

        CustomerNavActions.toCart(nav)

        verify { nav.navigate(capture(route), any<NavOptionsBuilder.() -> Unit>()) }
        assertEquals(CustomerBottomNavItem.Cart.route, route.captured)
    }

    @Test
    fun `toSeller navigates to SELLER_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toSeller(nav)

        verify { nav.navigate(capture(route), any<NavOptionsBuilder.() -> Unit>()) }
        assertEquals(SellerDestinations.SELLER_GRAPH, route.captured)
    }

    @Test
    fun `toNotif navigates to NOTIF_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toNotif(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(CustomerDestinations.NOTIF_GRAPH, route.captured)
    }

    @Test
    fun `toOrder without filter navigates to ORDER_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toOrder(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(CustomerDestinations.ORDER_GRAPH, route.captured)
    }

    @Test
    fun `toOrder with filter navigates to ORDER_GRAPH slash filter`() {
        val route = slot<String>()

        CustomerNavActions.toOrder(nav, "DELIVERING")

        verify { nav.navigate(capture(route)) }
        assertEquals("ORDER_GRAPH/DELIVERING", route.captured)
    }

    @Test
    fun `toOrder with blank filter navigates to ORDER_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toOrder(nav, "   ")

        verify { nav.navigate(capture(route)) }
        assertEquals(CustomerDestinations.ORDER_GRAPH, route.captured)
    }

    @Test
    fun `toChat without id navigates to CHAT_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toChat(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(CustomerDestinations.CHAT_GRAPH, route.captured)
    }

    @Test
    fun `toChat with id navigates to CHAT_GRAPH slash id`() {
        val route = slot<String>()

        CustomerNavActions.toChat(nav, "chat-1")

        verify { nav.navigate(capture(route)) }
        assertEquals("CHAT_GRAPH/chat-1", route.captured)
    }

    @Test
    fun `toChat with null id navigates to CHAT_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toChat(nav, null)

        verify { nav.navigate(capture(route)) }
        assertEquals(CustomerDestinations.CHAT_GRAPH, route.captured)
    }

    @Test
    fun `toEditProfile navigates to EDIT_PROFILE_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toEditProfile(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(CustomerDestinations.EDIT_PROFILE_GRAPH, route.captured)
    }

    @Test
    fun `toOrderHistory navigates to ORDER_HISTORY_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toOrderHistory(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(CustomerDestinations.ORDER_HISTORY_GRAPH, route.captured)
    }

    @Test
    fun `toFavorites navigates to FAVORITES_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toFavorites(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(CustomerDestinations.FAVORITES_GRAPH, route.captured)
    }

    @Test
    fun `toSettings navigates to SETTINGS_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toSettings(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(CustomerDestinations.SETTINGS_GRAPH, route.captured)
    }

    @Test
    fun `toHelpCenter navigates to HELP_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toHelpCenter(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(CustomerDestinations.HELP_GRAPH, route.captured)
    }

    @Test
    fun `toTnc navigates to SELLER_CREATE_TNC_GRAPH`() {
        val route = slot<String>()

        CustomerNavActions.toTnc(nav)

        verify { nav.navigate(capture(route)) }
        assertEquals(SellerDestinations.SELLER_CREATE_TNC_GRAPH, route.captured)
    }

    @Test
    fun `toDetail navigates to DETAIL_GRAPH_WITH_ID slash foodId`() {
        val route = slot<String>()

        CustomerNavActions.toDetail(nav, "food-1")

        verify { nav.navigate(capture(route)) }
        assertEquals("DETAIL_GRAPH_WITH_ID/food-1", route.captured)
    }

    @Test
    fun `toCafe navigates to CAFE_GRAPH_WITH_ID slash cafeId`() {
        val route = slot<String>()

        CustomerNavActions.toCafe(nav, "cafe-1")

        verify { nav.navigate(capture(route)) }
        assertEquals("CAFE_GRAPH_WITH_ID/cafe-1", route.captured)
    }
}
