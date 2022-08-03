package br.com.dio.app.repositories.presentation

import android.util.Log
import androidx.lifecycle.*
import br.com.dio.app.repositories.domain.UserPreferencesUseCase
import br.com.dio.app.repositories.presentation.MainViewModel.Companion.DEFAULT_QUERY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    @OptIn(ExperimentalCoroutinesApi::class)
    val query: LiveData<String?> =
        savedStateHandle.getStateFlow<String?>(LATEST_SEARCH_QUERY, DEFAULT_QUERY)
            .mapLatest { it }.asLiveData()

    init {
        viewModelScope.launch {
            // Collect only the first element emitted  and cancel flow's collection
            val name = userPreferencesUseCase.execute().catch {
                emit(DEFAULT_QUERY)
            }.first()
            if (name != null && savedStateHandle.get<String?>(LATEST_SEARCH_QUERY) != null) {
                savedStateHandle[LATEST_SEARCH_QUERY] = name
            }
        }
    }

    fun searchBy(query: String) {
        if (query != savedStateHandle[LATEST_SEARCH_QUERY]) {
            savedStateHandle[LATEST_SEARCH_QUERY] = query
        }
    }

    companion object {
        private const val LATEST_SEARCH_QUERY = "query"
        private const val DEFAULT_QUERY = "android"
    }

}