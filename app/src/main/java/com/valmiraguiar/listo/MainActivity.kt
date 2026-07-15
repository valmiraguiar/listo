package com.valmiraguiar.listo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.valmiraguiar.listo.feature.common.theme.ListoTheme
import com.valmiraguiar.listo.ui.ListoApp
import com.valmiraguiar.listo.ui.rememberListoAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListoTheme {
                val listoAppState = rememberListoAppState()
                ListoApp(
                    appState = listoAppState
                )
            }
        }
    }
}
