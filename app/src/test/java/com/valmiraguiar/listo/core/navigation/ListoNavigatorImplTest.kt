package com.valmiraguiar.listo.core.navigation

import androidx.navigation3.runtime.NavKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ListoNavigatorImplTest {

    @Test
    fun `navigateTo adds destination to back stack`() {
        val backStack = mutableListOf<NavKey>(ListoDestination.Splash)
        val navigator = ListoNavigatorImpl(backStack)

        navigator.navigateTo(ListoDestination.CreateList)

        assertEquals(
            listOf(ListoDestination.Splash, ListoDestination.CreateList),
            backStack,
        )
    }

    @Test
    fun `replaceAll keeps only the new root destination`() {
        val backStack = mutableListOf<NavKey>(
            ListoDestination.Splash,
            ListoDestination.CreateList,
        )
        val navigator = ListoNavigatorImpl(backStack)

        navigator.replaceAll(ListoDestination.TaskDetails(taskId = 7L))

        assertEquals(
            listOf(ListoDestination.TaskDetails(taskId = 7L)),
            backStack,
        )
    }

    @Test
    fun `navigateBack pops when there is previous destination`() {
        val backStack = mutableListOf<NavKey>(
            ListoDestination.Splash,
            ListoDestination.CreateList,
        )
        val navigator = ListoNavigatorImpl(backStack)

        val didPop = navigator.navigateBack()

        assertTrue(didPop)
        assertEquals(listOf(ListoDestination.Splash), backStack)
    }

    @Test
    fun `navigateBack does nothing on root destination`() {
        val backStack = mutableListOf<NavKey>(ListoDestination.Splash)
        val navigator = ListoNavigatorImpl(backStack)

        val didPop = navigator.navigateBack()

        assertFalse(didPop)
        assertEquals(listOf(ListoDestination.Splash), backStack)
    }
}
