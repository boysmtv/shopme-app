package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.feature.customer.contract.OrderFilter
import com.mtv.app.shopme.feature.customer.contract.filterOrders
import com.mtv.app.shopme.feature.customer.contract.resolveOrderFilter
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderScreenLogicTest {

    @Test
    fun `resolve order filter should fall back to semua for unknown input`() {
        assertEquals(OrderFilter.SEMUA, resolveOrderFilter(""))
        assertEquals(OrderFilter.COMPLETED, resolveOrderFilter("completed"))
        assertEquals(OrderFilter.ORDERED, resolveOrderFilter("ORDERED"))
    }

    @Test
    fun `filter orders should keep completed and ordered buckets separate`() {
        val orders = listOf(
            order("1", OrderStatus.UNPAID),
            order("2", OrderStatus.ORDERED),
            order("3", OrderStatus.COOKING),
            order("4", OrderStatus.DELIVERING),
            order("5", OrderStatus.COMPLETED)
        )

        assertEquals(listOf("1", "2"), filterOrders(orders, OrderFilter.ORDERED).map { it.id })
        assertEquals(listOf("3"), filterOrders(orders, OrderFilter.COOKING).map { it.id })
        assertEquals(listOf("4"), filterOrders(orders, OrderFilter.DELIVERING).map { it.id })
        assertEquals(listOf("5"), filterOrders(orders, OrderFilter.COMPLETED).map { it.id })
        assertEquals(listOf("1", "2", "3", "4", "5"), filterOrders(orders, OrderFilter.SEMUA).map { it.id })
    }

    private fun order(id: String, status: OrderStatus) = Order(
        id = id,
        status = status
    )
}
