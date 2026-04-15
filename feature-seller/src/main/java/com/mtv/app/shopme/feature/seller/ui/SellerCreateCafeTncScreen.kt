/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeTncScreen.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 13.10
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncEvent
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncUiState

@Composable
fun SellerCreateCafeTncScreen(
    state: SellerCreateCafeTncUiState,
    event: (SellerCreateCafeTncEvent) -> Unit
) {

    val isValid =
        state.agreeTerms &&
                state.agreeFoodSafety &&
                state.agreeLocation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {

            Text(
                text = "Create Your Cafe",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PoppinsFont
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Before continuing to create your cafe profile, please read and agree to the following terms and conditions. These rules are designed to ensure that all cafes listed on the platform provide a safe, reliable, and trustworthy experience for customers.",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = PoppinsFont
            )

            Spacer(Modifier.height(24.dp))

            SellerTncCard(
                checked = state.agreeTerms,
                title = "Accurate Cafe Information",
                description = "I confirm that all information provided about my cafe including the cafe name, contact number, description, menu offerings, and other related details are accurate and truthful. I understand that providing misleading or incorrect information may lead to the suspension or removal of my cafe listing from the platform.",
                onCheckedChange = {
                    event(SellerCreateCafeTncEvent.AgreeTerms(it))
                }
            )

            Spacer(Modifier.height(16.dp))

            SellerTncCard(
                checked = state.agreeFoodSafety,
                title = "Food Safety and Quality Compliance",
                description = "I agree that all food and beverages sold through my cafe comply with local food safety regulations and hygiene standards. I will ensure that the preparation, storage, and serving of food maintain proper cleanliness and safety practices to protect the health and well-being of customers.",
                onCheckedChange = { event(SellerCreateCafeTncEvent.AgreeFoodSafety(it)) }
            )

            Spacer(Modifier.height(16.dp))

            SellerTncCard(
                checked = state.agreeLocation,
                title = "Valid and Accessible Cafe Location",
                description = "I confirm that the cafe address and location provided are correct and accessible for customers and delivery partners. I understand that inaccurate location information may cause order delivery issues and negatively affect customer experience.",
                onCheckedChange = { event(SellerCreateCafeTncEvent.AgreeLocation(it)) }
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "By continuing, you acknowledge that you have read and agreed to the terms above. These agreements help maintain the quality of cafes on our platform and ensure a better experience for all users.",
                fontSize = 13.sp,
                color = Color.Gray,
                fontFamily = PoppinsFont
            )
        }

        Button(
            onClick = { event(SellerCreateCafeTncEvent.Next) },
            enabled = isValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColor.Blue
            )
        ) {

            Text(
                text = "Continue to Create Cafe",
                color = Color.White,
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SellerTncCard(
    checked: Boolean,
    title: String,
    description: String,
    onCheckedChange: (Boolean) -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F9FC)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )

            Spacer(Modifier.width(8.dp))

            Column {

                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFont
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerCreateCafeTncScreenPreview() {
    SellerCreateCafeTncScreen(
        state = SellerCreateCafeTncUiState(
            agreeTerms = true,
            agreeFoodSafety = false,
            agreeLocation = false
        ),
        event = {}
    )
}