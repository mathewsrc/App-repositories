package br.com.dio.app.repositories.presentation.di

import br.com.dio.app.repositories.presentation.FavoriteViewModel
import br.com.dio.app.repositories.presentation.HomeViewModel
import br.com.dio.app.repositories.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object PresentationModule {

    fun load() {
        loadKoinModules(
            listOf(
                mainViewModelModule(),
                homeViewModelModule(),
                favoriteViewModelModule()
            )
        )
    }

    private fun mainViewModelModule(): Module {
        return module {
            viewModel { MainViewModel(/*get(), get()*/) }
        }
    }

    private fun homeViewModelModule(): Module {
        return module {
            viewModel { HomeViewModel(get()) }
        }
    }

    private fun favoriteViewModelModule(): Module {
        return module {
            viewModel { FavoriteViewModel(get()) }
        }
    }
}