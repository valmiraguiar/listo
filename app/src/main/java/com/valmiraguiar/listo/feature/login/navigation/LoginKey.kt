package com.valmiraguiar.listo.feature.login.navigation

import androidx.navigation3.runtime.NavKey
import com.valmiraguiar.listo.core.navigation.ListoNavigator
import kotlinx.serialization.Serializable

@Serializable
data object LoginKey : NavKey

@Serializable
data object RegisterKey : NavKey

fun ListoNavigator.navigateToLogin() {
    navigate(LoginKey)
}

fun ListoNavigator.navigateToRegister() {
    navigate(RegisterKey)
}
