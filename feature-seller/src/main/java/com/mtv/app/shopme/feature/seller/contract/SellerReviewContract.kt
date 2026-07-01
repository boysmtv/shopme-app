package com.mtv.app.shopme.feature.seller.contract

import androidx.compose.runtime.Immutable
import com.mtv.app.shopme.domain.model.Review

@Immutable
data class SellerReviewUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val reviews: List<Review> = emptyList(),
    val replyingToReviewId: String? = null,
    val replyText: String = "",
    val isSubmittingReply: Boolean = false
)

sealed class SellerReviewEvent {
    object Load : SellerReviewEvent()
    data class ClickReply(val reviewId: String) : SellerReviewEvent()
    object ClickCancelReply : SellerReviewEvent()
    data class ChangeReplyText(val value: String) : SellerReviewEvent()
    data class SubmitReply(val reviewId: String) : SellerReviewEvent()
}

sealed class SellerReviewEffect {
    object NavigateBack : SellerReviewEffect()
}
