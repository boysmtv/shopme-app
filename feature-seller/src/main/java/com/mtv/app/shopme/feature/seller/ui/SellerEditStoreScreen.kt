/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerEditStoreScreen.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 15.53
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.SmartImage
import com.mtv.app.shopme.common.base.BaseSimpleFormField
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreEvent
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreUiState
import com.mtv.based.uicomponent.core.component.loading.LoadingV2

@Composable
fun SellerEditStoreScreen(
    state: SellerEditStoreUiState,
    event: (SellerEditStoreEvent) -> Unit
){
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingV2()
        }
        return
    }

    val selectedTab = state.selectedTab
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        event(SellerEditStoreEvent.PhotoSelected(uri.toString()))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Blue, AppColor.BlueSoft)
                )
            )
    ) {

        SellerHeader { event(SellerEditStoreEvent.ClickBack) }

        Spacer(Modifier.height(16.dp))

        StorePhotoSection(
            imageUrl = state.storePhoto,
            onUpload = {
                imagePicker.launch("image/*")
            }
        )

        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {

                SellerTab(selectedTab) {
                    event(SellerEditStoreEvent.ChangeTab(it))
                }

                Spacer(Modifier.height(24.dp))

                when (selectedTab) {

                    0 -> {
                        StoreInformationSection(state, event)

                    }

                    1 -> {
                        StoreAddressSection(state, event)
                    }
                }

                Spacer(Modifier.height(28.dp))

                Button(
                    onClick = {
                        event(SellerEditStoreEvent.Save)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColor.Blue
                    )
                ) {
                    Text(
                        text = "Save Store",
                        color = Color.White,
                        fontFamily = PoppinsFont
                    )
                }
            }
        }
    }
}

@Composable
fun SellerTab(selected: Int, onChange: (Int) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(AppColor.BlueSoft)
    ) {

        listOf("Store Info", "Address").forEachIndexed { index, title ->

            val active = selected == index

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(if (active) AppColor.Blue else Color.Transparent)
                    .clickable { onChange(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = title,
                    color = if (active) Color.White else AppColor.Blue,
                    fontFamily = PoppinsFont
                )
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

                SmartImage(
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

@Composable
fun StoreInformationSection(
    state: SellerEditStoreUiState,
    event: (SellerEditStoreEvent) -> Unit
) {

    Column {

        BaseSimpleFormField(
            label = "Store Name",
            value = state.storeName
        ) {
            event(SellerEditStoreEvent.ChangeStoreName(it))
        }

        Spacer(Modifier.height(16.dp))

        BaseSimpleFormField(
            label = "Phone Number",
            value = state.phone
        ) {
            event(SellerEditStoreEvent.ChangePhone(it))
        }

        Spacer(Modifier.height(16.dp))

        BaseSimpleFormField(
            label = "Minimal Order",
            value = state.minOrder
        ) {
            event(SellerEditStoreEvent.ChangeMinOrder(it))
        }

        Spacer(Modifier.height(16.dp))

        BaseSimpleFormField(
            label = "Jam Buka",
            value = state.storeOpen
        ) {
            event(SellerEditStoreEvent.ChangeStoreOpen(it))
        }

        Spacer(Modifier.height(16.dp))

        BaseSimpleFormField(
            label = "Description",
            value = state.description,
        ) {
            event(SellerEditStoreEvent.ChangeDescription(it))
        }
    }
}

@Composable
fun StoreAddressSection(
    state: SellerEditStoreUiState,
    event: (SellerEditStoreEvent) -> Unit
) {

    Column {

        Text(
            text = "Store Address",
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(12.dp))

        BaseSimpleFormField(
            label = "Village / Area",
            value = state.village
        ) {
            event(SellerEditStoreEvent.ChangeVillage(it))
        }

        Spacer(Modifier.height(12.dp))

        Row {

            BaseSimpleFormField(
                label = "Block",
                value = state.block,
                modifier = Modifier.weight(1f)
            ) {
                event(SellerEditStoreEvent.ChangeBlock(it))
            }

            Spacer(Modifier.width(10.dp))

            BaseSimpleFormField(
                label = "Number",
                value = state.number,
                modifier = Modifier.weight(1f)
            ) {
                event(SellerEditStoreEvent.ChangeNumber(it))
            }
        }

        Spacer(Modifier.height(12.dp))

        Row {

            BaseSimpleFormField(
                label = "RT",
                value = state.rt,
                modifier = Modifier.weight(1f)
            ) {
                event(SellerEditStoreEvent.ChangeRt(it))
            }

            Spacer(Modifier.width(10.dp))

            BaseSimpleFormField(
                label = "RW",
                value = state.rw,
                modifier = Modifier.weight(1f)
            ) {
                event(SellerEditStoreEvent.ChangeRw(it))
            }
        }
    }
}

@Composable
fun SellerHeader(onBack: () -> Unit) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Text(
                text = "Edit Store",
                fontFamily = PoppinsFont,
                fontSize = 22.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerEditStoreScreenPreview() {
    SellerEditStoreScreen(
        state = SellerEditStoreUiState(
            storeName = "Shopme Store",
            phone = "08123456789",
            description = "Best shop for everything",
            storePhoto = null,
            minOrder = "Rp 10.000",
            storeOpen = "09.00 - 18.00",
            village = "Griya Asri",
            block = "A",
            number = "12",
            rt = "01",
            rw = "02"
        ),
        event = {}
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun StoreAddressPreview() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(20.dp)
    ) {

        StoreAddressSection(
            state = SellerEditStoreUiState(
                village = "Griya Asri",
                block = "A",
                number = "12",
                rt = "01",
                rw = "02"
            ),
            event = {}
        )
    }
}
