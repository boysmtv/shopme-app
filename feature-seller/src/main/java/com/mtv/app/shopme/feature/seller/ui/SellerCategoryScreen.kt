package com.mtv.app.shopme.feature.seller.ui

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
import com.mtv.app.shopme.domain.model.SellerCategory
import com.mtv.app.shopme.feature.seller.contract.SellerCategoryEvent
import com.mtv.app.shopme.feature.seller.contract.SellerCategoryUiState

@Composable
fun SellerCategoryScreen(
    state: SellerCategoryUiState,
    event: (SellerCategoryEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { event(SellerCategoryEvent.ClickAdd) },
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
                text = "Categories",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColor.Blue)
                }
            } else if (!state.errorMessage.isNullOrBlank() && state.categories.isEmpty()) {
                ContentErrorState(
                    title = "Gagal memuat kategori",
                    message = state.errorMessage,
                    actionLabel = "Muat ulang",
                    onRetry = { event(SellerCategoryEvent.Load) }
                )
            } else if (state.categories.isEmpty()) {
                EmptyCategoryState(onAddClick = { event(SellerCategoryEvent.ClickAdd) })
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.categories, key = { it.id }) { category ->
                        CategoryCard(
                            category = category,
                            onEdit = { event(SellerCategoryEvent.ClickEdit(category)) },
                            onDelete = { event(SellerCategoryEvent.ClickDelete(category.id)) }
                        )
                    }
                }
            }
        }
    }

    if (state.showFormDialog) {
        CategoryFormDialog(
            state = state,
            onEvent = event
        )
    }
}

@Composable
private fun CategoryCard(
    category: SellerCategory,
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
                        text = category.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    if (category.isActive) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Active",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Text("Inactive", fontSize = 11.sp, color = Color.Gray)
                    }
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
private fun EmptyCategoryState(onAddClick: () -> Unit) {
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
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = "Categories",
                tint = AppColor.Blue.copy(alpha = 0.4f),
                modifier = Modifier.size(48.dp)
            )
        }

        Text(
            text = "No Categories Yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Create categories to organize\nyour products.",
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
            Text("Create Category", color = Color.White)
        }
    }
}

@Composable
private fun CategoryFormDialog(
    state: SellerCategoryUiState,
    onEvent: (SellerCategoryEvent) -> Unit
) {
    val isEditing = state.editingCategory != null

    AlertDialog(
        onDismissRequest = { onEvent(SellerCategoryEvent.ClickCancelForm) },
        title = { Text(if (isEditing) "Edit Category" else "New Category") },
        text = {
            OutlinedTextField(
                value = state.formName,
                onValueChange = { onEvent(SellerCategoryEvent.ChangeFormName(it)) },
                label = { Text("Category Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { onEvent(SellerCategoryEvent.SubmitForm) },
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
            TextButton(onClick = { onEvent(SellerCategoryEvent.ClickCancelForm) }) {
                Text("Cancel")
            }
        }
    )
}
