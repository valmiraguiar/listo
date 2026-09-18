package com.valmiraguiar.listo.feature.login.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.listo.feature.login.domain.exception.AuthNetworkException
import com.valmiraguiar.listo.feature.login.domain.exception.EmailAlreadyInUseException
import com.valmiraguiar.listo.feature.login.domain.exception.InvalidCredentialsException
import com.valmiraguiar.listo.feature.login.domain.exception.WeakPasswordException
import com.valmiraguiar.listo.feature.login.domain.usecase.RegisterUseCase
import com.valmiraguiar.listo.feature.login.presentation.register.state.RegisterError
import com.valmiraguiar.listo.feature.login.presentation.register.state.RegisterUiAction
import com.valmiraguiar.listo.feature.login.presentation.register.state.RegisterUiResult
import com.valmiraguiar.listo.feature.login.presentation.register.state.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _uiResult = MutableSharedFlow<RegisterUiResult>()
    val uiResult: SharedFlow<RegisterUiResult> get() = _uiResult

    fun dispatch(action: RegisterUiAction) {
        when (action) {
            is RegisterUiAction.EmailChange -> _uiState.update { it.copy(email = action.email) }
            is RegisterUiAction.PasswordChange -> _uiState.update { it.copy(password = action.password) }
            is RegisterUiAction.ConfirmPasswordChange -> _uiState.update {
                it.copy(confirmPassword = action.confirmPassword)
            }

            is RegisterUiAction.TogglePasswordVisibility -> _uiState.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }

            is RegisterUiAction.ToggleConfirmPasswordVisibility -> _uiState.update {
                it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
            }

            is RegisterUiAction.RegisterClick -> register()
        }
    }

    private fun register() {
        val currentState = _uiState.value
        if (!currentState.canSubmit) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            runCatching {
                registerUseCase(email = currentState.email.trim(), password = currentState.password)
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _uiResult.emit(RegisterUiResult.OnRegisterSuccess)
            }.onFailure { cause ->
                _uiState.update { it.copy(isLoading = false) }
                _uiResult.emit(RegisterUiResult.OnError(error = cause.toRegisterError()))
            }
        }
    }

    private fun Throwable.toRegisterError(): RegisterError = when (this) {
        is EmailAlreadyInUseException -> RegisterError.EmailAlreadyInUse
        is WeakPasswordException -> RegisterError.WeakPassword
        is InvalidCredentialsException -> RegisterError.InvalidEmail
        is AuthNetworkException -> RegisterError.Network
        else -> RegisterError.Unknown
    }
}
