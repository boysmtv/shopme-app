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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mtv.app.shopme.common.AppColor
import com.mtv.app.shopme.common.ContentErrorState
import com.mtv.app.shopme.domain.model.SellerDiscount
import com.mtv.app.shopme.feature.seller.contract.SellerDiscountEvent
import com.mtv.app.shopme.feature.seller.contract.SellerDiscountUiState

@Composable
fun SellerDiscountScreen(
    state: SellerDiscountUiState,
    event: (SellerDiscountEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { event(SellerDiscountEvent.ClickAdd) },
                containerColor = AppColor.Blue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Promo & Diskon",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColor.Blue)
                }
            } else if (!state.errorMessage.isNullOrBlank() && state.discounts.isEmpty()) {
                ContentErrorState(
                    title = "Gagal memuat promo",
                    message = state.errorMessage,
                    actionLabel = "Muat ulang",
                    onRetry = { event(SellerDiscountEvent.Load) }
                )
            } else if (state.discounts.isEmpty()) {
                EmptyDiscountState(onAddClick = { event(SellerDiscountEvent.ClickAdd) })
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.discounts, key = { it.id }) { discount ->
                        DiscountCard(
                            discount = discount,
                            onEdit = { event(SellerDiscountEvent.ClickEdit(discount)) },
                            onDelete = { event(SellerDiscountEvent.ClickDelete(discount.id)) }
                        )
                    }
                }
            }
        }
    }

    if (state.showFormDialog) {
        DiscountFormDialog(
            state = state,
            onEvent = event
        )
    }
}

@Composable
private fun DiscountCard(
    discount: SellerDiscount,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = discount.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    if (discount.isActive) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Active",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Text(
                            text = "Inactive",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = if (discount.type.name == "PERCENTAGE") "${discount.value}% OFF"
                           else "Rp ${discount.value} OFF",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${discount.startDate} - ${discount.endDate}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                if (!discount.minOrder.isNullOrBlank()) {
                    Text(
                        text = "Min. order: Rp ${discount.minOrder}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Column {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
private fun EmptyDiscountState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(AppColor.Blue.copy(alpha = 0.08f), RoundedCornerShape(60.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = "Discounts",
                tint = AppColor.Blue,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "No Promotions Yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Create discounts and promotions\nto attract more customers.",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(AppColor.Blue)
        ) {
            Text("Create Promotion", color = Color.White)
        }
    }
}

@Composable
private fun DiscountFormDialog(
    state: SellerDiscountUiState,
    onEvent: (SellerDiscountEvent) -> Unit
) {
    val isEditing = state.editingDiscount != null

    AlertDialog(
        onDismissRequest = { onEvent(SellerDiscountEvent.ClickCancelForm) },
        title = { Text(if (isEditing) "Edit Promo" else "New Promotion") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = state.formName,
                    onValueChange = { onEvent(SellerDiscountEvent.ChangeFormName(it)) },
                    label = { Text("Discount Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(
                        onClick = { onEvent(SellerDiscountEvent.ChangeFormType("PERCENTAGE")) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Percentage",
                            color = if (state.formType == "PERCENTAGE") AppColor.Blue else Color.Gray,
                            fontWeight = if (state.formType == "PERCENTAGE") FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    TextButton(
                        onClick = { onEvent(SellerDiscountEvent.ChangeFormType("FIXED")) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Fixed",
                            color = if (state.formType == "FIXED") AppColor.Blue else Color.Gray,
                            fontWeight = if (state.formType == "FIXED") FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                OutlinedTextField(
                    value = state.formValue,
                    onValueChange = { onEvent(SellerDiscountEvent.ChangeFormValue(it)) },
                    label = { Text(if (state.formType == "PERCENTAGE") "Value (%)" else "Value (Rp)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.formMinOrder,
                    onValueChange = { onEvent(SellerDiscountEvent.ChangeFormMinOrder(it)) },
                    label = { Text("Min Order (optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.formMaxDiscount,
                    onValueChange = { onEvent(SellerDiscountEvent.ChangeFormMaxDiscount(it)) },
                    label = { Text("Max Discount (optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.formStartDate,
                    onValueChange = { onEvent(SellerDiscountEvent.ChangeFormStartDate(it)) },
                    label = { Text("Start Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.formEndDate,
                    onValueChange = { onEvent(SellerDiscountEvent.ChangeFormEndDate(it)) },
                    label = { Text("End Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Active", fontSize = 14.sp)
                    Switch(
                        checked = state.formIsActive,
                        onCheckedChange = { onEvent(SellerDiscountEvent.ChangeFormIsActive(it)) }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onEvent(SellerDiscountEvent.SubmitForm) },
                enabled = !state.isSubmitting
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(if (isEditing) "Update" else "Create")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(SellerDiscountEvent.ClickCancelForm) }) {
                Text("Cancel")
            }
        }
    )
}
