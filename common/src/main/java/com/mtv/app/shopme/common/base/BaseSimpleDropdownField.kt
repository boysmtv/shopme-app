/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BaseSimpleDropdownField.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 14.43
 */

package com.mtv.app.shopme.common.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
@Composable
fun BaseSimpleDropdownField(
    label: String,
    value: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {

        Text(
            text = label,
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Column {
                Text(
                    text = value.ifEmpty { "Pilih $label" },
                    fontFamily = PoppinsFont,
                    fontSize = 14.sp,
                    color = if (value.isEmpty()) Color.LightGray else Color.Black
                )

                Spacer(Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }
        }

        if (expanded) {
            Dialog(onDismissRequest = { expanded = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .background(Color.White, RoundedCornerShape(20.dp))
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Pilih $label",
                            fontFamily = PoppinsFont,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                        )

                        HorizontalDivider(
                            color = Color.Gray,
                            thickness = 1.dp,
                        )

                        Column(modifier = Modifier.fillMaxWidth()) {
                            options.forEach { item ->

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onSelect(item)
                                            expanded = false
                                        }
                                        .padding(
                                            start = 20.dp,
                                            end = 20.dp,
                                            top = 16.dp,
                                            bottom = 16.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null,
                                        tint = AppColor.Orange,
                                        modifier = Modifier.size(22.dp)
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = item,
                                        fontFamily = PoppinsFont,
                                        fontSize = 15.sp,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                HorizontalDivider(
                                    color = Color.LightGray.copy(alpha = 0.3f),
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun BaseSimpleDropdownFieldPreview() {
//    var selected by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(20.dp)
//    ) {
//        BaseSimpleDropdownField(
//            label = "Kategori",
//            value = selected,
//            options = listOf("Elektronik", "Fashion", "Otomotif", "Makanan"),
//            onSelect = { selected = it }
//        )
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun BaseSimpleDropdownFieldDialogPreview() {
//    var selected by remember { mutableStateOf("") }
//    var expanded by remember { mutableStateOf(true) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(20.dp)
//    ) {
//        BaseSimpleDropdownField(
//            label = "Kategori",
//            value = selected,
//            options = listOf("Elektronik", "Fashion", "Otomotif", "Makanan"),
//            onSelect = {
//                selected = it
//                expanded = false
//            }
//        )
//
//        if (expanded) {
//            Dialog(onDismissRequest = { expanded = false }) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(24.dp)
//                        .background(Color.White, RoundedCornerShape(20.dp))
//                ) {
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = "Pilih Kategori",
//                            fontFamily = PoppinsFont,
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.SemiBold,
//                            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
//                        )
//
//                        HorizontalDivider(
//                            color = Color.Gray,
//                            thickness = 1.dp,
//                        )
//
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                        ) {
//                            listOf("Elektronik", "Fashion", "Otomotif", "Makanan").forEach { item ->
//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp),
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Default.Home,
//                                        contentDescription = null,
//                                        tint = AppColor.Orange,
//                                        modifier = Modifier.size(22.dp)
//                                    )
//
//                                    Spacer(modifier = Modifier.width(12.dp))
//
//                                    Text(
//                                        text = item,
//                                        fontFamily = PoppinsFont,
//                                        fontSize = 15.sp,
//                                        modifier = Modifier.weight(1f)
//                                    )
//
//                                    Icon(
//                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
//                                        contentDescription = null,
//                                        tint = Color.Gray,
//                                        modifier = Modifier.size(20.dp)
//                                    )
//                                }
//
//                                HorizontalDivider(
//                                    color = Color.LightGray.copy(alpha = 0.3f),
//                                    thickness = 1.dp
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}