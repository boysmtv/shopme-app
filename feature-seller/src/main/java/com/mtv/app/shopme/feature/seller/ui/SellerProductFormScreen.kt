/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductFormScreen.kt
 *
 * Last modified by Dedy Wijaya on 15/04/26 16.08
 */

package com.mtv.app.shopme.feature.seller.ui

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.domain.model.ProductItem
import com.mtv.app.shopme.domain.model.VariantGroup
import com.mtv.app.shopme.feature.seller.contract.SellerProductFormEvent
import com.mtv.app.shopme.feature.seller.contract.SellerProductFormUiState

enum class Availability { READY, PREORDER, JASTIP }
enum class ProductStep { BASIC, PRICING, VARIANT, REVIEW }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerProductFormScreen(
    state: SellerProductFormUiState,
    event: (SellerProductFormEvent) -> Unit
) {
    val currentStep = state.step

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult

        val firstEmptyIndex = state.images.indexOfFirst { it == null }
        if (firstEmptyIndex != -1) {
            event(
                SellerProductFormEvent.AddImage(uri.toString())
            )
        }
    }

    Scaffold(
        containerColor = AppColor.WhiteSoft,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        fontFamily = PoppinsFont,
                        text = "Daftarkan Menu",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        event(SellerProductFormEvent.ClickBack)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColor.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    if (currentStep != ProductStep.BASIC) {
                        OutlinedButton(
                            onClick = {
                                event(SellerProductFormEvent.PrevStep)
                            }
                        ) {
                            Text(
                                fontFamily = PoppinsFont,
                                text = "Back"
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (currentStep == ProductStep.REVIEW) {
                                event(SellerProductFormEvent.Save)
                            } else {
                                event(SellerProductFormEvent.NextStep)
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(AppColor.Blue)
                    ) {
                        Text(
                            fontFamily = PoppinsFont,
                            text = if (currentStep == ProductStep.REVIEW)
                                "Save Product"
                            else "Next"
                        )
                    }
                }
            }
        }
    ) { padding ->

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(padding)
        ) {

            item {
                ModernStepper(currentStep)
            }

            when (currentStep) {
                ProductStep.BASIC -> {
                    item {
                        SectionTitle("Product Images")
                        Spacer(Modifier.height(8.dp))

                        ModernCard {

                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                Text(
                                    fontFamily = PoppinsFont,
                                    text = "Upload up to 5 images",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    state.images.forEachIndexed { index, uri ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (uri == null)
                                                        Color(0xFFF5F7FA)
                                                    else
                                                        Color.Transparent
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = if (uri == null)
                                                        Color(0xFFCBD5E1)
                                                    else
                                                        Color.Transparent,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable {
                                                    launcher.launch("image/*")
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {

                                            if (uri != null) {

                                                AsyncImage(
                                                    model = uri,
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .matchParentSize()
                                                )

                                                Box(
                                                    modifier = Modifier
                                                        .align(Alignment.TopEnd)
                                                        .padding(6.dp)
                                                        .size(22.dp)
                                                        .clip(CircleShape)
                                                        .background(Color.Black.copy(alpha = 0.6f))
                                                        .clickable {
                                                            event(SellerProductFormEvent.RemoveImage(index))
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        Icons.Default.Close,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }

                                            } else {

                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Icon(
                                                        Icons.Default.Add,
                                                        contentDescription = null,
                                                        tint = Color.Gray
                                                    )

                                                    Spacer(Modifier.height(4.dp))

                                                    Text(
                                                        fontFamily = PoppinsFont,
                                                        text = "Add",
                                                        fontSize = 12.sp,
                                                        color = Color.Gray
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        SectionTitle("Product Info")
                        Spacer(Modifier.height(8.dp))

                        ModernCard {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                                ModernOutlinedField(
                                    value = state.product.name,
                                    onValueChange = {
                                        event(SellerProductFormEvent.ChangeName(it))
                                    },
                                    label = "Product Name",
                                    icon = Icons.Default.Inventory2,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                ModernOutlinedField(
                                    value = state.product.description,
                                    onValueChange = {
                                        event(SellerProductFormEvent.ChangeDescription(it))
                                    },
                                    label = "Description",
                                    icon = Icons.Default.Description,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(AppColor.BlueSoft.copy(alpha = 0.15f))
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column {
                                        Text(
                                            "Active Product",
                                            fontFamily = PoppinsFont,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Text(
                                            "Show this product to customers",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Switch(
                                        checked = state.isActive,
                                        onCheckedChange = {
                                            event(SellerProductFormEvent.ChangeActive(it))
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = AppColor.Blue,
                                            uncheckedThumbColor = Color.White,
                                            uncheckedTrackColor = AppColor.BlueSoft
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                ProductStep.PRICING -> {
                    item {
                        SectionTitle("Pricing")
                        Spacer(Modifier.height(8.dp))

                        ModernCard {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                ModernOutlinedField(
                                    value = state.product.price,
                                    onValueChange = {
                                        event(
                                            SellerProductFormEvent.ChangePrice(
                                                it.filter { c -> c.isDigit() }
                                            )
                                        )
                                    },
                                    label = "Base Price",
                                    keyboardType = KeyboardType.Number,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                ModernOutlinedField(
                                    value = state.product.stock.toString(),
                                    onValueChange = {
                                        event(
                                            SellerProductFormEvent.ChangeStock(
                                                it.toIntOrNull() ?: 0
                                            )
                                        )
                                    },
                                    label = "Stock",
                                    keyboardType = KeyboardType.Number,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                AvailabilitySelector(
                                    selected = state.availability,
                                    onSelect = {
                                        event(SellerProductFormEvent.ChangeAvailability(it))
                                    }
                                )
                            }
                        }
                    }
                }

                ProductStep.VARIANT -> {

                    item {
                        SectionTitle("Variants")
                        Spacer(Modifier.height(8.dp))

                        ModernCard {
                            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                state.variants.forEachIndexed { index, group ->
                                    VariantGroupCard(
                                        group = group,
                                        groupIndex = index,
                                        onEvent = event
                                    )
                                }
                                OutlinedButton(
                                    onClick = {
                                        event(SellerProductFormEvent.AddVariantGroup)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(fontFamily = PoppinsFont, text = "+ Add Variant Group")
                                }
                            }
                        }
                    }
                }

                ProductStep.REVIEW -> {

                    item {

                        SectionTitle("Review Product")

                        Spacer(Modifier.height(8.dp))

                        ModernCard {

                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                                // Product Name
                                Column {
                                    Text(
                                        text = "Product Name",
                                        fontFamily = PoppinsFont,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )

                                    Text(
                                        text = state.product.name,
                                        fontFamily = PoppinsFont,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp
                                    )
                                }

                                // Description
                                Column {
                                    Text(
                                        text = "Description",
                                        fontFamily = PoppinsFont,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )

                                    Text(
                                        text = state.product.description.ifEmpty { "-" },
                                        fontFamily = PoppinsFont
                                    )
                                }

                                // Price & Stock
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Column {
                                        Text(
                                            "Price",
                                            fontFamily = PoppinsFont,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )

                                        Text(
                                            formatRupiah(state.product.price),
                                            fontFamily = PoppinsFont,
                                            fontWeight = FontWeight.SemiBold,
                                            color = AppColor.Blue
                                        )
                                    }

                                    Column {
                                        Text(
                                            "Stock",
                                            fontFamily = PoppinsFont,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )

                                        Text(
                                            state.product.stock.toString(),
                                            fontFamily = PoppinsFont,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                // Availability Badge
                                Row(verticalAlignment = Alignment.CenterVertically) {

                                    Text(
                                        text = "Status",
                                        fontFamily = PoppinsFont,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )

                                    Spacer(Modifier.width(8.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(AppColor.BlueSoft)
                                            .padding(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = state.availability.name,
                                            fontFamily = PoppinsFont,
                                            color = AppColor.Blue,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                // Active Badge
                                Row(verticalAlignment = Alignment.CenterVertically) {

                                    Text(
                                        text = "Visibility",
                                        fontFamily = PoppinsFont,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )

                                    Spacer(Modifier.width(8.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                if (state.isActive)
                                                    Color(0xFFE8F5E9)
                                                else
                                                    Color(0xFFFFEBEE)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = if (state.isActive) "Active" else "Hidden",
                                            fontFamily = PoppinsFont,
                                            color = if (state.isActive) Color(0xFF2E7D32) else Color.Red,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                // Variants
                                if (state.variants.isNotEmpty()) {

                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                                        Text(
                                            "Variants",
                                            fontFamily = PoppinsFont,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        state.variants.forEach { group ->

                                            Column {

                                                Text(
                                                    group.name,
                                                    fontFamily = PoppinsFont,
                                                    fontWeight = FontWeight.Medium
                                                )

                                                Spacer(Modifier.height(4.dp))

                                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                    group.options.forEach { option ->
                                                        Box(
                                                            modifier = Modifier
                                                                .clip(RoundedCornerShape(20.dp))
                                                                .background(AppColor.BlueSoft)
                                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                                        ) {
                                                            Text(
                                                                "${option.name} (+${formatRupiah(option.price)})",
                                                                fontFamily = PoppinsFont,
                                                                fontSize = 12.sp
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernStepper(current: ProductStep) {

    val progress =
        (current.ordinal + 1).toFloat() / ProductStep.entries.size

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text(
            fontFamily = PoppinsFont,
            text = "Step ${current.ordinal + 1} of ${ProductStep.entries.size}",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            fontSize = 14.sp
        )

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = AppColor.Blue
        )

        Text(
            fontFamily = PoppinsFont,
            text = current.name,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        fontFamily = PoppinsFont,
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = Color.Gray,
        fontSize = 14.sp
    )
}

@Composable
fun ModernCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}

@Composable
fun AvailabilitySelector(
    selected: Availability,
    onSelect: (Availability) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Availability.entries.forEach {
            FilterChip(
                selected = selected == it,
                onClick = { onSelect(it) },
                label = {
                    Text(
                        fontFamily = PoppinsFont,
                        text = it.name
                    )
                }
            )
        }
    }
}

@Composable
fun VariantGroupCard(
    group: VariantGroup,
    groupIndex: Int,
    onEvent: (SellerProductFormEvent) -> Unit
) {

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        ModernOutlinedField(
            value = group.name,
            onValueChange = {
                onEvent(
                    SellerProductFormEvent.ChangeVariantGroupName(groupIndex, it)
                )
            },
            label = "Variant Group Name",
            icon = Icons.Default.Inventory2,
            modifier = Modifier.fillMaxWidth()
        )

        group.options.forEachIndexed { optionIndex, option ->

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                ModernOutlinedField(
                    value = option.name,
                    onValueChange = {
                        onEvent(
                            SellerProductFormEvent.ChangeVariantOptionName(
                                groupIndex,
                                optionIndex,
                                it
                            )
                        )
                    },
                    label = "Option",
                    modifier = Modifier.weight(1f)
                )

                ModernOutlinedField(
                    value = option.price,
                    onValueChange = {
                        onEvent(
                            SellerProductFormEvent.ChangeVariantOptionPrice(
                                groupIndex,
                                optionIndex,
                                it.filter { c -> c.isDigit() }
                            )
                        )
                    },
                    label = "+ Price",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        TextButton(onClick = {
            onEvent(
                SellerProductFormEvent.AddVariantOption(groupIndex)
            )
        }) {
            Text(
                text = "+ Add Option",
                fontFamily = PoppinsFont
            )
        }
    }
}

fun formatRupiah(value: String): String {
    if (value.isEmpty()) return "Rp 0"
    return "Rp %,d".format(value.toLong()).replace(',', '.')
}

@Composable
fun ModernOutlinedField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = singleLine,
        shape = RoundedCornerShape(14.dp),
        label = {
            Text(
                text = label,
                fontFamily = PoppinsFont
            )
        },
        leadingIcon = icon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = AppColor.Blue
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColor.Blue,
            unfocusedBorderColor = Color(0xFFE5E7EB),
            focusedContainerColor = Color(0xFFF8FAFC),
            unfocusedContainerColor = Color(0xFFF8FAFC),
            cursorColor = AppColor.Blue
        )
    )
}

@Preview(name = "Step 1 - Basic", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun PreviewStepBasic() {
    MaterialTheme {
        SellerProductFormScreen(
            state = SellerProductFormUiState(
                step = ProductStep.BASIC,
                product = ProductItem(
                    name = "Nasi Goreng",
                    description = "Enak banget",
                    price = "20000",
                    stock = 10
                ),
                isActive = true,
                availability = Availability.READY,
                images = List(5) { null }
            ),
            event = {}
        )
    }
}

@Preview(name = "Step 2 - Pricing", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun PreviewStepPricing() {
    MaterialTheme {
        SellerProductFormScreen(
            state = SellerProductFormUiState(
                step = ProductStep.PRICING,
                product = ProductItem(
                    name = "Nasi Goreng",
                    description = "Enak banget",
                    price = "20000",
                    stock = 10
                ),
                isActive = true,
                availability = Availability.READY,
                images = List(5) { null }
            ),
            event = {}
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(name = "Step 3 - Variant", showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun PreviewStepVariant() {
    MaterialTheme {
        SellerProductFormScreen(
            state = SellerProductFormUiState(
                step = ProductStep.VARIANT,
                product = ProductItem(
                    name = "Nasi Goreng",
                    description = "Enak banget",
                    price = "20000",
                    stock = 10
                ),
                isActive = true,
                availability = Availability.READY,
                images = listOf(null, null, null, null, null)
            ),
            event = {}
        )
    }
}

@Preview(
    name = "Step 4 - Review",
    showBackground = true,
    device = Devices.PIXEL_4_XL
)
@Composable
fun PreviewStepReview() {
    MaterialTheme {
        SellerProductFormScreen(
            state = SellerProductFormUiState(
                step = ProductStep.REVIEW,
                product = ProductItem(
                    name = "Nasi Goreng",
                    description = "Enak banget",
                    price = "20000",
                    stock = 10
                ),
                isActive = true,
                availability = Availability.READY,
                images = List(5) { null }
            ),
            event = {}
        )
    }
}
