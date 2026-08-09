package com.valmiraguiar.listo.feature.login.domain.repository

interface LoginPromptRepository {
    fun shouldShowLoginPrompt(): Boolean
    fun markLoginPromptAsShown()
}
