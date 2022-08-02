package br.com.dio.app.repositories.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * This class handles saving and retrieving user preferences, latest search repository name, etc.
 */
class UserPreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    UserPreferencesRepository {

    private object PreferencesKeys{
        val SORT_BY_STAR = stringPreferencesKey("latest_search_name")
    }

    override suspend fun saveSearchKey(key: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_BY_STAR] = key
        }
    }

    override fun getLatestSearchKey(): Flow<String?> {
       return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SORT_BY_STAR]
        }
    }
}