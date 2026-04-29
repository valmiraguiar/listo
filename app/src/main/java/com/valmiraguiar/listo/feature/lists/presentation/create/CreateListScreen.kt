package com.valmiraguiar.listo.feature.lists.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.lists.domain.model.ListCategory
import com.valmiraguiar.listo.feature.lists.domain.model.UnitOption
import com.valmiraguiar.listo.ui.theme.ListoTheme

@Composable
fun CreateListRoute(
    onBack: () -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: CreateListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val successMessage = stringResource(id = R.string.create_list_saved_message)

    LaunchedEffect(uiState.saveConfirmationVisible) {
        if (uiState.saveConfirmationVisible) {
            snackbarHostState.showSnackbar(
                message = successMessage,
            )
            viewModel.dismissSaveConfirmation()
        }
    }

    CreateListScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onListTitleChange = viewModel::updateListTitle,
        onQuantityChange = viewModel::updateItemQuantity,
        onUnitSelected = viewModel::updateItemUnit,
        onDescriptionChange = viewModel::updateItemDescription,
        onCategorySelected = viewModel::updateItemCategory,
        onAddItem = viewModel::addItem,
        onRemoveItem = viewModel::removeItem,
        onCreateList = viewModel::createList,
        modifier = modifier,
    )
}

@Composable
fun CreateListScreen(
    uiState: CreateListUiState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Boolean,
    onListTitleChange: (String) -> Unit,
    onQuantityChange: (Long, String) -> Unit,
    onUnitSelected: (Long, UnitOption) -> Unit,
    onDescriptionChange: (Long, String) -> Unit,
    onCategorySelected: (Long, ListCategory) -> Unit,
    onAddItem: () -> Unit,
    onRemoveItem: (Long) -> Unit,
    onCreateList: () -> Unit,
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
            ) {
                CreateListTopBar(onBack = onBack)
                Spacer(modifier = Modifier.height(28.dp))
                UnderlinedTextField(
                    value = uiState.listTitle,
                    onValueChange = onListTitleChange,
                    label = stringResource(id = R.string.create_list_name_label),
                    placeholder = stringResource(id = R.string.create_list_name_placeholder),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))
                LabeledSection(title = stringResource(id = R.string.create_list_items_label)) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                    ) {
                        uiState.items.forEachIndexed { index, item ->
                            ItemEditorCard(
                                index = index + 1,
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
                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = dropUnlessResumed(block = onAddItem),
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.create_list_add_item),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = dropUnlessResumed(block = onCreateList),
                    enabled = uiState.canSubmit,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(999.dp),
                    contentPadding = PaddingValues(horizontal = 30.dp, vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f),
                        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.create_list_cta),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

@Composable
private fun CreateListTopBar(
    onBack: () -> Boolean,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = dropUnlessResumed {
                onBack()
            },
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.action_back),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            text = stringResource(id = R.string.create_list_topbar_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun LabeledSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(10.dp))
        content()
    }
}

@Composable
private fun ItemEditorCard(
    index: Int,
    item: DraftListItemUiState,
    onQuantityChange: (String) -> Unit,
    onUnitSelected: (UnitOption) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategorySelected: (ListCategory) -> Unit,
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

                IconButton(onClick = { onRemoveItem(item.id) }, modifier = Modifier) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.action_back),
                        tint = Color.Red,
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
                ListCategory.entries.forEach { category ->
                    FilterChip(
                        selected = item.category == category,
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
    selected: UnitOption,
    onUnitSelected: (UnitOption) -> Unit,
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
            UnitOption.entries.forEach { unit ->
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
private fun UnderlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        readOnly = readOnly,
        singleLine = singleLine,
        textStyle = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
        ),
        keyboardOptions = keyboardOptions,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 28.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                    ) {
                        if (value.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.42f),
                            )
                        }
                        innerTextField()
                    }
                    if (trailingContent != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        trailingContent()
                    }
                }
                HorizontalDivider(
                    color = if (isFocused) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.9f)
                    },
                    thickness = 1.dp,
                )
            }
        },
    )
}

@Composable
private fun UnitOption.labelRes(): Int = when (this) {
    UnitOption.Unit -> R.string.unit_un
    UnitOption.Kilogram -> R.string.unit_kg
    UnitOption.Gram -> R.string.unit_g
    UnitOption.Liter -> R.string.unit_l
    UnitOption.Milliliter -> R.string.unit_ml
    UnitOption.Pack -> R.string.unit_pack
}

@Composable
private fun ListCategory.labelRes(): Int = when (this) {
    ListCategory.Beverages -> R.string.category_beverages
    ListCategory.Grocery -> R.string.category_grocery
    ListCategory.Dairy -> R.string.category_dairy
    ListCategory.Meat -> R.string.category_meat
}

@Preview(showBackground = true)
@Composable
private fun CreateListScreenPreview() {
    ListoTheme {
        CreateListScreen(
            uiState = CreateListUiState(
                listTitle = "Churrasco do fim de semana",
                items = listOf(
                    DraftListItemUiState(
                        id = 1L,
                        quantity = "2",
                        unit = UnitOption.Kilogram,
                        description = "Picanha",
                        category = ListCategory.Meat,
                    ),
                    DraftListItemUiState(
                        id = 2L,
                        quantity = "6",
                        unit = UnitOption.Unit,
                        description = "Refrigerante",
                        category = ListCategory.Beverages,
                    ),
                ),
            ),
            snackbarHostState = SnackbarHostState(),
            onBack = { true },
            onListTitleChange = {},
            onQuantityChange = { _, _ -> },
            onUnitSelected = { _, _ -> },
            onDescriptionChange = { _, _ -> },
            onCategorySelected = { _, _ -> },
            onAddItem = {},
            onCreateList = {},
            onRemoveItem = {}
        )
    }
}
