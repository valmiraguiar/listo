package com.valmiraguiar.listo.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.listo.feature.login.domain.exception.AuthNetworkException
import com.valmiraguiar.listo.feature.login.domain.exception.InvalidCredentialsException
import com.valmiraguiar.listo.feature.login.domain.usecase.LoginUseCase
import com.valmiraguiar.listo.feature.login.presentation.state.LoginError
import com.valmiraguiar.listo.feature.login.presentation.state.LoginUiAction
import com.valmiraguiar.listo.feature.login.presentation.state.LoginUiResult
import com.valmiraguiar.listo.feature.login.presentation.state.LoginUiState
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiResult = MutableSharedFlow<LoginUiResult>()
    val uiResult: SharedFlow<LoginUiResult> get() = _uiResult

    fun dispatch(action: LoginUiAction) {
        when (action) {
            is LoginUiAction.EmailChange -> _uiState.update { it.copy(email = action.email) }
            is LoginUiAction.PasswordChange -> _uiState.update { it.copy(password = action.password) }
            is LoginUiAction.TogglePasswordVisibility -> _uiState.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }

            is LoginUiAction.LoginClick -> login()
        }
    }

    private fun login() {
        val currentState = _uiState.value
        if (!currentState.canSubmit) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            runCatching {
                loginUseCase(email = currentState.email.trim(), password = currentState.password)
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _uiResult.emit(LoginUiResult.OnLoginSuccess)
            }.onFailure { cause ->
                _uiState.update { it.copy(isLoading = false) }
                _uiResult.emit(LoginUiResult.OnError(error = cause.toLoginError()))
            }
        }
    }

    private fun Throwable.toLoginError(): LoginError = when (this) {
        is InvalidCredentialsException -> LoginError.InvalidCredentials
        is AuthNetworkException -> LoginError.Network
        else -> LoginError.Unknown
    }
}
