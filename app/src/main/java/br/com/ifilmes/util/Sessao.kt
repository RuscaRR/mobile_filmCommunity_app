package br.com.ifilmes.util

import android.content.Context

// Versão Android da HttpSession usada no FrontController do projeto Java.
// No Java a sessão fica no servidor; aqui ela fica salva no SharedPreferences,
// por isso o usuário continua logado mesmo depois de fechar o app.
object Sessao {
    private const val ARQUIVO = "sessao"
    private const val USUARIO_SESSAO = "usuario_sessao" // mesmo nome do atributo no Java
    private const val ULTIMO_ACESSO = "ultimo_acesso"

    // No Java: sessao.setMaxInactiveInterval(60 * 10), ou seja, 10 minutos.
    // No celular usamos 7 dias, para o login não cair toda hora.
    private const val MAX_INACTIVE_INTERVAL = 60 * 60 * 24 * 7 // em segundos

    // Equivale a: invalidate() da sessão antiga + getSession(true) + setAttribute("usuario_sessao", us)
    fun criar(context: Context, usuarioId: Int) {
        invalidar(context)
        prefs(context).edit()
            .putInt(USUARIO_SESSAO, usuarioId)
            .putLong(ULTIMO_ACESSO, System.currentTimeMillis())
            .apply()
    }

    // Equivale a: getAttribute("usuario_sessao"). Devolve 0 quando não há sessão válida.
    fun getUsuarioId(context: Context): Int {
        val prefs = prefs(context)
        val usuarioId = prefs.getInt(USUARIO_SESSAO, 0)
        if (usuarioId == 0) return 0

        // Se ficou parado mais tempo que o limite, a sessão expira
        val segundosInativo = (System.currentTimeMillis() - prefs.getLong(ULTIMO_ACESSO, 0)) / 1000
        if (segundosInativo > MAX_INACTIVE_INTERVAL) {
            invalidar(context)
            return 0
        }

        // Cada acesso renova o tempo da sessão, como acontece no servidor
        prefs.edit().putLong(ULTIMO_ACESSO, System.currentTimeMillis()).apply()
        return usuarioId
    }

    // Equivale a: removeAttribute("usuario_sessao") + invalidate() do doGetLogout
    fun invalidar(context: Context) {
        prefs(context).edit()
            .remove(USUARIO_SESSAO)
            .remove(ULTIMO_ACESSO)
            .apply()
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE)
}
