package com.valmiraguiar.listo.feature.common.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope

@Composable
fun LaunchOnce(block: suspend CoroutineScope.() -> Unit) {
    var hasExecuted by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        if (!hasExecuted) {
            hasExecuted = true
            block()
        }
    }
}