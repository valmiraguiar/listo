package com.valmiraguiar.listo.feature.login.presentation.state

sealed interface LoginUiAction {
    data class EmailChange(val email: String) : LoginUiAction
    data class PasswordChange(val password: String) : LoginUiAction
    data object TogglePasswordVisibility : LoginUiAction
    data object LoginClick : LoginUiAction
}
