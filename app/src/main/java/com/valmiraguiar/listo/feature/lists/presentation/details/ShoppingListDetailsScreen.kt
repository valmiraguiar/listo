package com.valmiraguiar.listo.feature.lists.presentation.details

import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.common.theme.ListoTheme
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import com.valmiraguiar.listo.feature.lists.presentation.details.state.ShoppingListDetailsItemUiState
import com.valmiraguiar.listo.feature.lists.presentation.details.state.ShoppingListDetailsUiAction
import com.valmiraguiar.listo.feature.lists.presentation.details.state.ShoppingListDetailsUiResult
import com.valmiraguiar.listo.feature.lists.presentation.details.state.ShoppingListDetailsUiState

@Composable
fun ShoppingListDetailsRoute(
    listId: Long,
    onEditListClickNavigate: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShoppingListDetailsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiResult) {
        viewModel.uiResult.collect { result ->
            when (result) {
                is ShoppingListDetailsUiResult.OnEditListNavigate -> onEditListClickNavigate(listId)
                is ShoppingListDetailsUiResult.OnError -> Unit
                is ShoppingListDetailsUiResult.OnLoading -> Unit
                is ShoppingListDetailsUiResult.OnNavigateBack -> Unit
                is ShoppingListDetailsUiResult.OnShowListDetails -> Unit
                is ShoppingListDetailsUiResult.OnShowListNotFound -> Unit
            }
        }
    }

    ShoppingListDetailsScreen(
        listId = listId,
        uiState = uiState,
        onUiEvent = viewModel::dispatch,
        modifier = modifier,
    )
}

@Composable
fun ShoppingListDetailsScreen(
    listId: Long,
    uiState: ShoppingListDetailsUiState,
    onUiEvent: (ShoppingListDetailsUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(listId) {
        onUiEvent(ShoppingListDetailsUiAction.FetchListDetails(listId))
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.isNotFound -> {
                EmptyDetailsMessage(
                    text = stringResource(
                        id = R.string.shopping_list_details_not_found,
                        uiState.listId,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            uiState.items.isEmpty() -> {
                EmptyDetailsMessage(
                    text = stringResource(id = R.string.shopping_list_details_empty),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                ShoppingListDetailsContent(
                    title = uiState.title,
                    shoppingItems = uiState.items,
                    onCheckedChange = { item, isChecked ->
                        onUiEvent(
                            ShoppingListDetailsUiAction.ItemCheckedChange(
                                itemId = item.id,
                                isChecked = isChecked,
                            ),
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        if (!uiState.isLoading && !uiState.isNotFound) {
            ExtendedFloatingActionButton(
                onClick = dropUnlessResumed {
                    onUiEvent(ShoppingListDetailsUiAction.EditListClick)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                    )
                },
                text = {
                    Text(text = stringResource(id = R.string.shopping_list_details_edit))
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(16.dp),
            )
        }
    }
}

@Composable
private fun ShoppingListDetailsContent(
    title: String,
    shoppingItems: List<ShoppingListDetailsItemUiState>,
    onCheckedChange: (ShoppingListDetailsItemUiState, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 12.dp,
            end = 16.dp,
            bottom = 96.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(
            key = "shopping-list-title",
            contentType = "shopping-list-title",
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 12.dp,
                ),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        items(
            items = shoppingItems,
            key = { item -> item.id },
            contentType = { "shopping-list" },
        ) { item ->
            Column(
                modifier = Modifier.animateItem(
                    placementSpec = tween(durationMillis = ITEM_PLACEMENT_ANIMATION_DURATION),
                ),
            ) {
                ShoppingListItem(
                    item = item,
                    onCheckedChange = { isChecked ->
                        onCheckedChange(item, isChecked)
                    },
                )

                if (item != shoppingItems.last()) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA),
                    )
                }
            }
        }
    }
}

@Composable
private fun ShoppingListItem(
    item: ShoppingListDetailsItemUiState,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                value = item.isChecked,
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (item.isChecked) {
                    Icons.Filled.CheckCircle
                } else {
                    Icons.Outlined.Circle
                },
                contentDescription = null,
                tint = if (item.isChecked) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textDecoration = if (item.isChecked) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                },
            )
            Text(
                text = item.supportingText(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ShoppingListDetailsItemUiState.supportingText(): String {
    if (quantity.isBlank()) return classification

    return "${quantity.trim()} ${unit.label()} - $classification"
}

@Composable
private fun UnitEnum.label(): String {
    val stringId = when (this) {
        UnitEnum.Unit -> R.string.unit_un
        UnitEnum.Kilogram -> R.string.unit_kg
        UnitEnum.Gram -> R.string.unit_g
        UnitEnum.Liter -> R.string.unit_l
        UnitEnum.Milliliter -> R.string.unit_ml
        UnitEnum.Pack -> R.string.unit_pack
    }
    return stringResource(id = stringId)
}

@Composable
private fun EmptyDetailsMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private const val ITEM_PLACEMENT_ANIMATION_DURATION = 250
private const val DIVIDER_ALPHA = 0.35f

private val PREVIEW_ITEMS = listOf(
    ShoppingListDetailsItemUiState(id = 1L, title = "Arroz", classification = "Mercearia"),
    ShoppingListDetailsItemUiState(id = 2L, title = "Leite", classification = "Laticínios"),
    ShoppingListDetailsItemUiState(id = 3L, title = "Maçã", classification = "Hortifruti"),
    ShoppingListDetailsItemUiState(
        id = 4L,
        title = "Pão",
        classification = "Padaria",
        isChecked = true,
    ),
    ShoppingListDetailsItemUiState(
        id = 5L,
        title = "Sabonete",
        classification = "Higiene",
        isChecked = true,
    ),
)

@Preview(
    name = "Shopping list details",
    showBackground = true,
    backgroundColor = 0xFFF9F9F9,
)
@Composable
private fun ShoppingListDetailsScreenPreview() {
    ListoTheme(darkTheme = false) {
        ShoppingListDetailsContent(
            title = "Feira do mes",
            shoppingItems = PREVIEW_ITEMS,
            onCheckedChange = { _, _ -> },
        )
    }
}
