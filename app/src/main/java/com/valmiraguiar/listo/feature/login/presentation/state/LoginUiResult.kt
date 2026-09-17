package com.valmiraguiar.listo.feature.login.presentation.state

sealed interface LoginUiResult {
    data object OnLoginSuccess : LoginUiResult
    data class OnError(val error: LoginError) : LoginUiResult
}

enum class LoginError {
    InvalidCredentials,
    Network,
    Unknown,
}
