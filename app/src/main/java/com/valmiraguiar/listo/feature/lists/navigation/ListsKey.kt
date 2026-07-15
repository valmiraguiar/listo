package com.valmiraguiar.listo.feature.lists.navigation

import androidx.navigation3.runtime.NavKey
import com.valmiraguiar.listo.core.navigation.ListoNavigator
import kotlinx.serialization.Serializable

@Serializable
data object ShoppingListsKey : NavKey

@Serializable
data class ListDetailsKey(val listId: Long) : NavKey

fun ListoNavigator.navigateToLists() {
    navigate(ShoppingListsKey)
}

fun ListoNavigator.navigateToListDetails(listId: Long) {
    navigate(ListDetailsKey(listId = listId))
}
