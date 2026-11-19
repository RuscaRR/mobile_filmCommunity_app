package br.com.claquete.model

import androidx.annotation.StringRes
import br.com.claquete.R

enum class Genero(@StringRes val nomeRes: Int) {
    ACAO(R.string.genero_acao),
    ANIMACAO(R.string.genero_animacao),
    AVENTURA(R.string.genero_aventura),
    COMEDIA(R.string.genero_comedia),
    CRIME(R.string.genero_crime),
    DOCUMENTARIO(R.string.genero_documentario),
    DRAMA(R.string.genero_drama),
    FANTASIA(R.string.genero_fantasia),
    FICCAO_CIENTIFICA(R.string.genero_ficcao_cientifica),
    MUSICAL(R.string.genero_musical),
    ROMANCE(R.string.genero_romance),
    SUSPENSE(R.string.genero_suspense),
    TERROR(R.string.genero_terror);

    companion object {
        fun fromNome(nome: String): Genero {
            return entries.firstOrNull { it.name == nome } ?: DRAMA
        }
    }
}
