package br.com.dio.app.repositories.data.repositories

import br.com.dio.app.repositories.core.RemoteException
import br.com.dio.app.repositories.data.local.AppDatabase
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.data.services.GitHubService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class RepoRepositoryImpl(private val service: GitHubService, private val appDatabase: AppDatabase) :
    RepoRepository {

    override suspend fun listRepositories(user: String) = flow {
        try {
            val repoList = service.listRepositories(user)
            emit(repoList)
        } catch (ex: HttpException) {
            throw RemoteException(ex.message ?: "Não foi possivel fazer a busca no momento!")
        }
    }

    override suspend fun listFavoriteRepositories(): Flow<List<Repo>> {
        return appDatabase.repoDao().getAll()
    }

    override suspend fun save(repo: Repo) {
        appDatabase.repoDao().save(repo)
    }

    override suspend fun delete(repo: Repo) {
        appDatabase.repoDao().delete(repo)
    }

    override suspend fun getCount(): Flow<Int> {
        return appDatabase.repoDao().getCount()
    }
}