package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.ContentErrorState
import com.mtv.app.shopme.common.SmartImage
import com.mtv.app.shopme.domain.model.Review
import com.mtv.app.shopme.feature.seller.contract.SellerReviewEvent
import com.mtv.app.shopme.feature.seller.contract.SellerReviewUiState

@Composable
fun SellerReviewScreen(
    state: SellerReviewUiState,
    event: (SellerReviewEvent) -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Customer Reviews",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColor.Blue)
                }
            } else if (!state.errorMessage.isNullOrBlank() && state.reviews.isEmpty()) {
                ContentErrorState(
                    title = "Gagal memuat review",
                    message = state.errorMessage,
                    actionLabel = "Muat ulang",
                    onRetry = { event(SellerReviewEvent.Load) }
                )
            } else if (state.reviews.isEmpty()) {
                EmptyReviewState()
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.reviews, key = { it.id }) { review ->
                        ReviewCard(
                            review = review,
                            isReplying = state.replyingToReviewId == review.id,
                            replyText = state.replyText,
                            onReplyClick = { event(SellerReviewEvent.ClickReply(review.id)) },
                            onCancelReply = { event(SellerReviewEvent.ClickCancelReply) },
                            onChangeReplyText = { event(SellerReviewEvent.ChangeReplyText(it)) },
                            onSubmitReply = { event(SellerReviewEvent.SubmitReply(review.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewCard(
    review: Review,
    isReplying: Boolean,
    replyText: String,
    onReplyClick: () -> Unit,
    onCancelReply: () -> Unit,
    onChangeReplyText: (String) -> Unit,
    onSubmitReply: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AppColor.Blue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = AppColor.Blue,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.customerName ?: "Customer",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = review.foodName ?: "",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row {
                    repeat(5) { i ->
                        Icon(
                            if (i < review.rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = Color(0xFFFFA000),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            if (!review.comment.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = review.comment,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }

            if (!review.reply.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColor.Blue.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "Your reply:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColor.Blue
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = review.reply,
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            if (review.reply.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))

                if (isReplying) {
                    OutlinedTextField(
                        value = replyText,
                        onValueChange = onChangeReplyText,
                        placeholder = { Text("Write your reply...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = onCancelReply) {
                            Text("Cancel", fontSize = 13.sp)
                        }
                        TextButton(onClick = onSubmitReply) {
                            Text("Reply", fontSize = 13.sp, color = AppColor.Blue)
                        }
                    }
                } else {
                    OutlinedButton(onClick = onReplyClick) {
                        Text("Reply", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyReviewState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = "No Reviews Yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Reviews from customers will\nappear here.",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}
