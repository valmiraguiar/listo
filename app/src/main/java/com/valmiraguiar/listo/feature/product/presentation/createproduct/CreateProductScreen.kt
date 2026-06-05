package com.valmiraguiar.listo.feature.product.presentation.createproduct

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.common.components.UnderlinedTextField
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum
import com.valmiraguiar.listo.ui.theme.ListoTheme

@Composable
fun CreateProductScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateProductScreen(
        uiState = uiState,
        onQuantityChange = viewModel::updateItemQuantity,
        onUnitSelected = viewModel::updateItemUnit,
        onDescriptionChange = viewModel::updateItemDescription,
        onCategorySelected = viewModel::updateItemCategory,
        onAddItem = viewModel::addItem,
        onRemoveItem = viewModel::removeItem,
        modifier = modifier
    )
}

@Composable
fun CreateProductScreen(
    uiState: CreateProductUiState,
    onQuantityChange: (Long, String) -> Unit,
    onUnitSelected: (Long, UnitEnum) -> Unit,
    onDescriptionChange: (Long, String) -> Unit,
    onCategorySelected: (Long, CategoryEnum) -> Unit,
    onAddItem: () -> Unit,
    onRemoveItem: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .navigationBarsPadding(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(top = 32.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                uiState.items.forEachIndexed { index, item ->
                    ItemEditorCard(
//                    index = index + 1,
                        item = item,
                        onQuantityChange = { quantity ->
                            onQuantityChange(item.id, quantity)
                        },
                        onUnitSelected = { unit ->
                            onUnitSelected(item.id, unit)
                        },
                        onDescriptionChange = { description ->
                            onDescriptionChange(item.id, description)
                        },
                        onCategorySelected = { category ->
                            onCategorySelected(item.id, category)
                        },
                        onRemoveItem = onRemoveItem
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = dropUnlessResumed(block = onAddItem),
                shape = RoundedCornerShape(18.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.create_list_add_item),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
private fun ItemEditorCard(
    item: DraftListItemUiState,
    onQuantityChange: (String) -> Unit,
    onUnitSelected: (UnitEnum) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategorySelected: (CategoryEnum) -> Unit,
    onRemoveItem: (Long) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    UnderlinedTextField(
                        value = item.quantity,
                        onValueChange = onQuantityChange,
                        label = stringResource(id = R.string.create_list_quantity_placeholder),
                        modifier = Modifier.width(88.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    )
                    UnitDropdown(
                        selected = item.unit,
                        onUnitSelected = onUnitSelected,
                        modifier = Modifier.width(110.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            UnderlinedTextField(
                value = item.description,
                onValueChange = onDescriptionChange,
                label = stringResource(id = R.string.create_list_item_description_placeholder),
                modifier = Modifier,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.create_list_category_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryEnum.entries.forEach { category ->
                    FilterChip(
                        selected = item.categoryEnum == category,
                        onClick = { onCategorySelected(category) },
                        label = {
                            Text(text = stringResource(id = category.labelRes()))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitDropdown(
    selected: UnitEnum,
    onUnitSelected: (UnitEnum) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        UnderlinedTextField(
            value = stringResource(id = selected.labelRes()),
            onValueChange = {},
            label = stringResource(id = R.string.create_list_unit_label),
            readOnly = true,
            trailingContent = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true,
                )
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            UnitEnum.entries.forEach { unit ->
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = unit.labelRes()))
                    },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun UnitEnum.labelRes(): Int = when (this) {
    UnitEnum.Unit -> R.string.unit_un
    UnitEnum.Kilogram -> R.string.unit_kg
    UnitEnum.Gram -> R.string.unit_g
    UnitEnum.Liter -> R.string.unit_l
    UnitEnum.Milliliter -> R.string.unit_ml
    UnitEnum.Pack -> R.string.unit_pack
}

@Composable
private fun CategoryEnum.labelRes(): Int = when (this) {
    CategoryEnum.Beverages -> R.string.category_beverages
    CategoryEnum.Grocery -> R.string.category_grocery
    CategoryEnum.Dairy -> R.string.category_dairy
    CategoryEnum.Meat -> R.string.category_meat
}

@Preview(showBackground = true)
@Composable
private fun CreateProductScreenPreview() {
    ListoTheme {
        CreateProductScreen()
    }
}
