package com.valmiraguiar.listo.feature.splash.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.valmiraguiar.listo.core.navigation.ListoNavigator
import com.valmiraguiar.listo.feature.lists.navigation.navigateToLists
import com.valmiraguiar.listo.feature.splash.presentation.SplashScreen

fun EntryProviderScope<NavKey>.splashEntry(navigator: ListoNavigator) {
    entry<SplashKey> {
        SplashScreen(
            onContinue = navigator::navigateToLists
        )
    }
}