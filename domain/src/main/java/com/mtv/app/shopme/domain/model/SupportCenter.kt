package com.mtv.app.shopme.domain.model

data class SupportCenter(
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
    val faq: List<SupportFaq>,
    val bootstrapMessages: List<SupportBootstrapMessage>,
    val sellerTerms: List<SupportSellerTerm>
)

data class SupportFaq(
    val id: String,
    val question: String,
    val answer: String
)

data class SupportBootstrapMessage(
    val id: String,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: String
)

data class SupportSellerTerm(
    val id: String,
    val title: String,
    val description: String
)
