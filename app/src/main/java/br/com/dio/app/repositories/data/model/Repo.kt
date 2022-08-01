package br.com.dio.app.repositories.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Repo(
    @PrimaryKey val id: Long,
    val name: String,
    @Embedded val owner: Owner,
    @SerializedName("stargazers_count")
    val stargazersCount: Long,
    val language: String?,
    @SerializedName("html_url")
    val htmlURL: String,
    val description: String,
    var favorite:Boolean = false
)
