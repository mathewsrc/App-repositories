package br.com.dio.app.repositories.presentation

import androidx.lifecycle.*
import br.com.dio.app.repositories.domain.UserPreferencesUseCase
import br.com.dio.app.repositories.presentation.MainViewModel.Companion.DEFAULT_QUERY
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.qualifier._q

/**
 *  if application is on its first run we going to use the [DEFAULT_QUERY],
 *  otherwise we will adopt two strategies: we will first try to retrieve
 *  the last name searched from the [SavedStateHandle] and
 *  if the user closes our application and reopens it we will
 *  try to retrieve the name stored in the [Datastore].
 */
class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userPreferencesUseCase: UserPreferencesUseCase
) : ViewModel() {

    private val _query = MutableLiveData<String?>()
    val query : LiveData<String?> = _query

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow(
                LATEST_SEARCH_QUERY,
                userPreferencesUseCase.execute().catch { emit(DEFAULT_QUERY) }.first() ?: DEFAULT_QUERY
            ).collectLatest {
                _query.value = it
            }
        }
    }

    fun searchBy(query: String) {
        if (query != savedStateHandle[LATEST_SEARCH_QUERY]) {
            savedStateHandle[LATEST_SEARCH_QUERY] = query
        }
    }

    fun clearSearch() {
        _query.value = null
    }

    companion object {
        private const val LATEST_SEARCH_QUERY = "query"
        private const val DEFAULT_QUERY = "android"
    }

}