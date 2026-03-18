/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeScreen.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 14.12
 */
package com.mtv.app.shopme.feature.seller.ui

import android.net.Uri
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
import androidx.compose.material.icons.filled.HourglassEmpty
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeStateListener

enum class CafeStep { BASIC, ADDRESS, PHOTO, REVIEW }

@Composable
fun SellerCreateCafeScreen(
    uiState: SellerCreateCafeStateListener,
    uiData: SellerCreateCafeDataListener,
    uiEvent: SellerCreateCafeEventListener,
    uiNavigation: SellerCreateCafeNavigationListener,
    initialStep: CafeStep = CafeStep.BASIC
) {

    var step by remember { mutableStateOf(initialStep) }

    var cafePhotoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        uri ?: return@rememberLauncherForActivityResult

        cafePhotoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.WhiteSoft)
    ) {

        Surface(shadowElevation = 4.dp) {
            Column(Modifier.padding(16.dp)) {

                CafeStepper(step)

            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
        ) {

            when (step) {

                CafeStep.BASIC -> {

                    item {

                        SectionTitle("Cafe Information")

                        Spacer(modifier = Modifier.height(16.dp))

                        ModernCard {

                            ModernOutlinedField(
                                value = uiData.cafeName,
                                onValueChange = uiEvent.onCafeNameChange,
                                label = "Cafe Name",
                                icon = Icons.Default.Store,
                                modifier = Modifier.fillMaxWidth()
                            )

                            ModernOutlinedField(
                                value = uiData.phone,
                                onValueChange = uiEvent.onPhoneChange,
                                label = "Phone Number",
                                icon = Icons.Default.Phone,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row {
                                ModernOutlinedField(
                                    value = uiData.openHours,
                                    icon = Icons.Default.WatchLater,
                                    onValueChange = uiEvent.onOpenHoursChange,
                                    label = "Opening Hours",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                )

                                Spacer(Modifier.width(16.dp))

                                ModernOutlinedField(
                                    value = uiData.closeHours,
                                    icon = Icons.Default.WatchLater,
                                    onValueChange = uiEvent.onOpenHoursChange,
                                    label = "Opening Hours",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                )
                            }

                            ModernOutlinedField(
                                value = uiData.minOrder,
                                onValueChange = uiEvent.onDescriptionChange,
                                label = "Minimum Order",
                                icon = Icons.Default.PriceChange,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                singleLine = false
                            )

                            ModernOutlinedField(
                                value = uiData.description,
                                onValueChange = uiEvent.onDescriptionChange,
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
                                value = uiData.village,
                                onValueChange = uiEvent.onVillageChange,
                                label = "Village",
                                icon = Icons.Default.LocationOn,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                ModernOutlinedField(
                                    value = uiData.block,
                                    onValueChange = uiEvent.onBlockChange,
                                    label = "Block",
                                    modifier = Modifier.weight(1f)
                                )

                                ModernOutlinedField(
                                    value = uiData.number,
                                    onValueChange = uiEvent.onNumberChange,
                                    label = "Number",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                ModernOutlinedField(
                                    value = uiData.rt,
                                    onValueChange = uiEvent.onRtChange,
                                    label = "RT",
                                    modifier = Modifier.weight(1f)
                                )

                                ModernOutlinedField(
                                    value = uiData.rw,
                                    onValueChange = uiEvent.onRwChange,
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
                                photoUri = cafePhotoUri,
                                onUploadClick = {
                                    launcher.launch("image/*")
                                },
                                onRemoveClick = {
                                    cafePhotoUri = null
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
                            cafePhotoUri?.let {

                                ModernCard {

                                    Column {

                                        Text(
                                            text = "Cafe Photo",
                                            fontFamily = PoppinsFont,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Spacer(Modifier.height(12.dp))

                                        AsyncImage(
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
                                        value = uiData.cafeName
                                    )

                                    ReviewItem(
                                        icon = Icons.Default.Phone,
                                        label = "Phone",
                                        value = uiData.phone
                                    )

                                    ReviewItem(
                                        icon = Icons.Default.Schedule,
                                        label = "Opening Hours",
                                        value = "${uiData.openHours} - ${uiData.closeHours}"
                                    )

                                    ReviewItem(
                                        icon = Icons.Default.ShoppingCart,
                                        label = "Minimum Order",
                                        value = uiData.minOrder
                                    )
                                }
                            }

                            // DESCRIPTION
                            ModernCard {

                                Column {

                                    SectionSmallTitle("Description")

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = uiData.description,
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
                                        value = uiData.village
                                    )

                                    ReviewItem(
                                        icon = Icons.Default.Home,
                                        label = "Block / Number",
                                        value = "${uiData.block}/${uiData.number}"
                                    )

                                    ReviewItem(
                                        icon = Icons.Default.Map,
                                        label = "RT / RW",
                                        value = "${uiData.rt}/${uiData.rw}"
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

                if (step != CafeStep.BASIC) {

                    Button(
                        onClick = {
                            step = CafeStep.entries[step.ordinal - 1]
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
                        if (step == CafeStep.REVIEW) {
                            uiEvent.onCreateCafe()
                        } else {
                            step = CafeStep.entries[step.ordinal + 1]
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColor.Blue
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = if (step == CafeStep.REVIEW) "Create Cafe" else "Next",
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
            text = step.name,
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
    photoUri: Uri?,
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

            if (photoUri != null) {

                AsyncImage(
                    model = photoUri,
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
            uiState = SellerCreateCafeStateListener(),
            uiData = SellerCreateCafeDataListener(
                cafeName = "Shopme Coffee",
                phone = "08123456789",
                openHours = "09:00",
                closeHours = "22:00",
                minOrder = "Rp 10.000",
                description = "Specialty coffee & fresh bakery"
            ),
            uiEvent = SellerCreateCafeEventListener(),
            uiNavigation = SellerCreateCafeNavigationListener(),
            initialStep = CafeStep.BASIC
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
            uiState = SellerCreateCafeStateListener(),
            uiData = SellerCreateCafeDataListener(
                cafeName = "Shopme Coffee",
                village = "Griya Asri",
                block = "A",
                number = "12",
                rt = "01",
                rw = "02"
            ),
            uiEvent = SellerCreateCafeEventListener(),
            uiNavigation = SellerCreateCafeNavigationListener(),
            initialStep = CafeStep.ADDRESS
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
            uiState = SellerCreateCafeStateListener(),
            uiData = SellerCreateCafeDataListener(
                cafeName = "Shopme Coffee",
                village = "Griya Asri",
                block = "A",
                number = "12",
                rt = "01",
                rw = "02"
            ),
            uiEvent = SellerCreateCafeEventListener(),
            uiNavigation = SellerCreateCafeNavigationListener(),
            initialStep = CafeStep.PHOTO
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
            uiState = SellerCreateCafeStateListener(),
            uiData = SellerCreateCafeDataListener(
                cafeName = "Shopme Coffee",
                phone = "08123456789",
                openHours = "09:00",
                closeHours = "22:00",
                minOrder = "Rp 10.000",
                description = "Specialty coffee & fresh bakery",
                village = "Griya Asri",
                block = "A",
                number = "12",
                rt = "01",
                rw = "02"
            ),
            uiEvent = SellerCreateCafeEventListener(),
            uiNavigation = SellerCreateCafeNavigationListener(),
            initialStep = CafeStep.REVIEW
        )

    }
}