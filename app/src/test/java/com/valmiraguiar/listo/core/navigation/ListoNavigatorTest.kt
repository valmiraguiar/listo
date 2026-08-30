package com.valmiraguiar.listo.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.valmiraguiar.listo.feature.lists.navigation.EditListKey
import com.valmiraguiar.listo.feature.lists.navigation.ListDetailsKey
import com.valmiraguiar.listo.feature.lists.navigation.ShoppingListsKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ListoNavigatorTest {

    @Test
    fun `navigate adds destination to back stack`() {
        val backStack = NavBackStack<NavKey>(ShoppingListsKey)
        val navigator = ListoNavigator(
            state = ListoNavigationState(
                startRoute = ShoppingListsKey,
                stack = backStack,
            ),
        )

        navigator.navigate(EditListKey())

        assertEquals(
            listOf(ShoppingListsKey, EditListKey()),
            backStack.toList(),
        )
    }

    @Test
    fun `navigate moves existing destination to top of back stack`() {
        val editListKey = EditListKey()
        val backStack = NavBackStack<NavKey>(
            ShoppingListsKey,
            editListKey,
            ListDetailsKey(listId = 7L),
        )
        val navigator = ListoNavigator(
            state = ListoNavigationState(
                startRoute = ShoppingListsKey,
                stack = backStack,
            ),
        )

        navigator.navigate(editListKey)

        assertEquals(
            listOf(ShoppingListsKey, ListDetailsKey(listId = 7L), editListKey),
            backStack.toList(),
        )
    }

    @Test
    fun `navigateBack pops when there is previous destination`() {
        val backStack = NavBackStack<NavKey>(
            ShoppingListsKey,
            EditListKey(),
        )
        val navigator = ListoNavigator(
            state = ListoNavigationState(
                startRoute = ShoppingListsKey,
                stack = backStack,
            ),
        )

        navigator.navigateBack()

        assertEquals(listOf(ShoppingListsKey), backStack.toList())
    }

    @Test
    fun `navigateBack throws on root destination`() {
        val backStack = NavBackStack<NavKey>(ShoppingListsKey)
        val navigator = ListoNavigator(
            state = ListoNavigationState(
                startRoute = ShoppingListsKey,
                stack = backStack,
            ),
        )

        assertThrows(IllegalStateException::class.java) {
            navigator.navigateBack()
        }

        assertEquals(listOf(ShoppingListsKey), backStack.toList())
    }
}
