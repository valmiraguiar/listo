package com.valmiraguiar.listo.feature.login.domain.usecase

import com.valmiraguiar.listo.feature.login.domain.repository.LoginPromptRepository
import javax.inject.Inject

class MarkLoginPromptAsShownUseCase @Inject constructor(
    private val loginPromptRepository: LoginPromptRepository,
) {
    operator fun invoke() {
        loginPromptRepository.markLoginPromptAsShown()
    }
}
