package br.com.dio.app.repositories.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.domain.ListUserRepositoriesUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val listUserRepositoriesUseCase: ListUserRepositoriesUseCase,
) : ViewModel() {

    private val _repos = MutableLiveData<State>()
    val repos: LiveData<State> = _repos

    fun getRepoList(user: String) {
        viewModelScope.launch {
            listUserRepositoriesUseCase(user)
                .onStart {
                    _repos.postValue(State.Loading)
                }
                .catch {
                    _repos.postValue(State.Error(it))
                }
                .collect {
                    _repos.postValue(State.Success(it))
                }
        }
    }

    fun save(repo: Repo){
        viewModelScope.launch {
            try {
                listUserRepositoriesUseCase.save(repo)
            } catch (e: Exception) {
                Log.e(TAG, "Error to save item to database")
            }
        }
    }

    fun delete(repo: Repo){
        viewModelScope.launch {
            try {
                listUserRepositoriesUseCase.delete(repo)
            } catch (e: Exception) {
                Log.e(TAG, "Error to delete item from database")
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val list: List<Repo>) : State()
        data class Error(val error: Throwable) : State()
    }

    companion object{
        private const val TAG = "HomeViewModel"
    }
}