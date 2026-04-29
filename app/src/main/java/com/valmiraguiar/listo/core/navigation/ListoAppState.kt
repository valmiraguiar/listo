package com.valmiraguiar.listo.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

@Stable
class ListoAppState internal constructor(
    val backStack: NavBackStack<NavKey>,
    private val navigator: ListoNavigator,
) {
    val canNavigateBack: Boolean
        get() = navigator.canNavigateBack

    fun navigateTo(destination: ListoDestination) {
        navigator.navigateTo(destination)
    }

    fun replaceAll(destination: ListoDestination) {
        navigator.replaceAll(destination)
    }

    fun navigateBack(): Boolean = navigator.navigateBack()
}

@Composable
fun rememberListoAppState(
    startDestination: ListoDestination = ListoDestination.Splash,
): ListoAppState {
    val backStack = rememberNavBackStack(startDestination)
    val navigator = remember(backStack) {
        ListoNavigatorImpl(backStack)
    }

    return remember(backStack, navigator) {
        ListoAppState(
            backStack = backStack,
            navigator = navigator,
        )
    }
}
