/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerPaymentMethodScreen.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 22.20
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodStateListener

@Composable
fun SellerPaymentMethodScreen(
    uiState: SellerPaymentMethodStateListener,
    uiData: SellerPaymentMethodDataListener,
    uiEvent: SellerPaymentMethodEventListener,
    uiNavigation: SellerPaymentMethodNavigationListener
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

        SellerPaymentHeader(uiNavigation)

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            colors = CardDefaults.cardColors(containerColor = AppColor.WhiteSoft)
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {

                PaymentMethodCard(
                    icon = "💵",
                    title = "Cash Payment",
                    description = "Customer pays when order arrives",
                    enabled = uiData.cashEnabled,
                    onToggle = uiEvent.onCashToggle
                )

                Spacer(Modifier.height(16.dp))

                PaymentMethodCard(
                    icon = "🏦",
                    title = "Bank Transfer",
                    description = "Input your bank account",
                    enabled = uiData.bankEnabled,
                    onToggle = uiEvent.onBankToggle
                ) {

                    BankDropdown()

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiData.bankNumber,
                        onValueChange = {
                            uiEvent.onBankChange(formatAccountNumber(it))
                        },
                        label = { Text("Account Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                PaymentMethodCard(
                    icon = "🔵",
                    title = "DANA Wallet",
                    description = "Receive payment via DANA",
                    enabled = uiData.danaEnabled,
                    onToggle = uiEvent.onDanaToggle
                ) {

                    OutlinedTextField(
                        value = uiData.danaNumber,
                        onValueChange = uiEvent.onDanaChange,
                        label = { Text("DANA Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(16.dp))

                PaymentMethodCard(
                    icon = "🟢",
                    title = "GoPay Wallet",
                    description = "Receive payment via GoPay",
                    enabled = uiData.gopayEnabled,
                    onToggle = uiEvent.onGopayToggle
                ) {

                    OutlinedTextField(
                        value = uiData.gopayNumber,
                        onValueChange = uiEvent.onGopayChange,
                        label = { Text("GoPay Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(16.dp))

                PaymentMethodCard(
                    icon = "🟣",
                    title = "OVO Wallet",
                    description = "Receive payment via OVO",
                    enabled = uiData.ovoEnabled,
                    onToggle = uiEvent.onOvoToggle
                ) {

                    OutlinedTextField(
                        value = uiData.ovoNumber,
                        onValueChange = uiEvent.onOvoChange,
                        label = { Text("OVO Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentMethodCard(
    icon: String,
    title: String,
    description: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
    content: @Composable (() -> Unit)? = null
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.White)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(icon, fontSize = 22.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            title,
                            fontFamily = PoppinsFont,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            description,
                            fontSize = 12.sp,
                            color = AppColor.Gray,
                            fontFamily = PoppinsFont
                        )
                    }
                }

                Switch(
                    checked = enabled,
                    onCheckedChange = onToggle
                )
            }

            AnimatedVisibility(
                visible = enabled && content != null,
                enter = fadeIn() + expandVertically()
            ) {

                Column {

                    Spacer(Modifier.height(16.dp))

                    content?.invoke()
                }
            }
        }
    }
}

fun formatAccountNumber(input: String): String {
    return input
        .filter { it.isDigit() }
        .chunked(4)
        .joinToString(" ")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankDropdown() {

    var expanded by remember { mutableStateOf(false) }
    var selectedBank by remember { mutableStateOf("BCA") }

    val banks = listOf("BCA", "BRI", "Mandiri", "BNI")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = selectedBank,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Bank") },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                ),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            banks.forEach {

                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        selectedBank = it
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun SellerPaymentHeader(nav: SellerPaymentMethodNavigationListener) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = nav.navigateBack) {

            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(Modifier.width(6.dp))

        Text(
            "Payment Method",
            fontFamily = PoppinsFont,
            fontSize = 22.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerPaymentMethodPreview() {

    SellerPaymentMethodScreen(
        uiState = SellerPaymentMethodStateListener(),
        uiData = SellerPaymentMethodDataListener(
            cashEnabled = true,
            bankNumber = "1234567890",
            gopayNumber = "08123456789",
            danaNumber = "08123456789",
            ovoNumber = "08123456789"
        ),
        uiEvent = SellerPaymentMethodEventListener(
            onCashToggle = {},
            onBankChange = {},
            onGopayChange = {},
            onDanaChange = {},
            onOvoChange = {},
            onSave = {}
        ),
        uiNavigation = SellerPaymentMethodNavigationListener(
            navigateBack = {}
        )
    )
}