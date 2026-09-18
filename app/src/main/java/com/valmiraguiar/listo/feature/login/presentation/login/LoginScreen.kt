package com.valmiraguiar.listo.feature.login.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.common.components.UnderlinedTextField
import com.valmiraguiar.listo.feature.common.theme.ListoTheme
import com.valmiraguiar.listo.feature.login.presentation.login.state.LoginError
import com.valmiraguiar.listo.feature.login.presentation.login.state.LoginUiAction
import com.valmiraguiar.listo.feature.login.presentation.login.state.LoginUiResult
import com.valmiraguiar.listo.feature.login.presentation.login.state.LoginUiState
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val invalidCredentialsMessage = stringResource(R.string.login_error_invalid_credentials)
    val accountExistsMessage = stringResource(R.string.login_error_account_exists)
    val networkErrorMessage = stringResource(R.string.login_error_network)
    val unknownErrorMessage = stringResource(R.string.login_error_unknown)
    val noGoogleAccountMessage = stringResource(R.string.login_error_no_google_account)

    LaunchedEffect(viewModel.uiResult) {
        viewModel.uiResult.collect { result ->
            when (result) {
                is LoginUiResult.OnLoginSuccess -> onLoginSuccess()
                is LoginUiResult.OnError -> {
                    val message = when (result.error) {
                        LoginError.InvalidCredentials -> invalidCredentialsMessage
                        LoginError.AccountExists -> accountExistsMessage
                        LoginError.Network -> networkErrorMessage
                        LoginError.Unknown -> unknownErrorMessage
                    }
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        onUiEvent = viewModel::dispatch,
        onRegisterClick = onRegisterClick,
        onGoogleSignInClick = {
            coroutineScope.launch {
                when (val result = requestGoogleIdToken(context)) {
                    is GoogleSignInResult.Success -> {
                        viewModel.dispatch(LoginUiAction.GoogleSignIn(idToken = result.idToken))
                    }

                    is GoogleSignInResult.Cancelled -> Unit
                    is GoogleSignInResult.NoCredentialAvailable -> {
                        snackbarHostState.showSnackbar(noGoogleAccountMessage)
                    }

                    is GoogleSignInResult.Failed -> {
                        snackbarHostState.showSnackbar(unknownErrorMessage)
                    }
                }
            }
        },
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onUiEvent: (LoginUiAction) -> Unit,
    onRegisterClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding)
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.login_screen_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                UnderlinedTextField(
                    value = uiState.email,
                    onValueChange = { onUiEvent(LoginUiAction.EmailChange(it)) },
                    label = stringResource(R.string.login_email_label),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                )

                UnderlinedTextField(
                    value = uiState.password,
                    onValueChange = { onUiEvent(LoginUiAction.PasswordChange(it)) },
                    label = stringResource(R.string.login_password_label),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (uiState.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingContent = {
                        IconButton(
                            onClick = { onUiEvent(LoginUiAction.TogglePasswordVisibility) },
                        ) {
                            Icon(
                                imageVector = if (uiState.isPasswordVisible) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = stringResource(
                                    if (uiState.isPasswordVisible) {
                                        R.string.login_hide_password
                                    } else {
                                        R.string.login_show_password
                                    },
                                ),
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Button(
                onClick = dropUnlessResumed { onUiEvent(LoginUiAction.LoginClick) },
                enabled = uiState.canSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text(text = stringResource(R.string.login_submit))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.login_divider_or),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            OutlinedButton(
                onClick = dropUnlessResumed(block = onGoogleSignInClick),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
            ) {
                Text(text = stringResource(R.string.login_google_signin))
            }

            OutlinedButton(
                onClick = dropUnlessResumed(block = onRegisterClick),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            ) {
                Text(text = stringResource(R.string.login_register_action))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    ListoTheme {
        LoginScreen(
            uiState = LoginUiState(email = "user@email.com"),
            onUiEvent = {},
            onRegisterClick = {},
            onGoogleSignInClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenLoadingPreview() {
    ListoTheme {
        LoginScreen(
            uiState = LoginUiState(
                email = "user@email.com",
                password = "123456",
                isLoading = true,
            ),
            onUiEvent = {},
            onRegisterClick = {},
            onGoogleSignInClick = {},
        )
    }
}
