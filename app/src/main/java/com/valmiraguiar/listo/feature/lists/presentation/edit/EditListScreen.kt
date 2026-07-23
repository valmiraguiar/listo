package com.valmiraguiar.listo.feature.lists.presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.common.components.UnderlinedTextField
import com.valmiraguiar.listo.feature.common.theme.ListoTheme
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListItemUiState
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiAction
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiResult
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiState
import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum

private const val DIVIDER_ALPHA = 0.35f

@Composable
fun EditListRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiResult) {
        viewModel.uiResult.collect { result ->
            when (result) {
                is EditListUiResult.OnError -> Unit
                is EditListUiResult.OnListSaved -> onBack()
                is EditListUiResult.OnNavigateBack -> onBack()
            }
        }
    }

    EditListScreen(
        uiState = uiState,
        onUiEvent = viewModel::dispatch,
        modifier = modifier,
    )
}

@Composable
fun EditListScreen(
    uiState: EditListUiState,
    onUiEvent: (EditListUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        when {
            uiState.items.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.edit_list_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            else -> {
                EditListContent(
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        EditListFloatingActions(
            canSave = uiState.canSave,
            onAddItem = {
                onUiEvent(EditListUiAction.AddItemClick)
            },
            onSaveList = {
                onUiEvent(EditListUiAction.SaveListClick)
            },
            modifier = Modifier.align(Alignment.BottomEnd),
        )
    }
}

@Composable
private fun EditListContent(
    uiState: EditListUiState,
    onUiEvent: (EditListUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 12.dp,
            end = 16.dp,
            bottom = 144.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = uiState.items,
            key = { item -> item.id },
            contentType = { "edit_list_item" },
        ) { item ->
            EditListItem(
                item = item,
                onDescriptionChange = { description ->
                    onUiEvent(
                        EditListUiAction.ItemDescriptionChange(
                            itemId = item.id,
                            description = description,
                        ),
                    )
                },
                onCategorySelected = { category ->
                    onUiEvent(
                        EditListUiAction.ItemCategoryChange(
                            itemId = item.id,
                            categoryEnum = category,
                        ),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            if (item != uiState.items.last()) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA),
                )
            }
        }
    }
}

@Composable
private fun EditListItem(
    item: EditListItemUiState,
    onDescriptionChange: (String) -> Unit,
    onCategorySelected: (CategoryEnum) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(start = 18.dp, top = 16.dp, end = 18.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        UnderlinedTextField(
            value = item.description,
            onValueChange = onDescriptionChange,
            label = stringResource(id = R.string.create_list_item_description_placeholder),
            modifier = Modifier,
        )

        CategoryDropdown(
            selected = item.categoryEnum,
            onCategorySelected = onCategorySelected,
            modifier = Modifier.widthIn(min = 128.dp, max = 180.dp),
        )
    }
}

@Composable
private fun EditListFloatingActions(
    canSave: Boolean,
    onAddItem: () -> Unit,
    onSaveList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ExtendedFloatingActionButton(
            onClick = dropUnlessResumed(block = onAddItem),
            icon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            },
            text = {
                Text(text = stringResource(id = R.string.create_list_add_item))
            },
            modifier = Modifier.weight(1f)
        )

        ExtendedFloatingActionButton(
            onClick = dropUnlessResumed {
                if (canSave) onSaveList()
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = null,
                )
            },
            text = {
                Text(text = stringResource(id = R.string.edit_list_save))
            },
            containerColor = if (canSave) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (canSave) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    selected: CategoryEnum,
    onCategorySelected: (CategoryEnum) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        UnderlinedTextField(
            value = selected.name,
            onValueChange = {},
            label = stringResource(id = R.string.create_list_category_label),
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
            CategoryEnum.entries.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(text = category.name)
                    },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditListScreenPreview() {
    ListoTheme {
        EditListScreen(
            uiState = EditListUiState(
                items = listOf(
                    EditListItemUiState(
                        id = 1L,
                        description = "Arroz",
                        categoryEnum = CategoryEnum.Grocery,
                    ),
                    EditListItemUiState(
                        id = 2L,
                        description = "Leite",
                        categoryEnum = CategoryEnum.Dairy,
                    ),
                ),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyEditListScreenPreview() {
    ListoTheme {
        EditListScreen(
            uiState = EditListUiState(),
            onUiEvent = {},
        )
    }
}
