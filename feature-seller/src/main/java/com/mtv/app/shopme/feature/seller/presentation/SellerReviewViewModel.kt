package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.ReviewReplyParam
import com.mtv.app.shopme.domain.usecase.GetSellerReviewsUseCase
import com.mtv.app.shopme.domain.usecase.ReplyToReviewUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerReviewEffect
import com.mtv.app.shopme.feature.seller.contract.SellerReviewEvent
import com.mtv.app.shopme.feature.seller.contract.SellerReviewUiState
import com.mtv.based.core.network.utils.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SellerReviewViewModel @Inject constructor(
    private val getSellerReviewsUseCase: GetSellerReviewsUseCase,
    private val replyToReviewUseCase: ReplyToReviewUseCase
) : BaseEventViewModel<SellerReviewEvent, SellerReviewEffect>() {

    private val _state = MutableStateFlow(SellerReviewUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerReviewEvent) {
        when (event) {
            SellerReviewEvent.Load -> loadReviews()
            is SellerReviewEvent.ClickReply -> _state.update { it.copy(replyingToReviewId = event.reviewId, replyText = "") }
            SellerReviewEvent.ClickCancelReply -> _state.update { it.copy(replyingToReviewId = null, replyText = "") }
            is SellerReviewEvent.ChangeReplyText -> _state.update { it.copy(replyText = event.value) }
            is SellerReviewEvent.SubmitReply -> submitReply(event.reviewId)
        }
    }

    private fun loadReviews() {
        observeIndependentDataFlow(
            flow = getSellerReviewsUseCase(),
            onState = { result ->
                _state.update {
                    it.copy(
                        reviews = if (result is LoadState.Success) result.data else it.reviews,
                        isLoading = result is LoadState.Loading && it.reviews.isEmpty(),
                        errorMessage = null
                    )
                }
            },
            onError = { _state.update { it.copy(isLoading = false, errorMessage = it.message) } }
        )
    }

    private fun submitReply(reviewId: String) {
        val text = _state.value.replyText
        if (text.isBlank()) return

        _state.update { it.copy(isSubmittingReply = true) }

        observeDataFlow(
            flow = replyToReviewUseCase(reviewId, ReviewReplyParam(reply = text)),
            onState = { result ->
                if (result is LoadState.Success) {
                    _state.update { it.copy(replyingToReviewId = null, replyText = "", isSubmittingReply = false) }
                    loadReviews()
                }
                _state.update { it.copy(isSubmittingReply = result is LoadState.Loading) }
            },
            onError = { _state.update { it.copy(isSubmittingReply = false) } }
        )
    }
}
