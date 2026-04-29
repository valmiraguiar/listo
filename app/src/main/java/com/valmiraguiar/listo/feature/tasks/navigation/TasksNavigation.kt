package com.valmiraguiar.listo.feature.tasks.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.valmiraguiar.listo.core.navigation.ListoDestination
import com.valmiraguiar.listo.feature.tasks.presentation.details.TaskDetailsRoute
import com.valmiraguiar.listo.feature.tasks.presentation.home.HomeRoute

fun EntryProviderScope<NavKey>.registerTasksEntries(
    onOpenTask: (Long) -> Unit,
    onBack: () -> Boolean,
) {
    entry<ListoDestination.Home> {
        HomeRoute(onOpenTask = onOpenTask)
    }

    entry<ListoDestination.TaskDetails> { destination ->
        TaskDetailsRoute(
            destination = destination,
            onBack = onBack,
        )
    }
}
