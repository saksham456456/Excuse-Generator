package com.example.savageexcuse.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_USERNAME = stringPreferencesKey("user_username")
        val USER_PERSONA = stringPreferencesKey("user_persona")
        val USER_AVATAR_URI = stringPreferencesKey("user_avatar_uri")
    }

    val onboardingCompleted: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED] ?: false
    }

    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    val userUsername: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_USERNAME]
    }

    val userAvatarUri: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_AVATAR_URI]
    }

    suspend fun completeOnboarding(name: String, persona: String) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = true
            preferences[USER_NAME] = name
            preferences[USER_PERSONA] = persona
        }
    }

    suspend fun updateProfile(name: String, username: String, avatarUri: String?) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
            preferences[USER_USERNAME] = username
            if (avatarUri != null) {
                preferences[USER_AVATAR_URI] = avatarUri
            }
        }
    }
}
