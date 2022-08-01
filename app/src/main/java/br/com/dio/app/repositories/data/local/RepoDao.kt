package br.com.dio.app.repositories.data.local

import androidx.room.*
import br.com.dio.app.repositories.data.model.Repo
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun save(repo: Repo)

    @Query("SELECT * FROM repo WHERE favorite = 1")
    fun getAll():Flow<List<Repo>>

//    @Delete
//    suspend fun delete(repo: Repo)
}