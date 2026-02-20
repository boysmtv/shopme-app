/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProfileScreen.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.01
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mtv.app.shopme.feature.seller.contract.SellerProfileDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerProfileEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerProfileNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerProfileStateListener
import androidx.compose.material3.ripple
import com.mtv.app.shopme.common.AppColor

@Composable
fun SellerProfileScreen(
    uiState: SellerProfileStateListener,
    uiData: SellerProfileDataListener,
    uiEvent: SellerProfileEventListener,
    uiNavigation: SellerProfileNavigationListener
) {

    val menuItems = listOf(
        ProfileMenuItemModel(
            "Order History",
            "Order information",
            Icons.AutoMirrored.Filled.ReceiptLong,
            uiNavigation.navigateToChangePassword
        ),
        ProfileMenuItemModel(
            "Payment Methods",
            "Manage payout account",
            Icons.Default.AccountBalance,
            uiNavigation.navigateToBankAccount
        ),
        ProfileMenuItemModel(
            "Store Settings",
            "Manage your store",
            Icons.Default.Store,
            uiNavigation.navigateToStoreSettings
        ),
        ProfileMenuItemModel(
            "Change Password",
            "Update your password",
            Icons.Default.Lock,
            uiNavigation.navigateToChangePassword
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {

        SellerProfileTopBar(
            onBack = uiNavigation.navigateToChangePassword,
            onLogout = uiEvent.onLogout
        )

        Spacer(Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            SellerProfileAvatar(
                imageUrl = "https://i.pravatar.cc/300"
            )

            Spacer(Modifier.height(16.dp))

            Text(
                uiData.sellerName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                uiData.email,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = uiNavigation.navigateToEditProfile,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColor.Blue
                )
            ) {
                Text("Edit Profile")
            }
        }

        Spacer(Modifier.height(28.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            menuItems.forEach {
                ProfileMenuItem(it)
            }
        }
    }
}

data class ProfileMenuItemModel(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun SellerProfileTopBar(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Profile",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        IconButton(
            onClick = onLogout,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
        }
    }
}

@Composable
fun ProfileMenuItem(
    item: ProfileMenuItemModel
) {

    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        label = ""
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
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(3.dp)
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
                Text(item.title, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(
                    item.subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
fun SellerProfileAvatar(
    imageUrl: String
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .size(110.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}


@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerProfileScreenPreview() {
    SellerProfileScreen(
        uiState = SellerProfileStateListener(),
        uiData = SellerProfileDataListener(
            sellerName = "Dedy Wijaya",
            email = "seller@email.com",
            phone = "08123456789",
            storeName = "Shopme Store",
            storeAddress = "Jakarta, Indonesia",
            isOnline = true
        ),
        uiEvent = SellerProfileEventListener(
            onToggleOnline = {},
            onEditProfile = {},
            onLogout = {}
        ),
        uiNavigation = SellerProfileNavigationListener({}, {}, {})
    )
}
