/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.app.shopme.domain.usecase.SearchFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.SearchEffect
import com.mtv.app.shopme.feature.customer.contract.SearchEvent
import com.mtv.app.shopme.feature.customer.contract.SearchUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchFoodUseCase: SearchFoodUseCase
) : BaseEventViewModel<SearchEvent, SearchEffect>() {

    private val _state = MutableStateFlow(SearchUiState())
    val uiState = _state.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        observeSearch()
    }

    override fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.Load -> doSearch("")

            is SearchEvent.QueryChanged -> {
                _state.update { it.copy(query = event.query) }
                queryFlow.value = event.query
            }

            is SearchEvent.LoadNextPage -> loadNextPage()

            is SearchEvent.BackClicked -> {
                emitEffect(SearchEffect.NavigateBack)
            }

            is SearchEvent.ClickFood -> {
                emitEffect(SearchEffect.NavigateToDetail(event.id))
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            queryFlow
                .debounce(500)
                .distinctUntilChanged()
                .collect {
                    doSearch(it, 0)
                }
        }
    }

    private fun doSearch(query: String, page: Int = 0) {

        val param = SearchParam(
            name = query,
            page = page
        )

        observeDataFlow(
            flow = searchFoodUseCase(param),
            onState = { state ->
                when (state) {

                    is LoadState.Loading -> {
                        if (page == 0) {
                            _state.update {
                                it.copy(foods = LoadState.Loading)
                            }
                        } else {
                            _state.update {
                                it.copy(isLoadingMore = true)
                            }
                        }
                    }

                    is LoadState.Success -> {
                        val data = state.data
                        val currentList = _state.value.getDataOrEmpty()
                        val newList = if (data.page == 0) {
                            data.content
                        } else {
                            currentList + data.content
                        }

                        _state.update {
                            it.copy(
                                foods = LoadState.Success(newList),
                                page = data.page,
                                isLastPage = data.last,
                                isLoadingMore = false
                            )
                        }
                    }

                    is LoadState.Error -> {
                        _state.update {
                            it.copy(
                                foods = LoadState.Error(state.error),
                                isLoadingMore = false
                            )
                        }
                        showError(state.error)
                    }

                    else -> Unit
                }
            }
        )
    }

    private fun loadNextPage() {
        val state = _state.value

        if (state.isLastPage) return
        if (state.isLoadingMore) return

        doSearch(
            query = state.query,
            page = state.page + 1
        )
    }

    private fun SearchUiState.getDataOrEmpty(): List<SearchFood> {
        return (foods as? LoadState.Success)?.data ?: emptyList()
    }

    private fun showError(error: UiError) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = error.message
                ),
                onPrimary = {
                    dismissDialog()
                }
            )
        )
    }
}