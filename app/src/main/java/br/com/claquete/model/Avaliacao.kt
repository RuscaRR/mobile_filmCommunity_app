package br.com.claquete.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "avaliacao",
    primaryKeys = ["usuarioId", "filmeId"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Filme::class,
            parentColumns = ["id"],
            childColumns = ["filmeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["filmeId"])]
)
data class Avaliacao(
    val usuarioId: Int,
    val filmeId: Int,
    val nota: Float = 0f,
    val favorito: Boolean = false,
    val resenha: String = "",
    val data: Long = System.currentTimeMillis()
)
