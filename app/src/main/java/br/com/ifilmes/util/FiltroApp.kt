package br.com.ifilmes.util

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import br.com.ifilmes.R
import br.com.ifilmes.ui.LoginActivity

// Versão Android do FiltroApp.java: antes de abrir uma tela que exige login,
// confere se existe uma sessão. Se não existir, volta para o login com "faça o login!".
object FiltroApp {
    fun doFilter(activity: Activity): Boolean {
        if (Sessao.getUsuarioId(activity) != 0) {
            return true
        }
        Toast.makeText(activity, R.string.msg_faca_login, Toast.LENGTH_SHORT).show()
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
        activity.finish()
        return false
    }
}
