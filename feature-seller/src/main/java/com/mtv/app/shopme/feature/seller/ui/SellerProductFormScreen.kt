/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductFormScreen.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.54
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
    var price by remember { mutableStateOf(uiData.selectedProduct?.price ?: "") }
    var stock by remember { mutableStateOf(uiData.selectedProduct?.stock?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                if (uiData.selectedProduct != null) "Edit Product" else "Add Product",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val product = SellerProduct(
                    id = uiData.selectedProduct?.id ?: System.currentTimeMillis().toString(),
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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(AppColor.Orange)
        ) {
            Text("Save", color = Color.White)
        }
    }
}
