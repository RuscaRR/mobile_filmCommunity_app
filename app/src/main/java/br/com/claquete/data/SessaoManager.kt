package br.com.claquete.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessaoManager(context: Context) {
    companion object {
        private const val NOME_PREFERENCIAS = "claquete_sessao"
        private const val CHAVE_USUARIO_ID = "usuario_id"
        private const val SEM_USUARIO = -1
    }

    private val preferencias: SharedPreferences =
        context.applicationContext.getSharedPreferences(NOME_PREFERENCIAS, Context.MODE_PRIVATE)

    fun salvarUsuario(usuarioId: Int) {
        preferencias.edit { putInt(CHAVE_USUARIO_ID, usuarioId) }
    }

    fun obterUsuarioId(): Int = preferencias.getInt(CHAVE_USUARIO_ID, SEM_USUARIO)

    fun estaLogado(): Boolean = obterUsuarioId() != SEM_USUARIO

    fun encerrar() {
        preferencias.edit { remove(CHAVE_USUARIO_ID) }
    }
}
