package com.valmiraguiar.listo.feature.login.presentation.login

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.valmiraguiar.listo.R

sealed interface GoogleSignInResult {
    data class Success(val idToken: String) : GoogleSignInResult
    data object Cancelled : GoogleSignInResult
    data object NoCredentialAvailable : GoogleSignInResult
    data object Failed : GoogleSignInResult
}

suspend fun requestGoogleIdToken(context: Context): GoogleSignInResult {
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build(),
        )
        .build()

    return try {
        val credential = CredentialManager.create(context)
            .getCredential(context = context, request = request)
            .credential

        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            GoogleSignInResult.Success(idToken = googleIdTokenCredential.idToken)
        } else {
            GoogleSignInResult.Failed
        }
    } catch (ignored: GetCredentialCancellationException) {
        GoogleSignInResult.Cancelled
    } catch (ignored: NoCredentialException) {
        GoogleSignInResult.NoCredentialAvailable
    } catch (ignored: GetCredentialException) {
        GoogleSignInResult.Failed
    } catch (ignored: GoogleIdTokenParsingException) {
        GoogleSignInResult.Failed
    }
}
