package com.valmiraguiar.listo.feature.login.data.repository

import android.content.Context
import com.valmiraguiar.listo.feature.login.domain.repository.LoginPromptRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

class LoginPromptRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
) : LoginPromptRepository {
    private val sharedPreferences = context.getSharedPreferences(
        LOGIN_PROMPT_PREFERENCES_NAME,
        Context.MODE_PRIVATE,
    )

    override fun shouldShowLoginPrompt(): Boolean {
        return !sharedPreferences.getBoolean(LOGIN_PROMPT_ALREADY_SHOWN_KEY, false)
    }

    override fun markLoginPromptAsShown() {
        sharedPreferences.edit {
            putBoolean(LOGIN_PROMPT_ALREADY_SHOWN_KEY, true)
        }
    }

    private companion object {
        const val LOGIN_PROMPT_PREFERENCES_NAME = "login_prompt_preferences"
        const val LOGIN_PROMPT_ALREADY_SHOWN_KEY = "login_prompt_already_shown"
    }
}
