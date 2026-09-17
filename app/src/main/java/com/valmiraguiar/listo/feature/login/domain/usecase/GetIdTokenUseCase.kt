package com.valmiraguiar.listo.feature.login.domain.usecase

import com.valmiraguiar.listo.feature.login.domain.repository.AuthRepository
import javax.inject.Inject

class GetIdTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): String? {
        return authRepository.getIdToken(forceRefresh = forceRefresh)
    }
}
