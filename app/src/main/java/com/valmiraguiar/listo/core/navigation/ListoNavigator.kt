package com.valmiraguiar.listo.core.navigation

import androidx.navigation3.runtime.NavKey

class ListoNavigator(
    val state: ListoNavigationState
) {

    /**
     * Navigate to a navigation key
     *
     * @param route = the navigation key to navigate to
     */
    fun navigate(
        route: NavKey
    ) {
        goToRoute(route)
    }

    fun navigateBack() {
        when (state.currentKey) {
            state.startRoute -> error("You cannot go back from the start route")
            else -> state.stack.removeLastOrNull()
        }
    }

    private fun goToRoute(route: NavKey) {
        state.stack.apply {
            remove(route)
            add(route)
        }
    }
}
