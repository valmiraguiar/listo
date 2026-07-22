package com.valmiraguiar.listo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.valmiraguiar.listo.core.navigation.ListoNavigator
import com.valmiraguiar.listo.feature.common.components.ListoTopBar
import com.valmiraguiar.listo.feature.lists.navigation.ShoppingListsKey
import com.valmiraguiar.listo.feature.lists.navigation.listsEntry
import com.valmiraguiar.listo.feature.splash.navigation.splashEntry

@Composable
fun ListoApp(
    appState: ListoAppState,
    modifier: Modifier = Modifier
) {
    val navigator = remember { ListoNavigator(appState.navigationState) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),
        topBar = {
            ListoTopBar(
                showBackButton = appState.navigationState.currentKey != ShoppingListsKey &&
                    appState.navigationState.currentKey != appState.navigationState.startRoute,
                onBackClick = navigator::navigateBack
            )
        }
    ) { contentPadding ->
        val entryProvider = entryProvider {
            splashEntry(navigator)
            listsEntry(navigator)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
        ) {
            NavDisplay(
                backStack = navigator.state.stack,
                modifier = modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                onBack = { navigator.navigateBack() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider,
            )
        }
    }
}
