package br.com.ifilmes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// Filme ou série salvo no catálogo. Os dados vêm da API do TMDB.
@Entity(tableName = "filme")
data class Filme(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tmdbId: Int,
    val tipo: String, // "movie" (filme) ou "tv" (série), igual ao TMDB
    val titulo: String,
    val ano: String,
    val sinopse: String,
    val poster: String,
    val usuarioId: Int = 0
) : Serializable
