package br.com.claquete.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "seguidor",
    primaryKeys = ["seguidorId", "seguidoId"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["seguidorId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["seguidoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["seguidoId"])]
)
data class Seguidor(
    val seguidorId: Int,
    val seguidoId: Int,
    val data: Long = System.currentTimeMillis()
)
