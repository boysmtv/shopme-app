/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditAddressScreen.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 11.00
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.base.BaseSimpleDropdownField
import com.mtv.app.shopme.common.base.BaseSimpleFormField
import com.mtv.app.shopme.feature.customer.contract.EditAddressDataListener
import com.mtv.app.shopme.feature.customer.contract.EditAddressEventListener
import com.mtv.app.shopme.feature.customer.contract.EditAddressNavigationListener
import com.mtv.app.shopme.feature.customer.contract.EditAddressStateListener

@Composable
fun EditAddressScreen(
    uiState: EditAddressStateListener,
    uiData: EditAddressDataListener,
    uiEvent: EditAddressEventListener,
    uiNavigation: EditAddressNavigationListener
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AppColor.Green,
                        AppColor.GreenSoft
                    )
                )
            )
            .padding(top = 32.dp)
    ) {
        EditAddressHeader(uiNavigation)

        Spacer(modifier = Modifier.height(16.dp))

        EditAddressMenus(uiEvent, uiData, uiNavigation)
    }
}

@Composable
fun EditAddressHeader(uiNavigation: EditAddressNavigationListener) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { uiNavigation.onBack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Edit Profil",
            fontFamily = PoppinsFont,
            fontSize = 22.sp,
            color = Color.White
        )
    }
}

@Composable
fun EditAddressMenus(
    uiEvent: EditAddressEventListener,
    uiData: EditAddressDataListener,
    uiNavigation: EditAddressNavigationListener
) {

    var village by remember { mutableStateOf(uiData.village) }
    var block by remember { mutableStateOf(uiData.block) }
    var number by remember { mutableStateOf(uiData.number) }
    var rt by remember { mutableStateOf(uiData.rt) }
    var rw by remember { mutableStateOf(uiData.rw) }
    var map by remember { mutableStateOf(uiData.map) }

    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            val villageList = listOf(
                "Perumahan Griya Asri",
                "Permata Indah",
                "Bukit Hijau Residence",
                "Green Valley",
                "Puri Mewah"
            )

            BaseSimpleDropdownField(
                label = "Nama Perumahan",
                value = village,
                options = villageList,
            ) { village = it }

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {

                Row(modifier = Modifier.fillMaxWidth()) {

                    BaseSimpleFormField(
                        label = "Blok",
                        value = block,
                        modifier = Modifier.weight(1f)
                    ) { block = it }

                    Spacer(modifier = Modifier.width(8.dp))

                    BaseSimpleFormField(
                        label = "No",
                        value = number,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    ) { number = it }

                    Spacer(modifier = Modifier.width(8.dp))

                    BaseSimpleFormField(
                        label = "RT",
                        value = rt,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    ) { rt = it }

                    Spacer(modifier = Modifier.width(8.dp))

                    BaseSimpleFormField(
                        label = "RW",
                        value = rw,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    ) { rw = it }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            BaseSimpleFormField(
                label = "Map",
                value = map
            ) { map = it }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    uiEvent.onSaveClicked(village, block, number, rt, rw, map)
                    uiNavigation.onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColor.Green)
            ) {
                Text(
                    text = "Simpan",
                    fontFamily = PoppinsFont,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun EditAddressScreenPreview() {
    EditAddressScreen(
        uiState = EditAddressStateListener(),
        uiData = EditAddressDataListener(),
        uiEvent = EditAddressEventListener(),
        uiNavigation = EditAddressNavigationListener()
    )
}
