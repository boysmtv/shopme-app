/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChangePinScreen.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.17
 */
package com.mtv.app.shopme.feature.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.auth.contract.ChangePinDataListener
import com.mtv.app.shopme.feature.auth.contract.ChangePinEventListener
import com.mtv.app.shopme.feature.auth.contract.ChangePinNavigationListener
import com.mtv.app.shopme.feature.auth.contract.ChangePinStateListener

@Composable
fun ChangePinScreen(
    uiState: ChangePinStateListener,
    uiData: ChangePinDataListener,
    uiEvent: ChangePinEventListener,
    uiNavigation: ChangePinNavigationListener
) {

    val haptic = LocalHapticFeedback.current
    var errorTrigger by remember { mutableIntStateOf(0) }

    val isValid =
        uiData.oldPin.length == 6 &&
                uiData.newPin.length == 6 &&
                uiData.confirmPin.length == 6

    // Trigger shake + haptic when error
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            errorTrigger++
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Green, AppColor.GreenSoft)
                )
            )
    ) {

        TopBar(uiNavigation)

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(10.dp))

                Text(
                    "Keamanan PIN",
                    fontFamily = PoppinsFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(20.dp))

                ShakeWrapper(trigger = errorTrigger) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        PinSection("PIN Lama", uiData.oldPin, uiEvent.onOldPinChange)
                        Spacer(Modifier.height(18.dp))

                        PinSection("PIN Baru", uiData.newPin, uiEvent.onNewPinChange)

                        Spacer(Modifier.height(6.dp))
                        PinStrengthMeter(uiData.newPin)

                        Spacer(Modifier.height(18.dp))
                        PinSection("Konfirmasi PIN Baru", uiData.confirmPin, uiEvent.onConfirmPinChange)
                    }
                }

                AnimatedVisibility(uiState.error != null) {
                    Text(
                        uiState.error ?: "",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = uiEvent.onSubmit,
                    enabled = isValid && !uiState.loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColor.Green
                    )
                ) {

                    if (uiState.loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Simpan PIN", color = Color.White)
                    }
                }

                SuccessCheck(uiState.loading)
            }
        }
    }
}

@Composable
fun PinSection(
    title: String,
    value: String,
    onChange: (String) -> Unit
) {

    val focusRequesters = List(6) { FocusRequester() }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(title, fontSize = 13.sp, color = Color.Gray)

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            repeat(6) { index ->

                val digit = value.getOrNull(index)?.toString() ?: ""

                OutlinedTextField(
                    value = digit,
                    onValueChange = { new ->
                        if (new.length <= 1 && new.all { it.isDigit() }) {

                            val newValue =
                                value.padEnd(6, ' ')
                                    .toCharArray()
                                    .also { it[index] = new.firstOrNull() ?: ' ' }
                                    .concatToString()
                                    .trim()

                            onChange(newValue)

                            if (new.isNotEmpty() && index < 5) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(46.dp)
                        .height(56.dp)
                        .focusRequester(focusRequesters[index])
                        .border(
                            1.dp,
                            if (digit.isNotEmpty()) AppColor.Green else Color.Gray.copy(.3f),
                            RoundedCornerShape(10.dp)
                        ),
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 20.sp
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColor.Green,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = AppColor.Green
                    )
                )
            }
        }
    }
}

@Composable
private fun TopBar(nav: ChangePinNavigationListener) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = nav.onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
        }
        Text("Ubah PIN", color = Color.White, fontFamily = PoppinsFont)
    }
}

@Composable
fun PinStrengthMeter(pin: String) {

    val strength = when {
        pin.length < 4 -> 0
        pin.toSet().size <= 2 -> 1
        else -> 2
    }

    val color = when (strength) {
        0 -> Color.Red
        1 -> Color(0xFFFF9800)
        else -> Color(0xFF4CAF50)
    }

    Row(
        modifier = Modifier
            .padding(top = 6.dp)
            .height(4.dp)
            .fillMaxWidth(.5f)
    ) {
        repeat(3) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (it <= strength) color else Color.Gray.copy(.2f))
            )
            if (it < 2) Spacer(Modifier.width(4.dp))
        }
    }
}

@Composable
fun ShakeWrapper(
    trigger: Int,
    content: @Composable () -> Unit
) {
    val offset = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger > 0) {
            offset.snapTo(0f)
            offset.animateTo(
                targetValue = 10f,
                animationSpec = keyframes {
                    durationMillis = 350
                    0f at 0
                    20f at 50
                    20f at 100
                    15f at 150
                    15f at 200
                    5f at 250
                    5f at 300
                    0f at 350
                }
            )
        }
    }

    Box(modifier = Modifier.offset(x = offset.value.dp)) {
        content()
    }
}

@Composable
fun SuccessCheck(show: Boolean) {

    AnimatedVisibility(show) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(AppColor.Green, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "PIN berhasil diubah",
                color = AppColor.Green,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun ChangePinPreview() {
    ChangePinScreen(
        uiState = ChangePinStateListener(),
        uiData = ChangePinDataListener(),
        uiEvent = ChangePinEventListener(),
        uiNavigation = ChangePinNavigationListener()
    )
}