package com.valmiraguiar.listo.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valmiraguiar.listo.core.navigation.ListoNavHost
import com.valmiraguiar.listo.core.navigation.rememberListoAppState

@Composable
fun ListoApp(modifier: Modifier = Modifier) {
    val appState = rememberListoAppState()

    Surface(modifier = modifier) {
        ListoNavHost(appState = appState)
    }
}
