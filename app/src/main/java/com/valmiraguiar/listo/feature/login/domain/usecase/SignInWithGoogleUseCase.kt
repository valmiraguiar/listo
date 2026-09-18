package com.valmiraguiar.listo.feature.login.domain.usecase

import com.valmiraguiar.listo.feature.login.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(idToken: String) {
        authRepository.loginWithGoogle(idToken = idToken)
    }
}
