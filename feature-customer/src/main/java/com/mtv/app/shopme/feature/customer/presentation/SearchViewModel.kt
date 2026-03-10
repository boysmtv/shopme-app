/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.remote.request.CartQuantityRequest
import com.mtv.app.shopme.data.remote.request.SearchFoodRequest
import com.mtv.app.shopme.domain.usecase.CartItemUseCase
import com.mtv.app.shopme.domain.usecase.SearchFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.SearchDataListener
import com.mtv.app.shopme.feature.customer.contract.SearchStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.update

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchFoodUseCase: SearchFoodUseCase,
) : BaseViewModel(), UiOwner<SearchStateListener, SearchDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(SearchStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(SearchDataListener())

    private var currentPage = 0
    private var isLastPage = false
    private var isLoading = false

    fun doSearch(name: String, page: Int = 0) {

        if (isLoading) return
        isLoading = true

        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.searchFoodState },
                set = { state -> copy(searchFoodState = state) }
            ),
            block = {
                searchFoodUseCase(
                    SearchFoodRequest(
                        name = name,
                        page = page,
                        size = 10
                    )
                )
            },
            onSuccess = { response ->
                response.data?.let {
                    currentPage = it.page
                    isLastPage = it.last
                }
            },
            onComplete = {
                isLoading = false
            }
        )
    }

    fun loadNextPage() {
        if (isLastPage) return
        doSearch(
            name = uiData.value.query,
            page = currentPage + 1
        )
    }

}