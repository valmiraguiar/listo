package com.valmiraguiar.listo.feature.login.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String)
    suspend fun getIdToken(forceRefresh: Boolean = false): String?
}
