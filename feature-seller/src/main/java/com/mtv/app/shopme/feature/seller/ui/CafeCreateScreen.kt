/*
 * Project: Boys.mtv@gmail.com
 * File: CafeCreateScreen.kt
 *
 * Last modified by Dedy Wijaya on 13/03/2026 13.43
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.base.BaseSimpleFormField
import com.mtv.app.shopme.feature.seller.contract.CafeCreateDataListener
import com.mtv.app.shopme.feature.seller.contract.CafeCreateEventListener
import com.mtv.app.shopme.feature.seller.contract.CafeCreateNavigationListener
import com.mtv.app.shopme.feature.seller.contract.CafeCreateStateListener

@Composable
fun CafeCreateScreen(
    uiState: CafeCreateStateListener,
    uiData: CafeCreateDataListener,
    uiEvent: CafeCreateEventListener,
    uiNavigation: CafeCreateNavigationListener
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Blue, AppColor.BlueSoft)
                )
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = uiNavigation.navigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Text(
                text = "Create Cafe",
                color = Color.White,
                fontFamily = PoppinsFont,
                fontSize = 22.sp
            )
        }


        StorePhotoSection(
            imageUrl = uiData.image,
            onUpload = uiEvent.onUploadImage
        )

        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                BaseSimpleFormField(
                    label = "Cafe Name",
                    value = uiData.name
                ) {
                    uiEvent.onNameChange(it)
                }

                Spacer(Modifier.height(16.dp))

                BaseSimpleFormField(
                    label = "Phone",
                    value = uiData.phone
                ) {
                    uiEvent.onPhoneChange(it)
                }

                Spacer(Modifier.height(16.dp))

                BaseSimpleFormField(
                    label = "Description",
                    value = uiData.description
                ) {
                    uiEvent.onDescriptionChange(it)
                }

                Spacer(Modifier.height(16.dp))

                BaseSimpleFormField(
                    label = "Minimal Order",
                    value = uiData.minimalOrder
                ) {
                    uiEvent.onMinimalOrderChange(it)
                }

                Spacer(Modifier.height(16.dp))

                Row {
                    BaseSimpleFormField(
                        label = "Open Time (HH:mm)",
                        value = uiData.openTime,
                        modifier = Modifier.weight(1f)
                    ) {
                        uiEvent.onOpenTimeChange(it)
                    }

                    Spacer(Modifier.width(16.dp))

                    BaseSimpleFormField(
                        label = "Close Time (HH:mm)",
                        value = uiData.closeTime,
                        modifier = Modifier.weight(1f)
                    ) {
                        uiEvent.onCloseTimeChange(it)
                    }
                }

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = uiEvent.onCreateCafe,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {

                    Text(
                        text = "Create Cafe",
                        fontFamily = PoppinsFont
                    )
                }
            }
        }
    }
}

@Composable
private fun StorePhotoSection(
    imageUrl: String?,
    onUpload: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50))
                .background(AppColor.BlueSoft)
                .clickable { onUpload() },
            contentAlignment = Alignment.Center
        ) {

            if (imageUrl != null) {

                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

            } else {

                Text(
                    text = "Upload",
                    color = AppColor.Blue,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun CafeCreateScreenPreview() {

    CafeCreateScreen(
        uiState = CafeCreateStateListener(),

        uiData = CafeCreateDataListener(
            name = "Shopme Cafe",
            phone = "08123456789",
            description = "Best coffee in town",
            minimalOrder = "10000",
            openTime = "09:00",
            closeTime = "22:00",
            image = null,
            isActive = true
        ),

        uiEvent = CafeCreateEventListener(
            onNameChange = {},
            onPhoneChange = {},
            onDescriptionChange = {},
            onMinimalOrderChange = {},
            onOpenTimeChange = {},
            onCloseTimeChange = {},
            onUploadImage = {},
            onCreateCafe = {}
        ),

        uiNavigation = CafeCreateNavigationListener(
            navigateBack = {}
        )
    )
}