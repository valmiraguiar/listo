package com.valmiraguiar.listo.feature.lists.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.common.components.LaunchOnce
import com.valmiraguiar.listo.feature.common.theme.ListoTheme
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListSummary
import com.valmiraguiar.listo.feature.lists.presentation.list.state.ShoppingListUiResult
import com.valmiraguiar.listo.feature.lists.presentation.list.state.ShoppingListsUiAction
import com.valmiraguiar.listo.feature.lists.presentation.list.state.ShoppingListsUiState
import java.text.DateFormat
import java.util.Date

private const val DIVIDER_ALPHA = 0.35f

@Composable
fun ShoppingListsRoute(
    onShoppingListClickNavigate: (Long) -> Unit,
    onCreateListClickNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShoppingListsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiResult) {
        viewModel.uiResult.collect { result ->
            when (result) {
                is ShoppingListUiResult.OnCreateListNavigate -> onCreateListClickNavigate()
                is ShoppingListUiResult.OnDetailListNavigate -> onShoppingListClickNavigate(result.listId)
                is ShoppingListUiResult.OnError -> TODO()
                is ShoppingListUiResult.OnLoading -> TODO()
                is ShoppingListUiResult.OnNavigateBack -> TODO()
                is ShoppingListUiResult.OnShowEmptyLists -> TODO()
                is ShoppingListUiResult.OnShowLists -> TODO()
            }
        }
    }

    ShoppingListsScreen(
        uiState = uiState,
        onUiEvent = viewModel::dispatch,
        modifier = modifier,
    )
}

@Composable
fun ShoppingListsScreen(
    uiState: ShoppingListsUiState,
    onUiEvent: (ShoppingListsUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchOnce {
        onUiEvent(ShoppingListsUiAction.FetchLists)
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

            uiState.shoppingLists.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.shopping_lists_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            else -> {
                ShoppingListsContent(
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        if (!uiState.isLoading) {
            ExtendedFloatingActionButton(
                onClick = dropUnlessResumed {
                    onUiEvent(ShoppingListsUiAction.NewListClick)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                },
                text = {
                    Text(text = stringResource(id = R.string.shopping_lists_create))
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
private fun ShoppingListsContent(
    uiState: ShoppingListsUiState,
    onUiEvent: (ShoppingListsUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 12.dp,
            end = 16.dp,
            bottom = 96.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = uiState.shoppingLists,
            key = { shoppingList -> shoppingList.id },
            contentType = { "shopping_list" },
        ) { shoppingList ->
            ShoppingListCard(
                shoppingList = shoppingList,
                onClick = {
                    onUiEvent.invoke(
                        ShoppingListsUiAction.ItemListClick(
                            shoppingList.id
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (shoppingList != uiState.shoppingLists.last()) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA),
                )
            }
        }
    }
}

@Composable
private fun ShoppingListCard(
    shoppingList: ShoppingListSummary,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = remember {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
    }
    val createdAtText = remember(shoppingList.createdAt, dateFormatter) {
        dateFormatter.format(Date(shoppingList.createdAt))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 8.dp)
            .clickable(
                onClick = { onClick(shoppingList.id) }
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = shoppingList.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 16.dp, end = 18.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = createdAtText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 6.dp, end = 18.dp, bottom = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "navigation item"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShoppingListsScreenPreview() {
    ListoTheme {
        ShoppingListsScreen(
            uiState = ShoppingListsUiState(
                isLoading = false,
                shoppingLists = listOf(
                    ShoppingListSummary(
                        id = 1L,
                        title = "Compras da semana",
                        createdAt = 1_784_324_400_000L,
                    ),
                    ShoppingListSummary(
                        id = 2L,
                        title = "Churrasco de domingo",
                        createdAt = 1_784_238_000_000L,
                    ),
                    ShoppingListSummary(
                        id = 3L,
                        title = "Churrasco de domingo",
                        createdAt = 1_784_238_000_000L,
                    ),
                ),
            ),
            onUiEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyShoppingListsScreenPreview() {
    ListoTheme {
        ShoppingListsScreen(
            uiState = ShoppingListsUiState(isLoading = false),
            onUiEvent = {}
        )
    }
}
