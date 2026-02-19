/*
 * MasterProductScreen.kt
 * Clean Modern Marketplace Version
 */

package com.mtv.app.shopme.feature.seller.ui

import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont

/* ------------------------------------------------ */
/* -------------------- MODELS -------------------- */
/* ------------------------------------------------ */

enum class Availability { READY, PREORDER, JASTIP }
enum class ProductStep { BASIC, PRICING, VARIANT, REVIEW }

data class VariantGroupUi(
    var name: String = "",
    var options: SnapshotStateList<VariantOptionUi> = mutableStateListOf()
)

data class VariantOptionUi(
    var name: String = "",
    var price: String = ""
)

/* ------------------------------------------------ */
/* ------------------- SCREEN --------------------- */
/* ------------------------------------------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterProductScreen(
    onBack: () -> Unit = {}
) {

    var currentStep by remember { mutableStateOf(ProductStep.BASIC) }

    var productName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }
    var availability by remember { mutableStateOf(Availability.READY) }

    val variantGroups = remember { mutableStateListOf<VariantGroupUi>() }
    val maxImage = 5
    val imageUris = remember { mutableStateListOf<Uri?>(null, null, null, null, null) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult

        val firstEmptyIndex = imageUris.indexOfFirst { it == null }
        if (firstEmptyIndex != -1) {
            imageUris[firstEmptyIndex] = uri
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
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onBack) {
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
                                currentStep =
                                    ProductStep.entries[currentStep.ordinal - 1]
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
                            if (currentStep != ProductStep.REVIEW) {
                                currentStep =
                                    ProductStep.entries[currentStep.ordinal + 1]
                            }
                        },
                        shape = RoundedCornerShape(16.dp)
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
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 120.dp),
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
                                    imageUris.forEachIndexed { index, uri ->
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
                                                            imageUris[index] = null
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

                                OutlinedTextField(
                                    value = productName,
                                    onValueChange = { productName = it },
                                    label = { Text(fontFamily = PoppinsFont, text = "Product Name") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    label = { Text(fontFamily = PoppinsFont, text = "Description") },
                                    minLines = 3,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement =
                                        Arrangement.SpaceBetween,
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {
                                    Text(fontFamily = PoppinsFont, text = "Active Product")
                                    Switch(
                                        checked = isActive,
                                        onCheckedChange = { isActive = it }
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

                                OutlinedTextField(
                                    value = price,
                                    onValueChange = {
                                        price = it.filter { c -> c.isDigit() }
                                    },
                                    label = { Text(fontFamily = PoppinsFont, text = "Base Price") },
                                    visualTransformation =
                                        RupiahVisualTransformation(),
                                    keyboardOptions =
                                        KeyboardOptions(
                                            keyboardType =
                                                KeyboardType.Number
                                        ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = stock,
                                    onValueChange = { stock = it },
                                    label = { Text(fontFamily = PoppinsFont, text = "Stock") },
                                    keyboardOptions =
                                        KeyboardOptions(
                                            keyboardType =
                                                KeyboardType.Number
                                        ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                AvailabilitySelector(
                                    selected = availability,
                                    onSelect = { availability = it }
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

                                variantGroups.forEach { group ->
                                    VariantGroupCard(
                                        group = group,
                                        onAddOption = {
                                            group.options.add(
                                                VariantOptionUi()
                                            )
                                        }
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        variantGroups.add(
                                            VariantGroupUi()
                                        )
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
                        SectionTitle("Review")
                        Spacer(Modifier.height(8.dp))

                        ModernCard {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(fontFamily = PoppinsFont, text = "Name: $productName")
                                Text(fontFamily = PoppinsFont, text = "Price: ${formatRupiah(price)}")
                                Text(fontFamily = PoppinsFont, text = "Stock: $stock")
                                Text(fontFamily = PoppinsFont, text = "Status: ${availability.name}")
                                Text(fontFamily = PoppinsFont, text = "Variant Groups: ${variantGroups.size}")
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
                .height(6.dp)
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
            modifier = Modifier.padding(20.dp),
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
    group: VariantGroupUi,
    onAddOption: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        OutlinedTextField(
            value = group.name,
            onValueChange = { group.name = it },
            label = {
                Text(
                    fontFamily = PoppinsFont,
                    text = "Variant Group Name"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        group.options.forEach { option ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                OutlinedTextField(
                    value = option.name,
                    onValueChange = { option.name = it },
                    label = {
                        Text(
                            fontFamily = PoppinsFont,
                            text = "Option"
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = option.price,
                    onValueChange = {
                        option.price = it.filter { c -> c.isDigit() }
                    },
                    label = {
                        Text(
                            fontFamily = PoppinsFont,
                            text = "+ Price"
                        )
                    },
                    visualTransformation =
                        RupiahVisualTransformation(),
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        TextButton(onClick = onAddOption) {
            Text(
                fontFamily = PoppinsFont,
                text = "+ Add Option"
            )
        }
    }
}

/* ------------------------------------------------ */
/* ---------------- FORMATTER --------------------- */
/* ------------------------------------------------ */

fun formatRupiah(value: String): String {
    if (value.isEmpty()) return "Rp 0"
    return "Rp %,d".format(value.toLong()).replace(',', '.')
}

class RupiahVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        val digits = text.text.filter { it.isDigit() }
        if (digits.isEmpty())
            return TransformedText(
                AnnotatedString(""),
                OffsetMapping.Identity
            )

        val formatted =
            "Rp %,d".format(digits.toLong()).replace(',', '.')

        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int) =
                    formatted.length

                override fun transformedToOriginal(offset: Int) =
                    digits.length
            }
        )
    }
}

/* ------------------------------------------------ */
/* ------------------- PREVIEW -------------------- */
/* ------------------------------------------------ */

@Preview(showBackground = true)
@Composable
fun PreviewMasterProductScreen() {
    MaterialTheme {
        MasterProductScreen()
    }
}
