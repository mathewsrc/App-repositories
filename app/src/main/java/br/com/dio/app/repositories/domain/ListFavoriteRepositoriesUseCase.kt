package br.com.dio.app.repositories.domain

import br.com.dio.app.repositories.core.UseCase
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.data.repositories.RepoRepository
import kotlinx.coroutines.flow.Flow

class ListFavoriteRepositoriesUseCase(
    private val repository: RepoRepository
) : UseCase.NoParam<List<Repo>>() {

    override suspend fun execute(): Flow<List<Repo>> {
        return repository.listFavoriteRepositories()
    }

    suspend fun save(repo: Repo) {
        repository.save(repo)
    }

    suspend fun getCount(): Flow<Int> {
        return repository.getCount()
    }

    suspend fun delete(repo: Repo){
        repository.delete(repo)
    }
}