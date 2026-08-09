package com.valmiraguiar.listo.feature.lists.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.valmiraguiar.listo.core.navigation.ListoNavigator
import com.valmiraguiar.listo.feature.lists.presentation.details.ShoppingListDetailsRoute
import com.valmiraguiar.listo.feature.lists.presentation.edit.EditListRoute
import com.valmiraguiar.listo.feature.lists.presentation.list.ShoppingListsRoute
import com.valmiraguiar.listo.feature.login.navigation.navigateToLogin

fun EntryProviderScope<NavKey>.listsEntry(navigator: ListoNavigator) {
    entry<ShoppingListsKey> {
        ShoppingListsRoute(
            onShoppingListClickNavigate = navigator::navigateToListDetails,
            onCreateListClickNavigate = { navigator.navigateToEditList() },
            onLoginClickNavigate = navigator::navigateToLogin,
            viewModel = hiltViewModel()
        )
    }

    entry<ListDetailsKey> { key ->
        ShoppingListDetailsRoute(
            listId = key.listId,
            onEditListClickNavigate = { listId -> navigator.navigateToEditList(listId) },
            viewModel = hiltViewModel(),
        )
    }

    entry<EditListKey> { key ->
        EditListRoute(
            listId = key.listId,
            onBack = navigator::navigateBack,
        )
    }
}
