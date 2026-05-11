/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileScreen.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.customer.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.PoppinsFont
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.SmartImage
import com.mtv.app.shopme.common.base.BaseSimpleFormField
import com.mtv.app.shopme.common.uriToBase64
import com.mtv.app.shopme.data.mock.DataUiMock
import com.mtv.app.shopme.data.mock.DataUiMock.addresses
import com.mtv.app.shopme.data.mock.DataUiMock.customer
import com.mtv.app.shopme.data.mock.DataUiMock.villages
import com.mtv.app.shopme.domain.model.Address
import com.mtv.app.shopme.domain.model.Village
import com.mtv.app.shopme.feature.customer.contract.EditProfileDialog
import com.mtv.app.shopme.feature.customer.contract.EditProfileEvent
import com.mtv.app.shopme.feature.customer.contract.EditProfileUiState
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogCenterV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import com.mtv.based.uicomponent.core.component.loading.LoadingV2
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.OK_STRING
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    state: EditProfileUiState,
    event: (EditProfileEvent) -> Unit
) {

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddAddress by remember { mutableStateOf(false) }
    val sheetController = rememberSheetController()
    val context = LocalContext.current

    val customer = (state.customer as? LoadState.Success)?.data
    val addresses = (state.addresses as? LoadState.Success)?.data.orEmpty()
    val villages = (state.villages as? LoadState.Success)?.data.orEmpty()
    val isInitialLoading =
        ((state.customer is LoadState.Loading && customer == null) ||
                (state.addresses is LoadState.Loading && addresses.isEmpty()) ||
                (state.villages is LoadState.Loading && villages.isEmpty()))

    var name by remember { mutableStateOf(EMPTY_STRING) }
    var phone by remember { mutableStateOf(EMPTY_STRING) }
    var email by remember { mutableStateOf(EMPTY_STRING) }
    var photo by remember { mutableStateOf(EMPTY_STRING) }
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        val encoded = uri?.let { uriToBase64(context, it) }
        if (!encoded.isNullOrBlank()) {
            photo = "data:image/jpeg;base64,$encoded"
        }
    }


    val isValid = name.isNotBlank() && phone.length >= 10

    LaunchedEffect(customer) {
        if (name.isBlank()) {
            name = customer?.name.orEmpty()
            phone = customer?.phone.orEmpty()
            email = customer?.email.orEmpty()
            photo = customer?.photo.orEmpty()
        }
    }

    if (state.activeDialog is EditProfileDialog.SuccessUpdateProfile) {
        DialogCenterV1(
            state = DialogStateV1(
                type = DialogType.SUCCESS,
                title = stringResource(R.string.success),
                message = stringResource(R.string.successfully_update_profile),
                primaryButtonText = OK_STRING
            ),
            onDismiss = { event(EditProfileEvent.DismissActiveDialog) }
        )
    }

    if (isInitialLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingV2()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Green, AppColor.GreenSoft)
                )
            )
            .statusBarsPadding()
    ) {

        PremiumHeader(
            photo = photo,
            onBack = { event(EditProfileEvent.ClickBack) },
            onUpload = { photoPicker.launch("image/*") }
        )

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {

            Column(Modifier.padding(20.dp)) {

                AnimatedTab(selectedTab) { selectedTab = it }

                Spacer(Modifier.height(24.dp))

                AnimatedContent(
                    targetState = selectedTab,
                    label = "EditProfileTabAnimation"
                ) { tab ->

                    if (tab == 0) {

                        ProfileSection(
                            name = name,
                            phone = phone,
                            email = email,
                            onNameChange = { name = it },
                            onPhoneChange = { phone = it },
                            onEmailChange = { email = it }
                        )

                    } else {

                        AddressSection(
                            addresses = addresses,
                            onAdd = { showAddAddress = true },
                            onDelete = { id ->
                                event(EditProfileEvent.DeleteAddress(id))
                            },
                            onSetDefault = { id ->
                                event(EditProfileEvent.SetDefaultAddress(id))
                            }
                        )
                    }
                }

                if (selectedTab == 0) {
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = {
                            event(
                                EditProfileEvent.UpdateProfile(
                                    name = name,
                                    phone = phone,
                                    photo = photo
                                )
                            )
                        },
                        enabled = isValid,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColor.Green)
                    ) {
                        Text(
                            text = "Simpan Perubahan",
                            color = Color.White,
                            fontFamily = PoppinsFont
                        )
                    }
                }
            }
        }

        if (showAddAddress) {
            AddAddressSheet(
                controller = sheetController,
                villages = villages,
                onDismiss = { showAddAddress = false },
                onSave = { villageId, block, number, rt, rw, isDefault ->
                    event(
                        EditProfileEvent.AddAddress(
                            villageId,
                            block,
                            number,
                            rt,
                            rw,
                            isDefault
                        )
                    )
                    showAddAddress = false
                }
            )
        }
    }
}

