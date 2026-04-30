package com.valmiraguiar.listo.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.valmiraguiar.listo.feature.lists.navigation.registerListEntries
import com.valmiraguiar.listo.feature.splash.navigation.registerSplashEntry

@Composable
fun ListoNavHost(
    appState: ListoAppState,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        backStack = appState.backStack,
        modifier = modifier.fillMaxSize(),
        onBack = { appState.navigateBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator<NavKey>(),
        ),
        entryProvider = entryProvider<NavKey> {
            registerSplashEntry(
                onContinue = {
                    appState.replaceAll(ListoDestination.CreateList)
                },
            )
            registerListEntries(
                onBack = appState::navigateBack,
            )
        },
    )
}
