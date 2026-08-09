package com.valmiraguiar.listo.feature.login.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.valmiraguiar.listo.feature.login.presentation.LoginScreen

fun EntryProviderScope<NavKey>.loginEntry() {
    entry<LoginKey> {
        LoginScreen()
    }
}
