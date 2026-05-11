package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class SupportCenterResponse(
    val phone: String,
    val email: String,
    val whatsapp: String,
    val whatsappMessageTemplate: String,
    val emailSubject: String,
    val emailBodyTemplate: String,
    val operationalStartHour: Int,
    val operationalEndHour: Int,
    val operationalTimezone: String,
    val operationalHoursLabel: String,
    val statusOnlineLabel: String,
    val statusOfflineLabel: String,
    val liveChatTitle: String,
    val liveChatStatusOnlineLabel: String,
    val sellerOnboardingTitle: String,
    val sellerOnboardingDescription: String,
    val sellerOnboardingFooter: String,
    val faq: List<SupportFaqResponse> = emptyList(),
    val bootstrapMessages: List<SupportBootstrapMessageResponse> = emptyList(),
    val sellerTerms: List<SupportSellerTermResponse> = emptyList()
)

@Serializable
data class SupportFaqResponse(
    val id: String,
    val question: String,
    val answer: String
)

@Serializable
data class SupportBootstrapMessageResponse(
    val id: String,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: String
)

@Serializable
data class SupportSellerTermResponse(
    val id: String,
    val title: String,
    val description: String
)
