package br.com.dio.app.repositories.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {

    private val _query = MutableLiveData<String?>(null)
    val query: LiveData<String?> = _query

    fun searchBy(query: String) {
        _query.value = query
    }

    fun clearQuery() {
        _query.value = null
    }

}