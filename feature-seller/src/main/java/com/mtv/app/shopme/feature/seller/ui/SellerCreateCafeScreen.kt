/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeScreen.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 14.12
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.SmartImage
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEvent
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeUiState

enum class CafeStep { BASIC, ADDRESS, PHOTO, REVIEW }

@Composable
fun SellerCreateCafeScreen(
    state: SellerCreateCafeUiState,
    event: (SellerCreateCafeEvent) -> Unit
) {
    val currentStep = CafeStep.entries.getOrElse(state.step - 1) { CafeStep.BASIC }
    val photo = state.cafePhoto
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        event(SellerCreateCafeEvent.ImageSelected(uri.toString()))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.WhiteSoft)
    ) {

        Surface(shadowElevation = 4.dp) {
            Column(Modifier.padding(16.dp)) {
                CafeStepper(currentStep)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
        ) {

            when (currentStep) {

                CafeStep.BASIC -> {

                    item {

                        SectionTitle("Cafe Information")

                        Spacer(modifier = Modifier.height(16.dp))

                        ModernCard {

                            ModernOutlinedField(
                                value = state.cafeName.ifEmpty { "-" },
                                onValueChange = { event(SellerCreateCafeEvent.ChangeCafeName(it)) },
                                label = "Cafe Name",
                                icon = Icons.Default.Store,
                                modifier = Modifier.fillMaxWidth()
                            )

                            ModernOutlinedField(
                                value = state.phone,
                                onValueChange = { event(SellerCreateCafeEvent.ChangePhone(it)) },
                                label = "Phone Number",
                                icon = Icons.Default.Phone,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row {
                                ModernOutlinedField(
                                    value = state.openHours,
                                    onValueChange = { event(SellerCreateCafeEvent.ChangeOpenHours(it)) },
                                    icon = Icons.Default.WatchLater,
                                    label = "Open Hours",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                )

                                Spacer(Modifier.width(16.dp))

                                ModernOutlinedField(
                                    value = state.closeHours,
                                    onValueChange = { event(SellerCreateCafeEvent.ChangeCloseHours(it)) },
                                    icon = Icons.Default.WatchLater,
                                    label = "Close Hours",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                )
                            }

                            ModernOutlinedField(
                                value = state.minOrder,
                                onValueChange = { event(SellerCreateCafeEvent.ChangeMinOrder(it)) },
                                label = "Minimum Order",
                                icon = Icons.Default.PriceChange,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                singleLine = false
                            )

                            ModernOutlinedField(
                                value = state.description,
                                onValueChange = { event(SellerCreateCafeEvent.ChangeDescription(it)) },
                                label = "Description",
                                icon = Icons.Default.Description,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                singleLine = false
                            )
                        }
                    }
                }

                CafeStep.ADDRESS -> {

                    item {

                        SectionTitle("Cafe Address")

                        Spacer(modifier = Modifier.height(16.dp))

                        ModernCard {

                            ModernOutlinedField(
                                value = state.village,
                                onValueChange = { event(SellerCreateCafeEvent.ChangeVillage(it)) },
                                label = "Village",
                                icon = Icons.Default.LocationOn,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                ModernOutlinedField(
                                    value = state.block,
                                    onValueChange = { event(SellerCreateCafeEvent.ChangeBlock(it)) },
                                    label = "Block",
                                    modifier = Modifier.weight(1f)
                                )

                                ModernOutlinedField(
                                    value = state.number,
                                    onValueChange = { event(SellerCreateCafeEvent.ChangeNumber(it)) },
                                    label = "Number",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                ModernOutlinedField(
                                    value = state.rt,
                                    onValueChange = { event(SellerCreateCafeEvent.ChangeRt(it)) },
                                    label = "RT",
                                    modifier = Modifier.weight(1f)
                                )

                                ModernOutlinedField(
                                    value = state.rw,
                                    onValueChange = { event(SellerCreateCafeEvent.ChangeRw(it)) },
                                    label = "RW",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                CafeStep.PHOTO -> {
                    item {

                        SectionTitle("Cafe Profile Photo")

                        Spacer(Modifier.height(8.dp))

                        ModernCard {

                            CafeProfilePhotoUpload(
                                photoData = photo,
                                onUploadClick = {
                                    imagePicker.launch("image/*")
                                },
                                onRemoveClick = {
                                    event(SellerCreateCafeEvent.RemovePhoto)
                                }
                            )
                        }
                    }
                }

                CafeStep.REVIEW -> {

                    item {
                        SectionTitle("Review Cafe")

                        Spacer(modifier = Modifier.height(20.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

                            // PHOTO
                            photo?.let {
                                ModernCard {
                                    Column {
                                        Text(
                                            text = "Cafe Photo",
                                            fontFamily = PoppinsFont,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Spacer(Modifier.height(12.dp))

                                        SmartImage(
                                            model = it,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(180.dp)
                                                .clip(RoundedCornerShape(16.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }

                            // BASIC INFO
                            ModernCard {

                                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                                    SectionSmallTitle("Basic Information")

                                    ReviewItem(
                                        icon = Icons.Default.Store,
                                        label = "Cafe Name",
                                        value = state.cafeName.ifEmpty { "-" }
                                    )

                                    ReviewItem(
                                        icon = Icons.Default.Phone,
                                        label = "Phone",
                                        value = state.phone
                                    )

                                    ReviewItem(
                                        icon = Icons.Default.Schedule,
                                        label = "Opening Hours",
                                        value = "${state.openHours} - ${state.closeHours}"
                                    )

                                    ReviewItem(
                                        icon = Icons.Default.ShoppingCart,
                                        label = "Minimum Order",
                                        value = state.minOrder
                                    )
                                }
                            }

                            // DESCRIPTION
                            ModernCard {

                                Column {

                                    SectionSmallTitle("Description")

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = state.description,
                                        fontFamily = PoppinsFont,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }

                            ModernCard {

                                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                                    SectionSmallTitle("Address")

                                    ReviewItem(
                                        icon = Icons.Default.LocationOn,
                                        label = "Village",
                                        value = state.village
                                    )

                                    ReviewItem(
                                        icon = Icons.Default.Home,
                                        label = "Block / Number",
                                        value = "${state.block}/${state.number}"
                                    )

                                    ReviewItem(
                                        icon = Icons.Default.Map,
                                        label = "RT / RW",
                                        value = "${state.rt}/${state.rw}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Surface(shadowElevation = 8.dp) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                if (currentStep != CafeStep.BASIC) {
                    Button(
                        onClick = {
                            event(SellerCreateCafeEvent.PrevStep)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        )
                    ) {
                        Text("Back", fontFamily = PoppinsFont)
                    }
                }

                Button(
                    onClick = {
                        if (currentStep == CafeStep.REVIEW) {
                            event(SellerCreateCafeEvent.CreateCafe)
                        } else {
                            event(SellerCreateCafeEvent.NextStep)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColor.Blue
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = if (currentStep == CafeStep.REVIEW) "Create Cafe" else "Next",
                        fontFamily = PoppinsFont
                    )
                }
            }
        }
    }
}

@Composable
fun CafeStepper(step: CafeStep) {

    val progress = (step.ordinal + 1) / CafeStep.entries.size.toFloat()

    Column {

        Text(
            text = "Step ${step.ordinal + 1} of ${CafeStep.entries.size}",
            fontFamily = PoppinsFont,
            color = Color.Gray
        )

        Spacer(Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = AppColor.Blue
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = when (step) {
                CafeStep.BASIC -> "Basic Info"
                CafeStep.ADDRESS -> "Address"
                CafeStep.PHOTO -> "Photo"
                CafeStep.REVIEW -> "Review"
            },
            fontFamily = PoppinsFont,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ReviewItem(
    icon: ImageVector,
    label: String,
    value: String
) {

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppColor.Blue
        )

        Column {

            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = PoppinsFont
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = value.ifEmpty { "-" },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsFont
            )
        }
    }
}

@Composable
fun SectionSmallTitle(title: String) {
    Text(
        text = title,
        fontFamily = PoppinsFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    )
}

@Composable
fun CafeProfilePhotoUpload(
    photoData: String?,
    onUploadClick: () -> Unit,
    onRemoveClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F7FA))
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5E7EB),
                    shape = CircleShape
                )
                .clickable { onUploadClick() },
            contentAlignment = Alignment.Center
        ) {
            if (photoData != null) {
                SmartImage(
                    model = photoData,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable { onRemoveClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }

            } else {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Upload",
                        fontFamily = PoppinsFont,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Text(
            text = "Upload cafe profile photo",
            fontFamily = PoppinsFont,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Preview(
    name = "Create Cafe - Step 1 Basic",
    showBackground = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun PreviewCreateCafeBasic() {
    MaterialTheme {
        SellerCreateCafeScreen(
            state = SellerCreateCafeUiState(
                step = 1
            ),
            event = {}
        )
    }
}

@Preview(
    name = "Create Cafe - Step 2 Address",
    showBackground = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun PreviewCreateCafeAddress() {
    MaterialTheme {
        SellerCreateCafeScreen(
            state = SellerCreateCafeUiState(
                step = 2
            ),
            event = {}
        )
    }
}

@Preview(
    name = "Create Cafe - Step 3 Photo",
    showBackground = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun PreviewCreateCafePhotoStep() {
    MaterialTheme {
        SellerCreateCafeScreen(
            state = SellerCreateCafeUiState(
                step = 3
            ),
            event = {}
        )
    }
}

@Preview(
    name = "Create Cafe - Step 4 Review",
    showBackground = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun PreviewCreateCafeReview() {
    MaterialTheme {
        SellerCreateCafeScreen(
            state = SellerCreateCafeUiState(
                step = 4
            ),
            event = {}
        )
    }
}
