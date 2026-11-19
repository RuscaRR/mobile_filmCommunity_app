package br.com.claquete.model

import androidx.room.Embedded

data class FilmeComNota(
    @Embedded
    val filme: Filme,

    val mediaNotas: Float,
    val totalAvaliacoes: Int
)
