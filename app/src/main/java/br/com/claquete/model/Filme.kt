package br.com.claquete.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "filme",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["usuarioId"])]
)
data class Filme(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,
    val diretor: String,
    val ano: Int,
    val genero: String,
    val duracao: Int,
    val sinopse: String,
    val poster: String = "",
    val trailerUrl: String = "",
    val usuarioId: Int? = null,
    val dataCadastro: Long = System.currentTimeMillis()
) : Serializable
