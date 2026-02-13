/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: BaseSimpleFormField.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 11.10
 */

package com.mtv.app.shopme.common.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.PoppinsFont

@Composable
fun BaseSimpleFormField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(16.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = PoppinsFont,
                fontSize = 14.sp,
                color = Color.Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
        ) { innerTextField ->
            Column(modifier = Modifier.fillMaxWidth()) {
                innerTextField()

                Spacer(Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }
        }
    }
}