package com.mtv.app.shopme.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.mapper.toEntity
import com.mtv.app.shopme.data.remote.datasource.CartRemoteDataSource
import com.mtv.app.shopme.data.remote.datasource.ProfileRemoteDataSource
import com.mtv.app.shopme.data.remote.datasource.SellerRemoteDataSource
import com.mtv.app.shopme.data.remote.request.CustomerUpdateRequest
import com.mtv.app.shopme.data.remote.response.SellerPaymentMethodResponse
import com.mtv.app.shopme.data.remote.response.SellerProfileResponse
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch

@Singleton
class OfflineMutationSyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val homeDao: HomeDao,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val cartRemoteDataSource: CartRemoteDataSource,
    private val sellerRemoteDataSource: SellerRemoteDataSource
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val store = PendingMutationStore(homeDao)
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    @Volatile private var started = false
    @Volatile private var syncing = false

    fun start() {
        if (started) return
        started = true
        registerNetworkCallback()
        flushAsync()
    }

    fun enqueue(action: PendingMutationAction) {
        scope.launch {
            store.enqueue(action)
            flushPending()
        }
    }

    fun flushAsync() {
        scope.launch {
            flushPending()
        }
    }

    private fun registerNetworkCallback() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager?.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    flushAsync()
                }
            }
        )
    }

    private suspend fun flushPending() {
        if (syncing || !isOnline()) return
        syncing = true
        try {
            store.list().forEach { record ->
                try {
                    when (val action = record.action) {
                        is PendingMutationAction.ProfileUpdate -> syncProfileUpdate(action.body)
                        is PendingMutationAction.CartAdd -> cartRemoteDataSource.addCart(action.body)
                        is PendingMutationAction.CartQuantity -> cartRemoteDataSource.updateQuantity(
                            cartId = action.cartId,
                            body = action.body
                        )
                        PendingMutationAction.CartClear -> cartRemoteDataSource.clearCart()
                        is PendingMutationAction.CartClearByCafe -> cartRemoteDataSource.clearCartByCafe(action.cafeId)
                        is PendingMutationAction.SellerAvailability -> syncSellerAvailability(action.body)
                        is PendingMutationAction.SellerPaymentMethods -> syncSellerPaymentMethods(action.body)
                    }
                    store.delete(record.entity.id)
                } catch (throwable: Throwable) {
                    val nextAttempt = record.entity.attemptCount + 1
                    store.markFailed(record.entity.id, nextAttempt, throwable.message)
                    if (throwable.isRetryableNetworkFailure()) {
                        return
                    }
                    store.delete(record.entity.id)
                }
            }
        } finally {
            syncing = false
        }
    }

    private suspend fun syncProfileUpdate(body: CustomerUpdateRequest) {
        profileRemoteDataSource.updateProfile(body)
        profileRemoteDataSource.getCustomer().also { customer ->
            homeDao.insertCustomer(customer.toDomain().toEntity())
        }
    }

    private suspend fun syncSellerAvailability(body: com.mtv.app.shopme.data.remote.request.SellerAvailabilityRequest) {
        sellerRemoteDataSource.updateAvailability(body).cacheSellerProfile()
    }

    private suspend fun syncSellerPaymentMethods(body: com.mtv.app.shopme.data.remote.request.SellerPaymentMethodRequest) {
        sellerRemoteDataSource.updatePaymentMethods(body).cacheSellerPaymentMethods()
    }

    private suspend fun SellerProfileResponse.cacheSellerProfile() {
        PayloadCacheStore.write(
            homeDao = homeDao,
            cacheKey = SELLER_PROFILE_CACHE_KEY,
            serializer = SellerProfileResponse.serializer(),
            value = this
        )
    }

    private suspend fun SellerPaymentMethodResponse.cacheSellerPaymentMethods() {
        PayloadCacheStore.write(
            homeDao = homeDao,
            cacheKey = SELLER_PAYMENT_CACHE_KEY,
            serializer = SellerPaymentMethodResponse.serializer(),
            value = this
        )
    }

    private fun isOnline(): Boolean {
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun Throwable.isRetryableNetworkFailure(): Boolean =
        this is IOException || this is TimeoutCancellationException

    companion object {
        private const val SELLER_PROFILE_CACHE_KEY = "seller:profile"
        private const val SELLER_PAYMENT_CACHE_KEY = "seller:payment"
    }
}
