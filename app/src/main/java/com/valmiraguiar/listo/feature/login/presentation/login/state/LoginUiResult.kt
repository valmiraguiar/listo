package com.valmiraguiar.listo.feature.login.presentation.login.state

sealed interface LoginUiResult {
    data object OnLoginSuccess : LoginUiResult
    data class OnError(val error: LoginError) : LoginUiResult
}

enum class LoginError {
    InvalidCredentials,
    AccountExists,
    Network,
    Unknown,
}
