package com.valmiraguiar.listo.core.navigation

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

@Composable
fun rememberListoNavigationState(
    startRoute: NavKey
): ListoNavigationState {
    val stack = rememberNavBackStack(startRoute)

    return remember(startRoute, stack) {
        ListoNavigationState(
            startRoute = startRoute,
            stack = stack
        )
    }
}

class ListoNavigationState(
    val startRoute: NavKey,
    val stack: NavBackStack<NavKey>
) {
    val currentKey: NavKey by derivedStateOf { stack.last() }
}