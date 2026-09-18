package com.valmiraguiar.listo.feature.login.presentation.register.state

sealed interface RegisterUiResult {
    data object OnRegisterSuccess : RegisterUiResult
    data class OnError(val error: RegisterError) : RegisterUiResult
}

enum class RegisterError {
    EmailAlreadyInUse,
    WeakPassword,
    InvalidEmail,
    Network,
    Unknown,
}
