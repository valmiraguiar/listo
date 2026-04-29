package com.valmiraguiar.listo.core.navigation

import androidx.navigation3.runtime.NavKey

interface ListoNavigator {
    val canNavigateBack: Boolean

    fun navigateTo(destination: ListoDestination)

    fun replaceAll(destination: ListoDestination)

    fun navigateBack(): Boolean
}

class ListoNavigatorImpl(
    private val backStack: MutableList<NavKey>,
) : ListoNavigator {

    override val canNavigateBack: Boolean
        get() = backStack.size > 1

    override fun navigateTo(destination: ListoDestination) {
        backStack.add(destination)
    }

    override fun replaceAll(destination: ListoDestination) {
        backStack.clear()
        backStack.add(destination)
    }

    override fun navigateBack(): Boolean {
        if (!canNavigateBack) return false
        backStack.removeLast()
        return true
    }
}
