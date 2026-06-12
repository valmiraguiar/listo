package com.valmiraguiar.listo.feature.lists.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valmiraguiar.listo.ui.theme.ListoTheme

@Composable
fun ShoppingListDetailsRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ShoppingListDetailsScreen(
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
fun ShoppingListDetailsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shoppingItems = remember {
        mutableStateListOf<ShoppingListDetailsItem>().apply {
            addAll(MOCK_ITEMS)
        }
    }

    ShoppingListDetailsContent(
        shoppingItems = shoppingItems,
        onBack = onBack,
        onCheckedChange = { item, isChecked ->
            shoppingItems.remove(item)
            val updatedItem = item.copy(isChecked = isChecked)

            if (isChecked) {
                shoppingItems.add(updatedItem)
            } else {
                shoppingItems.add(0, updatedItem)
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun ShoppingListDetailsContent(
    shoppingItems: List<ShoppingListDetailsItem>,
    onBack: () -> Unit,
    onCheckedChange: (ShoppingListDetailsItem, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back arrow",
                modifier = Modifier.size(24.dp),
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = shoppingItems,
                key = { item -> item.id },
                contentType = { "shopping-list-item" },
            ) { item ->
                ShoppingListItem(
                    item = item,
                    onCheckedChange = { isChecked ->
                        onCheckedChange(item, isChecked)
                    },
                )
            }
        }
    }
}

@Composable
private fun ShoppingListItem(
    item: ShoppingListDetailsItem,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange,
        )

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
                text = item.classification,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private data class ShoppingListDetailsItem(
    val id: Long,
    val title: String,
    val classification: String,
    val isChecked: Boolean = false,
)

private val MOCK_ITEMS = listOf(
    ShoppingListDetailsItem(id = 1L, title = "Arroz", classification = "Mercearia"),
    ShoppingListDetailsItem(id = 2L, title = "Leite", classification = "Laticínios"),
    ShoppingListDetailsItem(id = 3L, title = "Maçã", classification = "Hortifruti"),
    ShoppingListDetailsItem(id = 4L, title = "Pão", classification = "Padaria"),
    ShoppingListDetailsItem(id = 5L, title = "Sabonete", classification = "Higiene"),
    ShoppingListDetailsItem(id = 6L, title = "Refrigerante", classification = "Bebidas"),
)

private val PREVIEW_ITEMS = listOf(
    ShoppingListDetailsItem(id = 1L, title = "Arroz", classification = "Mercearia"),
    ShoppingListDetailsItem(id = 2L, title = "Leite", classification = "Laticínios"),
    ShoppingListDetailsItem(id = 3L, title = "Maçã", classification = "Hortifruti"),
    ShoppingListDetailsItem(
        id = 4L,
        title = "Pão",
        classification = "Padaria",
        isChecked = true,
    ),
    ShoppingListDetailsItem(
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
            shoppingItems = PREVIEW_ITEMS,
            onBack = {},
            onCheckedChange = { _, _ -> },
        )
    }
}
