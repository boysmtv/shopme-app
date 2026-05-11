/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerStoreScreen.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.01
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.SmartImage
import com.mtv.app.shopme.domain.model.ProfileMenuItem
import com.mtv.app.shopme.feature.seller.contract.SellerStoreEvent
import com.mtv.app.shopme.feature.seller.contract.SellerStoreUiState
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

@Composable
fun SellerStoreScreen(
    state: SellerStoreUiState,
    event: (SellerStoreEvent) -> Unit
) {
    val menuItems = listOf(
        ProfileMenuItem(
            title = "Order History",
            subtitle = "Order information",
            icon = Icons.AutoMirrored.Filled.ReceiptLong,
            onClick = { event(SellerStoreEvent.ClickOrderHistory) }
        ),
        ProfileMenuItem(
            title = "Store Settings",
            subtitle = "Manage your store",
            icon = Icons.Default.Store,
            onClick = { event(SellerStoreEvent.ClickStoreSettings) }
        ),
        ProfileMenuItem(
            title = "Payment Methods",
            subtitle = "Manage payout account",
            icon = Icons.Default.AccountBalance,
            onClick = { event(SellerStoreEvent.ClickBankAccount) }
        ),
        ProfileMenuItem(
            title = "Change Password",
            subtitle = "Update your password",
            icon = Icons.Default.Lock,
            onClick = { event(SellerStoreEvent.ClickChangePassword) }
        ),
        ProfileMenuItem(
            title = "Back to Customer",
            subtitle = "Back to customer for buy something",
            icon = Icons.Default.Lock,
            onClick = { event(SellerStoreEvent.ClickBackToCustomer) }
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .statusBarsPadding()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SellerStoreTopBar(
                onBack = { event(SellerStoreEvent.ClickBack) },
                onLogout = { event(SellerStoreEvent.Logout) }
            )
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.WhiteSoft)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            item {
                SellerStoreHeader(
                    state = state,
                    event = event
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            items(menuItems) { item ->
                ProfileMenuItem(
                    item = item
                )
            }
        }
    }
}


@Composable
fun SellerStoreTopBar(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onBack,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Back",
                tint = AppColor.BlueMedium
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Profile",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                fontFamily = PoppinsFont,
                color = Color.Black
            )
        }

        IconButton(
            onClick = onLogout,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Logout",
                tint = AppColor.BlueMedium
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    item: ProfileMenuItem
) {

    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        label = EMPTY_STRING
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interaction,
                indication = ripple(),
                onClick = item.onClick
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColor.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AppColor.Blue.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    item.icon,
                    contentDescription = null,
                    tint = AppColor.Blue
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFont
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = PoppinsFont
                )
            }

            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SellerStoreHeader(
    state: SellerStoreUiState,
    event: (SellerStoreEvent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColor.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                val headerImage = state.sellerPhoto.ifBlank { state.storePhoto }

                if (headerImage.isNotBlank()) {
                    SmartImage(
                        model = headerImage,
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(AppColor.Blue.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.storeName.firstOrNull()?.uppercase() ?: state.sellerName.firstOrNull()
                                ?.uppercase() ?: "S",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PoppinsFont,
                            color = AppColor.Blue,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column {

                    Text(
                        text = state.sellerName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFont
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = state.storeName,
                        fontSize = 14.sp,
                        color = AppColor.Gray,
                        fontFamily = PoppinsFont
                    )

                    Spacer(Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        val onlineColor =
                            if (state.isOnline) Color(0xFF2E7D32) else AppColor.Gray

                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(onlineColor)
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(
                            text = if (state.isOnline) "Online" else "Offline",
                            fontSize = 12.sp,
                            color = onlineColor,
                            fontFamily = PoppinsFont
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = state.storeAddress,
                fontSize = 12.sp,
                color = AppColor.Gray,
                fontFamily = PoppinsFont,
                lineHeight = 16.sp
            )

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SellerActionButton(
                    text = "Edit Store",
                    background = AppColor.Blue,
                    textColor = Color.White,
                    modifier = Modifier.weight(1f),
                    onClick = { event(SellerStoreEvent.ClickStoreSettings) }
                )

                SellerActionButton(
                    text = "Check Store",
                    background = AppColor.BlueSoft,
                    textColor = AppColor.Blue,
                    modifier = Modifier.weight(1f),
                    onClick = { event(SellerStoreEvent.ToggleOnline) }
                )
            }
        }
    }
}

@Composable
fun SellerActionButton(
    text: String,
    background: Color,
    textColor: Color = Color.White,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = PoppinsFont
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerStoreScreenPreview() {

    val mockState = SellerStoreUiState(
        sellerName = "Dedy Wijaya",
        email = "seller@email.com",
        phone = "08123456789",
        storeName = "Shopme Store",
        storeAddress = "Jakarta, Indonesia",
        isOnline = true
    )

    SellerStoreScreen(
        state = mockState,
        event = {}
    )
}
