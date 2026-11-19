package br.com.claquete.model

import androidx.room.Embedded
import androidx.room.Relation

data class AvaliacaoDetalhada(
    @Embedded
    val avaliacao: Avaliacao,

    @Relation(parentColumn = "usuarioId", entityColumn = "id")
    val usuario: Usuario,

    @Relation(parentColumn = "filmeId", entityColumn = "id")
    val filme: Filme
)
