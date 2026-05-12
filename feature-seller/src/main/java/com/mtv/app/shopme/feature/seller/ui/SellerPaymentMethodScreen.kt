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
import androidx.compose.foundation.layout.Box
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
import com.mtv.app.shopme.common.ShimmerBlock
import com.mtv.app.shopme.common.ShimmerLine
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodEvent
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodUiState

@Composable
fun SellerPaymentMethodScreen(
    state: SellerPaymentMethodUiState,
    event: (SellerPaymentMethodEvent) -> Unit
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

        SellerPaymentHeader(
            onBack = { event(SellerPaymentMethodEvent.ClickBack) }
        )

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

                if (state.isLoading && state.bankNumber.isBlank() && state.ovoNumber.isBlank() && state.danaNumber.isBlank() && state.gopayNumber.isBlank()) {
                    SellerPaymentMethodShimmer()
                    Spacer(Modifier.height(20.dp))
                }

                PaymentMethodCard(
                    icon = "💵",
                    title = "Cash Payment",
                    description = "Customer pays when order arrives",
                    enabled = state.cashEnabled,
                    onToggle = {
                        event(SellerPaymentMethodEvent.ToggleCash(it))
                    }
                )

                Spacer(Modifier.height(16.dp))

                PaymentMethodCard(
                    icon = "🏦",
                    title = "Bank Transfer",
                    description = "Input your bank account",
                    enabled = state.bankEnabled,
                    onToggle = {
                        event(SellerPaymentMethodEvent.ToggleBank(it))
                    }
                ) {

                    BankDropdown(
                        onSelected = {
                            // optional kalau mau simpan bank type
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.bankNumber,
                        onValueChange = {
                            event(
                                SellerPaymentMethodEvent.ChangeBank(
                                    formatAccountNumber(it)
                                )
                            )
                        },
                        label = { Text("Account Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                PaymentMethodCard(
                    icon = "🟣",
                    title = "OVO Wallet",
                    description = "Receive payment via OVO",
                    enabled = state.ovoEnabled,
                    onToggle = {
                        event(SellerPaymentMethodEvent.ToggleOvo(it))
                    }
                ) {

                    OutlinedTextField(
                        value = state.ovoNumber,
                        onValueChange = {
                            event(SellerPaymentMethodEvent.ChangeOvo(it))
                        },
                        label = { Text("OVO Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(16.dp))

                PaymentMethodCard(
                    icon = "🔵",
                    title = "DANA Wallet",
                    description = "Receive payment via DANA",
                    enabled = state.danaEnabled,
                    onToggle = {
                        event(SellerPaymentMethodEvent.ToggleDana(it))
                    }
                ) {

                    OutlinedTextField(
                        value = state.danaNumber,
                        onValueChange = {
                            event(SellerPaymentMethodEvent.ChangeDana(it))
                        },
                        label = { Text("DANA Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(16.dp))

                PaymentMethodCard(
                    icon = "🟢",
                    title = "GoPay Wallet",
                    description = "Receive payment via GoPay",
                    enabled = state.gopayEnabled,
                    onToggle = {
                        event(SellerPaymentMethodEvent.ToggleGopay(it))
                    }
                ) {

                    OutlinedTextField(
                        value = state.gopayNumber,
                        onValueChange = {
                            event(SellerPaymentMethodEvent.ChangeGopay(it))
                        },
                        label = { Text("GoPay Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { event(SellerPaymentMethodEvent.Save) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColor.Blue)
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
private fun SellerPaymentMethodShimmer() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        repeat(4) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AppColor.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ShimmerBlock(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                ShimmerLine(widthFraction = 0.34f, heightDp = 14)
                                ShimmerLine(widthFraction = 0.54f, heightDp = 11)
                            }
                        }
                        ShimmerBlock(
                            modifier = Modifier
                                .width(42.dp)
                                .height(24.dp),
                            shape = RoundedCornerShape(50)
                        )
                    }
                    ShimmerBlock(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
        ShimmerBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        )
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
fun BankDropdown(
    onSelected: (String) -> Unit
) {

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
                        onSelected(it)
                    }
                )
            }
        }
    }
}

@Composable
fun SellerPaymentHeader(
    onBack: () -> Unit
) {

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
        state = SellerPaymentMethodUiState(
            cashEnabled = true,
            bankEnabled = true,
            bankNumber = "1234 5678 9012",
            ovoEnabled = true,
            ovoNumber = "08123456789",
            danaEnabled = true,
            danaNumber = "08123456789",
            gopayEnabled = true,
            gopayNumber = "08123456789"
        ),
        event = {}
    )
}
