/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 13.00
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.feature.customer.contract.ProfileDataListener
import com.mtv.app.shopme.feature.customer.contract.ProfileEventListener
import com.mtv.app.shopme.feature.customer.contract.ProfileNavigationListener
import com.mtv.app.shopme.feature.customer.contract.ProfileStateListener
import com.mtv.app.shopme.nav.CustomerBottomNavigationBar

@Composable
fun ProfileScreen(
    navController: NavController,
    uiState: ProfileStateListener,
    uiData: ProfileDataListener,
    uiEvent: ProfileEventListener,
    uiNavigation: ProfileNavigationListener
) {

    val scrollState = rememberScrollState()

    val orderMenus = listOf(
        OrderMenu("Dipesan", Icons.Filled.AccountBalanceWallet, 1),
        OrderMenu("Dimasak", Icons.Filled.Inventory, 2),
        OrderMenu("Dikirim", Icons.Filled.LocalShipping, 3),
        OrderMenu("Selesai", Icons.Filled.CheckCircle, 4)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        AppColor.Green,
                        AppColor.GreenSoft
                    )
                )
            )
            .statusBarsPadding()
            .padding(top = 24.dp)
    ) {
        HeaderProfile()

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            AppColor.WhiteSoft,
                            AppColor.White
                        )
                    )
                ),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    fontFamily = PoppinsFont,
                    text = "Pesanan Saya",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    orderMenus.forEach { menu ->

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { uiNavigation.onOrder() },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(55.dp)
                                    .background(AppColor.GreenSoft, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = menu.icon,
                                    contentDescription = null,
                                    tint = AppColor.Green,
                                    modifier = Modifier.size(28.dp)
                                )

                                if (menu.count > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = 6.dp, y = (-6).dp)
                                            .background(Color.Red, RoundedCornerShape(50))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            menu.count.toString(),
                                            color = Color.White,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                menu.title,
                                fontSize = 12.sp,
                                fontFamily = PoppinsFont
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(modifier = Modifier.height(1.dp))

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                ) {
                    ProfileMenuItem(
                        title = "Edit Account",
                        icon = Icons.Default.Person,
                        onClickMenu = { uiNavigation.onEditProfile() }
                    )
                    ProfileMenuItem(
                        title = "Riwayat Belanja",
                        icon = Icons.Default.History,
                        onClickMenu = { uiNavigation.onOrderHistory() }
                    )
                    ProfileMenuItem(
                        title = "Pengaturan Akun",
                        icon = Icons.Default.Settings,
                        onClickMenu = { uiNavigation.onSettings() }
                    )
                    ProfileMenuItem(
                        title = "Menjadi Penjual",
                        icon = Icons.Default.CardTravel,
                        onClickMenu = {
                            uiNavigation.onNavigateToSeller(navController)
                        }
                    )
                    ProfileMenuItem(
                        title = "Bantuan",
                        icon = Icons.AutoMirrored.Filled.Help,
                        onClickMenu = { uiNavigation.onHelpCenter() }
                    )
                    ProfileMenuItem(
                        title = "Keluar",
                        icon = Icons.Default.Map,
                        isLogout = true,
                        onClickMenu = { /* logout */ }
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderProfile() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box {
                Image(
                    painter = painterResource(R.drawable.image_burger),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(24.dp))
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(Color(0xFF1DA1F2), RoundedCornerShape(50))
                        .padding(4.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    "Hi, Boy 👋",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFont
                )
                Text(
                    "08158844424",
                    color = Color.White.copy(.9f),
                    fontSize = 14.sp,
                    fontFamily = PoppinsFont
                )
                Text(
                    "Puri Lestari - Blok G06/01",
                    color = Color.White.copy(.7f),
                    fontSize = 12.sp,
                    fontFamily = PoppinsFont
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Stats Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.15f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ProfileStat("12", "Pesanan")
                ProfileStat("3", "Aktif")
                ProfileStat("Gold", "Member")
            }
        }
    }
}

@Composable
fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            label,
            color = Color.White.copy(.7f),
            fontSize = 12.sp
        )
    }
}

@Composable
fun ProfileMenuItem(
    title: String,
    icon: ImageVector,
    isLogout: Boolean = false,
    onClickMenu: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickMenu() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isLogout) Color.Red else AppColor.Green,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                fontFamily = PoppinsFont,
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isLogout) FontWeight.Bold else FontWeight.Normal,
                color = Color.Black
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = if (isLogout) Color.Red else AppColor.Gray
            )
        }

        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.4f),
            thickness = 1.dp
        )
    }
}

data class OrderMenu(
    val title: String,
    val icon: ImageVector,
    val count: Int
)

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            CustomerBottomNavigationBar(navController)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .fillMaxSize()
                .background(Color.White)
        ) {
            ProfileScreen(
                navController = navController,
                uiState = ProfileStateListener(),
                uiData = ProfileDataListener(),
                uiEvent = ProfileEventListener(),
                uiNavigation = ProfileNavigationListener()
            )
        }
    }
}