@Composable
fun PremiumHeader(
    photo: String,
    onBack: () -> Unit,
    onUpload: () -> Unit
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }

            Text(
                text = "Edit Profile",
                fontFamily = PoppinsFont,
                fontSize = 22.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .clickable { onUpload() },
            contentAlignment = Alignment.Center
        ) {
            if (photo.isNotBlank()) {
                SmartImage(
                    model = photo,
                    contentDescription = "Profile Photo",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "Upload", color = AppColor.Green,
                    fontFamily = PoppinsFont,
                )
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun AddressSection(
    addresses: List<Address>,
    onAdd: () -> Unit,
    onDelete: (String) -> Unit,
    onSetDefault: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {

        items(addresses) { address ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(16.dp),
                border = if (address.isDefault)
                    BorderStroke(1.5.dp, Color(0xFF2E7D32))
                else null,
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (address.isDefault) Color(0xFFE8F5E9)
                        else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "${address.village} - Blok ${address.block}/${address.number}",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PoppinsFont,
                            modifier = Modifier.weight(1f)
                        )

                        if (address.isDefault) {

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color(0xFF2E7D32)
                            ) {
                                Text(
                                    text = "Default",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 4.dp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "RT ${address.rt} / RW ${address.rw}",
                        fontFamily = PoppinsFont,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (!address.isDefault) {
                            TextButton(
                                onClick = { onSetDefault(address.id) }
                            ) {
                                Text(
                                    text = "Set as Default",
                                    fontFamily = PoppinsFont
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        TextButton(
                            onClick = { onDelete(address.id) }
                        ) {
                            Text(
                                text = "Delete",
                                color = Color.Red,
                                fontFamily = PoppinsFont
                            )
                        }
                    }
                }
            }
        }

        item {

            OutlinedButton(
                onClick = onAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(50)
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Tambah Alamat",
                    fontFamily = PoppinsFont
                )
            }
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
    villages: List<Village>,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, Boolean) -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = controller.sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        LaunchedEffect(Unit) {
            controller.expand()
        }

        var selectedVillageId by rememberSaveable { mutableStateOf<String?>(null) }
        val selectedVillage = villages.firstOrNull { it.id == selectedVillageId }

        var block by rememberSaveable { mutableStateOf(EMPTY_STRING) }
        var number by rememberSaveable { mutableStateOf(EMPTY_STRING) }
        var rt by rememberSaveable { mutableStateOf(EMPTY_STRING) }
        var rw by rememberSaveable { mutableStateOf(EMPTY_STRING) }
        var isDefault by rememberSaveable { mutableStateOf(false) }

        val isValid =
            selectedVillage != null &&
                    block.isNotBlank() &&
                    number.isNotBlank() &&
                    rt.isNotBlank() &&
                    rw.isNotBlank()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(20.dp)
        ) {

            VillageDropdown(
                villages = villages,
                selectedVillage = selectedVillage,
                onSelected = { selectedVillageId = it.id }
            )

            Spacer(Modifier.height(16.dp))

            Row {
                BaseSimpleFormField(
                    label = "Blok",
                    value = block,
                    maxChar = 3,
                    modifier = Modifier.weight(1f)
                ) { block = it }

                Spacer(Modifier.width(12.dp))

                BaseSimpleFormField(
                    label = "No",
                    value = number,
                    maxChar = 3,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                ) { number = it }
            }

            Spacer(Modifier.height(16.dp))

            Row {

                BaseSimpleFormField(
                    label = "RT",
                    value = rt,
                    maxChar = 3,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                ) { rt = it }

                Spacer(Modifier.width(12.dp))

                BaseSimpleFormField(
                    label = "RW",
                    value = rw,
                    maxChar = 3,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                ) { rw = it }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = isDefault,
                        role = Role.Checkbox,
                        onValueChange = { isDefault = it }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = isDefault,
                    onCheckedChange = null
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Jadikan alamat utama",
                    fontFamily = PoppinsFont
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                enabled = isValid,
                onClick = {
                    onSave(
                        selectedVillage!!.id,
                        block,
                        number,
                        rt,
                        rw,
                        isDefault
                    )
                    controller.hide()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = AppColor.Green)
            ) {
                Text(
                    "Simpan Alamat",
                    color = Color.White,
                    fontFamily = PoppinsFont
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VillageDropdown(
    villages: List<Village>,
    selectedVillage: Village?,
    onSelected: (Village) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "dropdownRotation"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        BaseSimpleFormField(
            label = "Nama Perumahan",
            value = selectedVillage?.name ?: "",
            readOnly = true,
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth(),
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            }
        ) {}

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 250.dp)
        ) {

            villages.forEach { village ->

                DropdownMenuItem(
                    text = {
                        Text(
                            text = village.name,
                            fontFamily = PoppinsFont
                        )
                    },
                    onClick = {
                        onSelected(village)
                        expanded = false
                    }
                )

            }
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
            keyboardType = KeyboardType.Email,
            readOnly = true
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
        skipPartiallyExpanded = true
    )
    return remember {
        SheetController(state, scope)
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(
        state = EditProfileUiState(
            customer = LoadState.Success(customer()),
            addresses = LoadState.Success(addresses()),
            villages = LoadState.Success(villages())
        ),
        event = {}
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun EditProfileAddressTabPreview() {

    var selectedTab by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(AppColor.Green, AppColor.GreenSoft)
                )
            )
    ) {

        PremiumHeader(
            photo = EMPTY_STRING,
            onBack = {},
            onUpload = {}
        )

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(containerColor = AppColor.White)
        ) {

            Column(Modifier.padding(16.dp)) {

                AnimatedTab(selectedTab) { selectedTab = it }

                Spacer(Modifier.height(16.dp))

                AddressSection(
                    addresses = addresses(),
                    onAdd = {},
                    onDelete = {},
                    onSetDefault = {}
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun AddAddressSheetPreview() {

    val controller = rememberSheetController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED)),
        contentAlignment = Alignment.BottomCenter
    ) {

        AddAddressSheet(
            controller = controller,
            villages = villages(),
            onDismiss = {},
            onSave = { _, _, _, _, _, _ -> }
        )
    }
}
