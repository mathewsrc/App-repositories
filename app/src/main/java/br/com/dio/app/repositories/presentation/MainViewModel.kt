package br.com.dio.app.repositories.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(
    /*private val listUserRepositoriesUseCase: ListUserRepositoriesUseCase,
    private val listFavoriteRepositoriesUseCase: ListFavoriteRepositoriesUseCase*/
) : ViewModel() {

    private val _query = MutableLiveData<String?>(null)
    val query: LiveData<String?> = _query

//    private val _repos = MutableLiveData<State>()
//    val repos: LiveData<State> = _repos

    fun searchBy(query: String) {
        _query.value = query
    }

    fun clearQuery() {
        _query.value = null
    }

//    fun getRepoList(user: String) {
//        viewModelScope.launch {
//            listUserRepositoriesUseCase(user)
//                .onStart {
//                    _repos.postValue(State.Loading)
//                }
//                .catch {
//                    _repos.postValue(State.Error(it))
//                }
//                .collect {
//                    _repos.postValue(State.Success(it))
//                }
//        }
//    }
//
//    fun getFavoriteList() {
//        viewModelScope.launch {
//            listFavoriteRepositoriesUseCase(Unit)
//                .onStart {
//                    _repos.postValue(State.Loading)
//                }
//                .catch {
//                    _repos.postValue(State.Error(it))
//                }
//                .collect {
//                    _repos.postValue(State.Success(it))
//                }
//        }
//    }
//
//    sealed class State {
//        object Loading : State()
//        data class Success(val list: List<Repo>) : State()
//        data class Error(val error: Throwable) : State()
//    }

}