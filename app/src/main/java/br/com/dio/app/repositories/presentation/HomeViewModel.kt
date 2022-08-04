package br.com.dio.app.repositories.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.domain.ListUserRepositoriesUseCase
import br.com.dio.app.repositories.domain.UserPreferencesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val listUserRepositoriesUseCase: ListUserRepositoriesUseCase,
    private val userPreferencesUseCase: UserPreferencesUseCase
) : ViewModel() {

    private val _repos = MutableLiveData<State>()
    val repos: LiveData<State> = _repos

    private val sortByStar = MutableStateFlow(SortByStar.NONE)

    fun getRepoList(name: String) {
        viewModelScope.launch {
            listUserRepositoriesUseCase(name).combine(sortByStar) { repos, sort ->
                when (sort) {
                    SortByStar.NONE -> repos
                    SortByStar.ASCENDING -> repos.sortedBy { it.stargazersCount }
                    SortByStar.DESCENDING -> repos.sortedByDescending { it.stargazersCount }
                }
            }.onStart {
                _repos.postValue(State.Loading)
            }.catch {
                _repos.postValue(State.Error(it))
            }.collect {
                // Save name to datastore
                saveSearchRepositoryName(name)
                _repos.postValue(State.Success(it))
            }
        }
    }

    fun sortBy(sortBy: SortByStar) {
        sortByStar.value = sortBy
    }

    fun save(repo: Repo) {
        viewModelScope.launch {
            try {
                listUserRepositoriesUseCase.save(repo)
            } catch (e: Exception) {
                Log.e(TAG, "Error to save item to database", e)
            }
        }
    }

    fun saveSearchRepositoryName(key: String) {
        viewModelScope.launch {
            try {
                userPreferencesUseCase.saveSearchKey(key)
            } catch (e: Exception) {
                Log.e(TAG, "Error to save search key in datastore", e)
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val list: List<Repo>) : State()
        data class Error(val error: Throwable) : State()
    }

    enum class SortByStar {
        NONE,
        ASCENDING,
        DESCENDING
    }

    companion object {
        private const val TAG = "HomeViewModel"
        private const val DEFAULT_SEARCH_KEY = "android"
    }
}