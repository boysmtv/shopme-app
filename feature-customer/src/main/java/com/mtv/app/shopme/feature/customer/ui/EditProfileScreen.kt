/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.base.BaseSimpleFormField
import com.mtv.app.shopme.feature.customer.contract.EditProfileDataListener
import com.mtv.app.shopme.feature.customer.contract.EditProfileEventListener
import com.mtv.app.shopme.feature.customer.contract.EditProfileNavigationListener
import com.mtv.app.shopme.feature.customer.contract.EditProfileStateListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class UserAddress(
    val id: String,
    var village: String,
    var block: String,
    var number: String,
    var rt: String,
    var rw: String,
    var map: String,
    var isDefault: Boolean = false
)

@Composable
fun EditProfileScreen(
    uiState: EditProfileStateListener,
    uiData: EditProfileDataListener,
    uiEvent: EditProfileEventListener,
    uiNavigation: EditProfileNavigationListener
) {

    var selectedTab by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf(uiData.name) }
    var phone by remember { mutableStateOf(uiData.phone) }
    var email by remember { mutableStateOf(uiData.email) }
    var showAddAddress by remember { mutableStateOf(false) }
    val sheetController = rememberSheetController()

    var addresses by remember {
        mutableStateOf(
            listOf(
                UserAddress("1", "Griya Asri", "A", "12", "01", "02", "https://maps", true)
            )
        )
    }

    val isValid = name.isNotBlank() && phone.length >= 10 && email.contains("@")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Green, AppColor.GreenSoft)
                )
            )
    ) {

        PremiumHeader(uiNavigation)

        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {

            Column(Modifier.padding(20.dp)) {

                AnimatedTab(selectedTab) {
                    selectedTab = it
                }

                Spacer(Modifier.height(24.dp))

                AnimatedContent(targetState = selectedTab) { tab ->
                    if (tab == 0) {
                        ProfileSection(
                            name,
                            phone,
                            email,
                            onNameChange = { name = it },
                            onPhoneChange = { phone = it },
                            onEmailChange = { email = it }
                        )
                    } else {
                        AddressSection(
                            addresses = addresses,
                            onAdd = { showAddAddress = true },
                            onDelete = { id ->
                                addresses = addresses.filterNot { it.id == id }
                            },
                            onSetDefault = { id ->
                                addresses = addresses.map {
                                    it.copy(isDefault = it.id == id)
                                }
                            }
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
//                        uiEvent.onSaveClicked(name, phone, email)
                        uiNavigation.onBack()
                    },
                    enabled = isValid,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColor.Green
                    )
                ) {
                    Text(
                        text = "Simpan Perubahan", color = Color.White,
                        fontFamily = PoppinsFont,
                    )
                }
            }
        }

        if (showAddAddress) {
            AddAddressSheet(
                controller = sheetController,
                onDismiss = { showAddAddress = false },
                onSave = { newAddress ->
                    addresses = addresses + newAddress
                    showAddAddress = false
                }
            )
        }
    }
}

