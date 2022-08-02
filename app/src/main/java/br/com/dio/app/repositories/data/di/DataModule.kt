package br.com.dio.app.repositories.data.di

import android.util.Log
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import br.com.dio.app.repositories.data.local.AppDatabase
import br.com.dio.app.repositories.data.repositories.RepoRepository
import br.com.dio.app.repositories.data.repositories.RepoRepositoryImpl
import br.com.dio.app.repositories.data.repositories.UserPreferencesRepository
import br.com.dio.app.repositories.data.repositories.UserPreferencesRepositoryImpl
import br.com.dio.app.repositories.data.services.GitHubService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {

    private const val OK_HTTP = "OkHttp"
    private const val USER_SEARCH_PREFERENCE = "search"

    fun load() {
        loadKoinModules(networkModules() + repositoriesModule() + localDbModule() + datastoreModule())
    }

    private fun networkModules(): Module {
        return module {
            single {
                val interceptor = HttpLoggingInterceptor {
                    Log.e(OK_HTTP, it)
                }
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }

            single {
                GsonConverterFactory.create(GsonBuilder().create())
            }

            single {
                createService<GitHubService>(get(), get())
            }
        }
    }

    private fun repositoriesModule(): Module {
        return module {
            single<RepoRepository> { RepoRepositoryImpl(get(), get()) }
            single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }
        }
    }

    private fun localDbModule(): Module {
        return module {
            single {
                AppDatabase.createDatabase(androidContext())
            }
        }
    }

    private fun datastoreModule(): Module {
        return module {
            single {
                PreferenceDataStoreFactory.create(
                    corruptionHandler = ReplaceFileCorruptionHandler {
                        emptyPreferences()
                    },
                    scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                    produceFile = { androidContext().preferencesDataStoreFile(USER_SEARCH_PREFERENCE) }
                )
            }
        }
    }

    private inline fun <reified T> createService(
        client: OkHttpClient,
        factory: GsonConverterFactory
    ): T {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(factory)
            .build().create(T::class.java)
    }

}