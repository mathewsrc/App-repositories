package br.com.dio.app.repositories.data.repositories

import br.com.dio.app.repositories.data.model.Repo
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    suspend fun listRepositories(user: String): Flow<List<Repo>>
    suspend fun listFavoriteRepositories(): Flow<List<Repo>>
    suspend fun save(repo: Repo)
    suspend fun delete(repo: Repo)
    suspend fun getCount(): Flow<Int>
}