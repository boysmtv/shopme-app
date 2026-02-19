/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductListScreen.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.54
 */

package com.mtv.app.shopme.feature.seller.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.feature.seller.contract.SellerProductDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerProductEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerProductNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerProductStateListener
import com.mtv.app.shopme.feature.seller.model.SellerProduct

@Composable
fun SellerProductListScreen(
    uiState: SellerProductStateListener,
    uiData: SellerProductDataListener,
    uiEvent: SellerProductEventListener,
    uiNavigation: SellerProductNavigationListener
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = uiNavigation.onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                "My Products",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = uiNavigation.navigateToAddProduct,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(AppColor.Orange)
            ) {
                Text("Add Product", color = Color.White)
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(uiState.productList) { product ->
                SellerProductItem(
                    product = product,
                    onEdit = { uiNavigation.navigateToEditProduct(product) },
                    onDelete = { uiEvent.onDeleteProduct(product.id) }
                )
            }
        }
    }
}

@Composable
fun SellerProductItem(
    product: SellerProduct,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AppColor.LightOrange)
        ) {
            // Bisa ganti dengan AsyncImage Coil jika ada url
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, fontFamily = PoppinsFont, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text("Price: ${product.price}", fontSize = 13.sp, color = Color.Gray)
            Text("Stock: ${product.stock}", fontSize = 13.sp, color = Color.Gray)
        }

        Row {
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit"
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete"
                )
            }
        }
    }
}
