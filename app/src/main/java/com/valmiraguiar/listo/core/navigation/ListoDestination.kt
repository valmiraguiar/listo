package com.valmiraguiar.listo.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface ListoDestination : NavKey {
    @Serializable
    data object Splash : ListoDestination

    @Serializable
    data object Home : ListoDestination

    @Serializable
    data class TaskDetails(val taskId: Long) : ListoDestination
}
