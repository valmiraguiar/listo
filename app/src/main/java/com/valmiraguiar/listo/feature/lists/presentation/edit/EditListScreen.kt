package com.valmiraguiar.listo.feature.lists.presentation.edit

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.common.components.UnderlinedTextField
import com.valmiraguiar.listo.feature.common.theme.BackgroundVariant
import com.valmiraguiar.listo.feature.common.theme.ListoTheme
import com.valmiraguiar.listo.feature.lists.domain.model.CategoryEnum
import com.valmiraguiar.listo.feature.lists.domain.model.Product
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import com.valmiraguiar.listo.feature.lists.presentation.label
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiAction
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiResult
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiState

private const val ITEM_PLACEMENT_ANIMATION_DURATION_MILLIS = 300
private const val SCROLL_ANIMATION_DURATION_MILLIS = 450

@Composable
fun EditListRoute(
    listId: Long?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(listId) {
        viewModel.dispatch(EditListUiAction.OpenList(listId))
    }

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
    val listState = rememberLazyListState()
    var focusedItemId by remember { mutableStateOf<Long?>(null) }
    var shouldFocusAddedItem by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.shoppingList?.products?.size) {
        if (shouldFocusAddedItem && uiState.shoppingList?.products?.isNotEmpty() == true) {
            focusedItemId = uiState.shoppingList.products.last().id
            listState.animateScrollToLastItem()
            shouldFocusAddedItem = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = BackgroundVariant),
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                EditListContent(
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                    listState = listState,
                    focusedItemId = focusedItemId,
                    onItemFocused = { focusedItemId = null },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        if (!uiState.isLoading) {
            EditListFloatingActions(
                canSave = uiState.canSave,
                onAddItem = {
                    shouldFocusAddedItem = true
                    onUiEvent(EditListUiAction.AddItemClick)
                },
                onSaveList = {
                    onUiEvent(EditListUiAction.SaveListClick)
                },
                modifier = Modifier.align(Alignment.BottomEnd),
            )
        }
    }
}

@Composable
private fun EditListContent(
    uiState: EditListUiState,
    onUiEvent: (EditListUiAction) -> Unit,
    listState: LazyListState,
    focusedItemId: Long?,
    onItemFocused: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 144.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(
            key = "list_name",
            contentType = "list_name",
        ) {
            UnderlinedTextField(
                value = uiState.shoppingList?.title.orEmpty(),
                onValueChange = {
                    onUiEvent(
                        EditListUiAction.ListNameChange(
                            listName = it,
                        ),
                    )
                },
                label = stringResource(R.string.edit_list_name),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }

        if(uiState.shoppingList?.products != null)
        items(
            items = uiState.shoppingList.products,
            key = { item -> item.id },
            contentType = { "edit_list_item" },
        ) { item ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(
                        placementSpec = tween(
                            durationMillis = ITEM_PLACEMENT_ANIMATION_DURATION_MILLIS,
                        ),
                    )
                    .background(color = MaterialTheme.colorScheme.surface),
            ) {
                EditListItem(
                    item = item,
                    requestDescriptionFocus = item.id == focusedItemId,
                    onDescriptionChange = { description ->
                        onUiEvent(
                            EditListUiAction.ItemDescriptionChange(
                                itemId = item.id,
                                description = description,
                            ),
                        )
                    },
                    onQuantityChange = { quantity ->
                        onUiEvent(
                            EditListUiAction.ItemQuantityChange(
                                itemId = item.id,
                                quantity = quantity,
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
                    onRemoveItem = {
                        onUiEvent(EditListUiAction.RemoveItemClick(itemId = item.id))
                    },
                    onDescriptionFocused = onItemFocused,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun EditListItem(
    item: Product,
    requestDescriptionFocus: Boolean,
    onDescriptionChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onCategorySelected: (CategoryEnum) -> Unit,
    onRemoveItem: () -> Unit,
    onDescriptionFocused: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val descriptionFocusRequester = remember { FocusRequester() }

    LaunchedEffect(requestDescriptionFocus) {
        if (requestDescriptionFocus) {
            withFrameNanos { }
            descriptionFocusRequester.requestFocus()
            onDescriptionFocused()
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UnderlinedTextField(
                value = item.description,
                onValueChange = onDescriptionChange,
                label = stringResource(id = R.string.create_list_item_description_placeholder),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(descriptionFocusRequester),
            )

            IconButton(
                onClick = dropUnlessResumed(block = onRemoveItem),
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.edit_list_remove_item),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UnderlinedTextField(
                value = item.quantity,
                onValueChange = onQuantityChange,
                label = stringResource(id = R.string.create_list_quantity_placeholder),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
            )

            CategoryDropdown(
                selected = item.category,
                onCategorySelected = onCategorySelected,
                modifier = Modifier
                    .weight(1f)
                    .widthIn(min = 128.dp),
            )
        }
    }
}

private suspend fun LazyListState.animateScrollToLastItem() {
    withFrameNanos { }

    val lastItemIndex = layoutInfo.totalItemsCount - 1
    if (lastItemIndex < 0) return

    val lastVisibleItem = layoutInfo.visibleItemsInfo.firstOrNull { item ->
        item.index == lastItemIndex
    }

    val usableViewportEnd = layoutInfo.viewportEndOffset - layoutInfo.afterContentPadding
    val scrollDistance = lastVisibleItem?.let { item ->
        item.offset + item.size - usableViewportEnd
    }

    when {
        scrollDistance == null -> animateScrollToItem(lastItemIndex)
        scrollDistance > 0 -> animateScrollBy(
            value = scrollDistance.toFloat(),
            animationSpec = tween(
                durationMillis = SCROLL_ANIMATION_DURATION_MILLIS,
                easing = FastOutSlowInEasing,
            ),
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
            .padding(16.dp)
            .fillMaxWidth(),
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
            value = selected.label(),
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
                        Text(text = category.label())
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
                shoppingList =
                    ShoppingList(
                        id = 1L,
                        title = "Lista1",
                        products = listOf(
                            Product(
                                id = 1L,
                                description = "Arroz",
                                quantity = "2",
                                category = CategoryEnum.Grocery,
                                unit = UnitEnum.Unit,
                            ),
                            Product(
                                id = 2L,
                                description = "Leite",
                                quantity = "1",
                                category = CategoryEnum.Dairy,
                                unit = UnitEnum.Unit,
                            ),
                        ),
                        createdAt = 1786048252969L
                    )
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
