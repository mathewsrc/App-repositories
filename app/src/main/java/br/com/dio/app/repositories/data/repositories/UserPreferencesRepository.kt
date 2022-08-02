package br.com.dio.app.repositories.data.repositories

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun saveSearchKey(key:String)
    fun getLatestSearchKey():Flow<String?>
}