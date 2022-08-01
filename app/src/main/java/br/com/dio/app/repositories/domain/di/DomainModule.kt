package br.com.dio.app.repositories.domain.di

import br.com.dio.app.repositories.domain.ListFavoriteRepositoriesUseCase
import br.com.dio.app.repositories.domain.ListUserRepositoriesUseCase
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {

    fun load() {
        loadKoinModules(listOf(useCaseModule(), favoriteUserCaseModule()))
    }

    private fun useCaseModule(): Module {
        return module {
            factory { ListUserRepositoriesUseCase(get()) }
        }
    }

    private fun favoriteUserCaseModule():Module{
        return module {
            factory { ListFavoriteRepositoriesUseCase(get()) }
        }
    }

}