@Composable
fun PremiumHeader(nav: EditProfileNavigationListener) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { nav.onBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }

            Text(
                text = "Edit Akun",
                fontFamily = PoppinsFont,
                fontSize = 22.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Upload", color = AppColor.Green,
                fontFamily = PoppinsFont,
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun AddressSection(
    addresses: List<UserAddress>,
    onAdd: () -> Unit,
    onDelete: (String) -> Unit,
    onSetDefault: (String) -> Unit
) {
    Column {
        addresses.forEach { address ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AppColor.GreenSoft)
            ) {

                Column(Modifier.padding(16.dp)) {

                    Text(
                        text = "${address.village} Blok ${address.block} No ${address.number}",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFont,
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "RT ${address.rt} / RW ${address.rw}",
                        fontFamily = PoppinsFont,
                    )

                    Spacer(Modifier.height(10.dp))

                    Row {
                        if (!address.isDefault) {
                            TextButton(onClick = {
                                onSetDefault(address.id)
                            }) {
                                Text(
                                    text = "Jadikan Default",
                                    fontFamily = PoppinsFont,
                                )
                            }
                        }

                        Spacer(Modifier.weight(1f))

                        TextButton(onClick = {
                            onDelete(address.id)
                        }) {
                            Text(
                                text = "Hapus",
                                color = Color.Red,
                                fontFamily = PoppinsFont,
                            )
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onAdd,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Tambah Alamat",
                fontFamily = PoppinsFont,
            )
        }
    }
}

@Composable
fun AnimatedTab(selected: Int, onChange: (Int) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(AppColor.GreenSoft)
    ) {

        listOf("Profil", "Alamat").forEachIndexed { index, title ->

            val active = selected == index

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .background(if (active) AppColor.Green else Color.Transparent)
                    .clickable { onChange(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = if (active) Color.White else AppColor.Green,
                    fontFamily = PoppinsFont,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressSheet(
    controller: SheetController,
    onDismiss: () -> Unit,
    onSave: (UserAddress) -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = controller.sheetState,
        dragHandle = {
            Box(
                Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Gray.copy(.4f))
            )
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        AddAddressSheetContentProduction(
            controller = controller,
            onSave = onSave
        )
    }
}

@Composable
fun AddAddressSheetContentProduction(
    controller: SheetController,
    onSave: (UserAddress) -> Unit
) {

    var village by remember { mutableStateOf("") }
    var block by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var rt by remember { mutableStateOf("") }
    var rw by remember { mutableStateOf("") }
    var map by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(20.dp)
    ) {

        // ===== HEADER =====
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Tambah Alamat",
                fontFamily = PoppinsFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Spacer(Modifier.weight(1f))

            TextButton(onClick = { controller.partial() }) {
                Text(
                    text = "Half",
                    fontFamily = PoppinsFont,
                )
            }

            TextButton(onClick = { controller.expand() }) {
                Text(
                    text = "Full",
                    fontFamily = PoppinsFont,
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // ===== MAP PICKER SLOT =====
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(AppColor.GreenSoft),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Map Picker (Google Maps)", color = AppColor.Green,
                fontFamily = PoppinsFont,
            )
        }

        Spacer(Modifier.height(16.dp))

        // ===== FORM =====
        BaseSimpleFormField("Nama Perumahan", village) { village = it }
        Spacer(Modifier.height(10.dp))

        Row {
            BaseSimpleFormField("Blok", block, modifier = Modifier.weight(1f)) { block = it }
            Spacer(Modifier.width(8.dp))
            BaseSimpleFormField(
                "No",
                number,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            ) { number = it }
        }

        Spacer(Modifier.height(10.dp))

        Row {
            BaseSimpleFormField(
                "RT",
                rt,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            ) { rt = it }

            Spacer(Modifier.width(8.dp))

            BaseSimpleFormField(
                "RW",
                rw,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            ) { rw = it }
        }

        Spacer(Modifier.height(10.dp))

        BaseSimpleFormField("Link Map / Catatan", map) { map = it }

        Spacer(Modifier.height(20.dp))

        // ===== SAVE =====
        Button(
            onClick = {
                onSave(
                    UserAddress(
                        id = System.currentTimeMillis().toString(),
                        village = village,
                        block = block,
                        number = number,
                        rt = rt,
                        rw = rw,
                        map = map
                    )
                )
                controller.hide()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = AppColor.Green)
        ) {
            Text(
                text = "Simpan Alamat", color = Color.White,
                fontFamily = PoppinsFont,
            )
        }
    }
}

@Composable
fun ProfileSection(
    name: String,
    phone: String,
    email: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {

    Column {
        BaseSimpleFormField(
            label = "Nama Lengkap",
            value = name
        ) { onNameChange(it) }

        Spacer(Modifier.height(16.dp))

        BaseSimpleFormField(
            label = "Nomor Telepon",
            value = phone,
            keyboardType = KeyboardType.Phone
        ) { onPhoneChange(it) }

        Spacer(Modifier.height(16.dp))

        BaseSimpleFormField(
            label = "Email",
            value = email,
            keyboardType = KeyboardType.Email
        ) { onEmailChange(it) }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Pastikan data kamu valid agar pesanan tidak gagal dikirim.",
            fontSize = 12.sp,
            color = AppColor.Gray,
            fontFamily = PoppinsFont
        )
    }
}

@Stable
class SheetController @OptIn(ExperimentalMaterial3Api::class) constructor(
    val sheetState: SheetState,
    private val scope: CoroutineScope
) {
    @OptIn(ExperimentalMaterial3Api::class)
    fun expand() = scope.launch { sheetState.expand() }

    @OptIn(ExperimentalMaterial3Api::class)
    fun partial() = scope.launch { sheetState.partialExpand() }

    @OptIn(ExperimentalMaterial3Api::class)
    fun hide() = scope.launch { sheetState.hide() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberSheetController(): SheetController {
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    return remember { SheetController(state, scope) }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(
        uiState = EditProfileStateListener(),
        uiData = EditProfileDataListener(),
        uiEvent = EditProfileEventListener(),
        uiNavigation = EditProfileNavigationListener()
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun EditProfileAddressTabPreview() {

    var selectedTab by remember { mutableIntStateOf(1) }

    val dummyAddresses = listOf(
        UserAddress("1", "Griya Asri", "A", "12", "01", "02", "https://maps", true),
        UserAddress("2", "Permata Indah", "B", "8", "03", "05", "https://maps", false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Green, AppColor.GreenSoft)
                )
            )
    ) {

        PremiumHeader(EditProfileNavigationListener())

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = AppColor.White)
        ) {

            Column(Modifier.padding(16.dp)) {

                AnimatedTab(selectedTab) { selectedTab = it }

                Spacer(Modifier.height(16.dp))

                AddressSection(
                    addresses = dummyAddresses,
                    onAdd = {},
                    onDelete = {},
                    onSetDefault = {}
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColor.Green)
                ) {
                    Text(
                        text = "Simpan Perubahan",
                        color = Color.White,
                        fontFamily = PoppinsFont,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun AddAddressSheetProductionPreview() {

    val controller = rememberSheetController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDDDDDD)),
        contentAlignment = Alignment.BottomCenter
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color.White)
        ) {
            AddAddressSheetContentProduction(
                controller = controller,
                onSave = {}
            )
        }
    }
}