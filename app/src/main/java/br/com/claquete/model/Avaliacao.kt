package br.com.claquete.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "avaliacao")
data class Avaliacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val filmeId: Int,
    val usuarioId: Int,
    val nomeUsuario: String,
    val tituloFilme: String,
    val nota: Float,
    val comentario: String
) : Serializable
