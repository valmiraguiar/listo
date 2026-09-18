package com.valmiraguiar.listo.feature.login.presentation.register.state

sealed interface RegisterUiAction {
    data class EmailChange(val email: String) : RegisterUiAction
    data class PasswordChange(val password: String) : RegisterUiAction
    data class ConfirmPasswordChange(val confirmPassword: String) : RegisterUiAction
    data object TogglePasswordVisibility : RegisterUiAction
    data object ToggleConfirmPasswordVisibility : RegisterUiAction
    data object RegisterClick : RegisterUiAction
}
