/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.SessionManager
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    private val queryFlow = MutableStateFlow("")

    init {
        observeSearch()
        doSearch("", 0)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            queryFlow
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    currentPage = 0
                    isLastPage = false
                    doSearch(query, 0)
                }
        }
    }

    fun onQueryChanged(query: String) {
        uiData.update { it.copy(query = query) }
        queryFlow.value = query
    }

    fun doSearch(name: String, page: Int = 0, showLoading: Boolean = true) {

        if (isLoading) return
        isLoading = true

        if (page == 0) {
            currentPage = 0
            isLastPage = false
        }

        uiData.update { it.copy(query = name) }

        launchUseCase(
            loading = showLoading,
            target = uiState.valueFlowOf(
                get = { it.searchFoodState },
                set = { state -> copy(searchFoodState = state) }
            ),
            block = {
                searchFoodUseCase(
                    SearchFoodRequest(
                        name = name,
                        page = page,
                        size = 10,
                        sort = "name,asc"
                    )
                )
            },
            onSuccess = { response ->

                val pageData = response.data ?: return@launchUseCase

                currentPage = pageData.page
                isLastPage = pageData.last

                uiData.update { current ->
                    val newList = if (page == 0) {
                        pageData.content
                    } else {
                        current.results + pageData.content
                    }
                    current.copy(results = newList)
                }
            },
            onComplete = {
                isLoading = false
            }
        )
    }

    fun loadNextPage() {
        if (isLastPage) return
        if (isLoading) return

        doSearch(
            name = uiData.value.query,
            page = currentPage + 1,
            showLoading = false
        )
    }

}