package com.valmiraguiar.listo.feature.lists.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.ui.theme.ListoTheme
import java.text.DateFormat
import java.util.Date

@Composable
fun ShoppingListsRoute(
    onShoppingListClick: (Long) -> Unit,
    onCreateListClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShoppingListsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ShoppingListsScreen(
        uiState = uiState,
        onShoppingListClick = onShoppingListClick,
        onCreateListClick = onCreateListClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListsScreen(
    uiState: ShoppingListsUiState,
    onShoppingListClick: (Long) -> Unit,
    onCreateListClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
//    Scaffold(
//        modifier = modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//            .safeDrawingPadding(),
//        topBar = {
//            ListoTopBar()
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = dropUnlessResumed(block = onCreateListClick),
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Add,
//                    contentDescription = stringResource(id = R.string.shopping_lists_create),
//                )
//            }
//        },
//    ) { contentPadding ->
// TODO - Add the FAB again
    ShoppingListsContent(
        uiState = uiState,
        onShoppingListClick = onShoppingListClick,
        modifier = Modifier
            .fillMaxSize(),
    )
//    }
}

@Composable
private fun ShoppingListsContent(
    uiState: ShoppingListsUiState,
    onShoppingListClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.shoppingLists.isEmpty() -> {
            Box(
                modifier = modifier.padding(horizontal = 24.dp),
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
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = uiState.shoppingLists,
                    key = { shoppingList -> shoppingList.id },
                    contentType = { "shopping_list" },
                ) { shoppingList ->
                    ShoppingListCard(
                        shoppingList = shoppingList,
                        onClick = onShoppingListClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun ShoppingListCard(
    shoppingList: ShoppingListUiItem,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = remember {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
    }
    val createdAtText = remember(shoppingList.createdAt, dateFormatter) {
        dateFormatter.format(Date(shoppingList.createdAt))
    }

    OutlinedCard(
        onClick = dropUnlessResumed {
            onClick(shoppingList.id)
        },
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
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
}

@Preview(showBackground = true)
@Composable
private fun ShoppingListsScreenPreview() {
    ListoTheme {
        ShoppingListsScreen(
            uiState = ShoppingListsUiState(
                isLoading = false,
                shoppingLists = listOf(
                    ShoppingListUiItem(
                        id = 1L,
                        title = "Compras da semana",
                        createdAt = 1_784_324_400_000L,
                    ),
                    ShoppingListUiItem(
                        id = 2L,
                        title = "Churrasco de domingo",
                        createdAt = 1_784_238_000_000L,
                    ),
                    ShoppingListUiItem(
                        id = 3L,
                        title = "Churrasco de domingo",
                        createdAt = 1_784_238_000_000L,
                    ),
                ),
            ),
            onShoppingListClick = {},
            onCreateListClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyShoppingListsScreenPreview() {
    ListoTheme {
        ShoppingListsScreen(
            uiState = ShoppingListsUiState(isLoading = false),
            onShoppingListClick = {},
            onCreateListClick = {},
        )
    }
}
