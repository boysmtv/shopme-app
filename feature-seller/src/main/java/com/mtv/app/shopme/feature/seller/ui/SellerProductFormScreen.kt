/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductFormScreen.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.54
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.seller.contract.SellerProductDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerProductEventListener
import com.mtv.app.shopme.feature.seller.model.SellerProduct

@Composable
fun SellerProductFormScreen(
    uiData: SellerProductDataListener,
    uiEvent: SellerProductEventListener,
    onBack: () -> Unit
) {

    var name by remember { mutableStateOf(uiData.selectedProduct?.name ?: "") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf(uiData.selectedProduct?.price ?: "") }
    var stock by remember { mutableStateOf(uiData.selectedProduct?.stock?.toString() ?: "") }
    var isActive by remember { mutableStateOf(true) }
    var category by remember { mutableStateOf("Food") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
    ) {

        // 🔵 Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColor.Blue)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Text(
                text = if (uiData.selectedProduct != null) "Edit Product" else "Add Product",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {

            // 🔹 Image Preview
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(Color.LightGray.copy(alpha = 0.3f))
                ) {
                    Text("Tap to upload image", color = Color.Gray)
                }
            }

            Spacer(Modifier.height(20.dp))

            // 🔹 Basic Info Card
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price (Rp)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = stock,
                        onValueChange = { stock = it },
                        label = { Text("Stock") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Product Status", fontWeight = FontWeight.SemiBold)
                        Text(
                            if (isActive) "Visible to customers"
                            else "Hidden from store",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Switch(
                        checked = isActive,
                        onCheckedChange = { isActive = it }
                    )
                }
            }
        }

        // 🔥 Save Button Fixed Bottom
        Button(
            onClick = {

                val product = SellerProduct(
                    id = uiData.selectedProduct?.id
                        ?: System.currentTimeMillis().toString(),
                    name = name,
                    price = price,
                    stock = stock.toIntOrNull() ?: 0
                )

                if (uiData.selectedProduct != null) {
                    uiEvent.onUpdateProduct(product)
                } else {
                    uiEvent.onAddProduct(product)
                }

                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(AppColor.Orange)
        ) {
            Text("Save Product", color = Color.White)
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun SellerProductFormAddPreview() {
    SellerProductFormScreen(
        uiData = SellerProductDataListener(
            selectedProduct = null
        ),
        uiEvent = SellerProductEventListener(),
        onBack = {}
    )
}
