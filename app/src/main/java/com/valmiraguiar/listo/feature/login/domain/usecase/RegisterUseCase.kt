package com.valmiraguiar.listo.feature.login.domain.usecase

import com.valmiraguiar.listo.feature.login.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String) {
        authRepository.register(email = email, password = password)
    }
}
