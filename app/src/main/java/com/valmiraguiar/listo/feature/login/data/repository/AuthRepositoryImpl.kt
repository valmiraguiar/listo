package com.valmiraguiar.listo.feature.login.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.valmiraguiar.listo.feature.login.domain.exception.AuthNetworkException
import com.valmiraguiar.listo.feature.login.domain.exception.InvalidCredentialsException
import com.valmiraguiar.listo.feature.login.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    override suspend fun login(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (exception: FirebaseAuthInvalidUserException) {
            throw InvalidCredentialsException(cause = exception)
        } catch (exception: FirebaseAuthInvalidCredentialsException) {
            throw InvalidCredentialsException(cause = exception)
        } catch (exception: FirebaseNetworkException) {
            throw AuthNetworkException(cause = exception)
        }
    }

    override suspend fun getIdToken(forceRefresh: Boolean): String? {
        return firebaseAuth.currentUser?.getIdToken(forceRefresh)?.await()?.token
    }
}
