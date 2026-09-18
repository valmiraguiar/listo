package com.valmiraguiar.listo.feature.login.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.valmiraguiar.listo.feature.login.domain.exception.AuthNetworkException
import com.valmiraguiar.listo.feature.login.domain.exception.EmailAlreadyInUseException
import com.valmiraguiar.listo.feature.login.domain.exception.InvalidCredentialsException
import com.valmiraguiar.listo.feature.login.domain.exception.WeakPasswordException
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

    override suspend fun register(email: String, password: String) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        } catch (exception: FirebaseAuthUserCollisionException) {
            throw EmailAlreadyInUseException(cause = exception)
        } catch (exception: FirebaseAuthWeakPasswordException) {
            throw WeakPasswordException(cause = exception)
        } catch (exception: FirebaseAuthInvalidCredentialsException) {
            throw InvalidCredentialsException(cause = exception)
        } catch (exception: FirebaseNetworkException) {
            throw AuthNetworkException(cause = exception)
        }
    }

    override suspend fun loginWithGoogle(idToken: String) {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
        } catch (exception: FirebaseAuthUserCollisionException) {
            throw EmailAlreadyInUseException(cause = exception)
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
