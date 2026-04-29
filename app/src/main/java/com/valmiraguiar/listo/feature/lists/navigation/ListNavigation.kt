package com.valmiraguiar.listo.feature.lists.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.valmiraguiar.listo.core.navigation.ListoDestination
import com.valmiraguiar.listo.feature.lists.presentation.create.CreateListRoute

fun EntryProviderScope<NavKey>.registerListEntries(
    onBack: () -> Boolean,
) {
    entry<ListoDestination.Home> {
        CreateListRoute(onBack = onBack)
    }
}
