package br.com.dio.app.repositories.domain

import br.com.dio.app.repositories.core.UseCase
import br.com.dio.app.repositories.data.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class UserPreferencesUseCase(private val userPreferencesRepository: UserPreferencesRepository) :
    UseCase.NoParam<String?>() {
    override suspend fun execute(): Flow<String?> {
        return userPreferencesRepository.getLatestSearchKey()
    }

    suspend fun saveSearchKey(key: String) {
        userPreferencesRepository.saveSearchKey(key)
    }
}