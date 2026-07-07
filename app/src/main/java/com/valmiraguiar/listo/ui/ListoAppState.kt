package com.valmiraguiar.listo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.valmiraguiar.listo.core.navigation.ListoNavigationState
import com.valmiraguiar.listo.core.navigation.rememberListoNavigationState
import com.valmiraguiar.listo.feature.splash.navigation.SplashKey

@Stable
class ListoAppState internal constructor(
    val navigationState: ListoNavigationState,
)

@Composable
fun rememberListoAppState(): ListoAppState {
    val navigationState = rememberListoNavigationState(SplashKey)

    return remember(navigationState) {
        ListoAppState(
            navigationState = navigationState
        )
    }
}
