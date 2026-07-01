package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.ReviewReplyParam
import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class ReplyToReviewUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(reviewId: String, param: ReviewReplyParam) = repository.replyToReview(reviewId, param)
}
