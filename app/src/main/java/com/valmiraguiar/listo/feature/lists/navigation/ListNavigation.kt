package com.valmiraguiar.listo.feature.lists.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.valmiraguiar.listo.core.navigation.ListoDestination
import com.valmiraguiar.listo.feature.lists.presentation.create.CreateListRoute
import com.valmiraguiar.listo.feature.lists.presentation.details.ShoppingListDetailsRoute
import com.valmiraguiar.listo.feature.lists.presentation.list.ShoppingListsRoute

fun EntryProviderScope<NavKey>.registerListEntries(
    onBack: () -> Boolean,
    onCreateListClick: () -> Unit,
    onShoppingListClick: (Long) -> Unit,
) {
    entry<ListoDestination.ShoppingLists> {
        ShoppingListsRoute(
            onShoppingListClick = onShoppingListClick,
            onCreateListClick = onCreateListClick,
        )
    }

    entry<ListoDestination.CreateList> {
        CreateListRoute(onBack = onBack)
    }

    entry<ListoDestination.ShoppingListDetails> {
        ShoppingListDetailsRoute(onBack = onBack)
    }
}
