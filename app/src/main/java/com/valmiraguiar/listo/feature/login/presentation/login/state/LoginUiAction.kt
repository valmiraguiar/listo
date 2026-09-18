package com.valmiraguiar.listo.feature.login.presentation.login.state

sealed interface LoginUiAction {
    data class EmailChange(val email: String) : LoginUiAction
    data class PasswordChange(val password: String) : LoginUiAction
    data object TogglePasswordVisibility : LoginUiAction
    data object LoginClick : LoginUiAction
    data class GoogleSignIn(val idToken: String) : LoginUiAction
}
