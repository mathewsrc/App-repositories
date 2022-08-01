package br.com.dio.app.repositories.domain

import br.com.dio.app.repositories.core.UseCase
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.data.repositories.RepoRepository
import kotlinx.coroutines.flow.Flow

class ListFavoriteRepositoriesUseCase(
    private val repository: RepoRepository
) : UseCase<Unit, List<Repo>>() {
    override suspend fun execute(param: Unit): Flow<List<Repo>> {
        return repository.listFavoriteRepositories()
    }

    suspend fun save(repo: Repo){
        repository.save(repo)
    }

    suspend fun delete(repo: Repo){
        repository.save(repo)
    }
}