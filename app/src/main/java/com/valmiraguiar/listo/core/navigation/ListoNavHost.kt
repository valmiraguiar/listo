package com.valmiraguiar.listo.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.valmiraguiar.listo.feature.common.components.ListoTopBar
import com.valmiraguiar.listo.feature.lists.navigation.registerListEntries
import com.valmiraguiar.listo.feature.splash.navigation.registerSplashEntry

@Composable
fun ListoNavHost(
    appState: ListoAppState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),
        topBar = {
            ListoTopBar()
        }
    ) { contentPadding ->
        NavDisplay(
            backStack = appState.backStack,
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding),
            onBack = { appState.navigateBack() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator<NavKey>(),
            ),
            entryProvider = entryProvider<NavKey> {
                registerSplashEntry(
                    onContinue = {
                        appState.replaceAll(ListoDestination.ShoppingLists)
                    },
                )
                registerListEntries(
                    onBack = appState::navigateBack,
                    onCreateListClick = {
                        appState.navigateTo(ListoDestination.CreateList)
                    },
                    onShoppingListClick = { shoppingListId ->
                        appState.navigateTo(
                            ListoDestination.ShoppingListDetails(shoppingListId = shoppingListId),
                        )
                    },
                )
            },
        )
    }
}
