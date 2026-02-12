/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: MapScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 15.18
 */

package com.mtv.app.shopme.feature.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.contract.MapDataListener
import com.mtv.app.shopme.feature.contract.MapEventListener
import com.mtv.app.shopme.feature.contract.MapNavigationListener
import com.mtv.app.shopme.feature.contract.MapStateListener


@Composable
fun MapScreen(
    uiState: MapStateListener,
    uiData: MapDataListener,
    uiEvent: MapEventListener,
    uiNavigation: MapNavigationListener
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // AppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.width(8.dp))
            Text("Select Location", fontFamily = PoppinsFont, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // Placeholder Map
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text("Map Placeholder", fontFamily = PoppinsFont, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun MapScreenPreview() {
    MapScreen(
        uiState = MapStateListener(),
        uiData = MapDataListener(),
        uiEvent = MapEventListener(),
        uiNavigation = MapNavigationListener()
    )
}
