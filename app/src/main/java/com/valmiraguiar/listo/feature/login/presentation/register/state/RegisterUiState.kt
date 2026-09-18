package com.valmiraguiar.listo.feature.login.presentation.register.state

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
) {
    val passwordsMatch: Boolean
        get() = confirmPassword.isEmpty() || password == confirmPassword

    val canSubmit: Boolean
        get() = email.isNotBlank() &&
            password.isNotBlank() &&
            password == confirmPassword &&
            !isLoading
}
