package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.model.SellerDashboard
import com.mtv.app.shopme.domain.model.Review
import com.mtv.app.shopme.domain.model.SellerCategory
import com.mtv.app.shopme.domain.model.SellerDiscount
import com.mtv.app.shopme.domain.model.SellerPaymentMethod
import com.mtv.app.shopme.domain.model.SellerOrderItem
import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.param.DiscountParam
import com.mtv.app.shopme.domain.param.ReviewReplyParam
import com.mtv.app.shopme.domain.param.SellerCategoryParam
import com.mtv.app.shopme.domain.param.SellerPaymentMethodParam
import com.mtv.app.shopme.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface SellerRepository {
    fun getDashboard(): Flow<Resource<SellerDashboard>>
    fun getProfile(): Flow<Resource<SellerProfile>>
    fun getPaymentMethods(): Flow<Resource<SellerPaymentMethod>>
    fun getOrders(): Flow<Resource<List<SellerOrderItem>>>
    fun getOrders(page: Int, size: Int, status: OrderStatus? = null): Flow<Resource<PagedData<SellerOrderItem>>>
    fun getOrderDetail(orderId: String): Flow<Resource<Order>>
    fun updateOrderStatus(orderId: String, status: OrderStatus): Flow<Resource<Unit>>
    fun cancelOrder(orderId: String, reason: String?): Flow<Resource<Unit>>
    fun updateAvailability(isOnline: Boolean): Flow<Resource<SellerProfile>>
    fun updatePaymentMethods(param: SellerPaymentMethodParam): Flow<Resource<SellerPaymentMethod>>
    fun getDiscounts(): Flow<Resource<List<SellerDiscount>>>
    fun createDiscount(param: DiscountParam): Flow<Resource<SellerDiscount>>
    fun updateDiscount(discountId: String, param: DiscountParam): Flow<Resource<SellerDiscount>>
    fun deleteDiscount(discountId: String): Flow<Resource<Unit>>
    fun getReviews(): Flow<Resource<List<Review>>>
    fun replyToReview(reviewId: String, param: ReviewReplyParam): Flow<Resource<Review>>
    fun getCategories(): Flow<Resource<List<SellerCategory>>>
    fun createCategory(param: SellerCategoryParam): Flow<Resource<SellerCategory>>
    fun updateCategory(categoryId: String, param: SellerCategoryParam): Flow<Resource<SellerCategory>>
    fun deleteCategory(categoryId: String): Flow<Resource<Unit>>
}
