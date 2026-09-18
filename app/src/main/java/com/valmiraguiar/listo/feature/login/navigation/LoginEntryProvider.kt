package com.valmiraguiar.listo.feature.login.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.valmiraguiar.listo.core.navigation.ListoNavigator
import com.valmiraguiar.listo.feature.login.presentation.login.LoginRoute
import com.valmiraguiar.listo.feature.login.presentation.register.RegisterRoute

fun EntryProviderScope<NavKey>.loginEntry(navigator: ListoNavigator) {
    entry<LoginKey> {
        LoginRoute(
            onLoginSuccess = navigator::navigateBack,
            onRegisterClick = navigator::navigateToRegister,
        )
    }

    entry<RegisterKey> {
        RegisterRoute(
            onRegisterSuccess = navigator::navigateBack,
        )
    }
}
