package br.com.ifilmes.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.ifilmes.R
import br.com.ifilmes.data.IFilmesDatabase
import br.com.ifilmes.databinding.ActivityLoginBinding
import br.com.ifilmes.model.Usuario
import br.com.ifilmes.util.Sessao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: IFilmesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se já existe uma sessão válida, o usuário continua logado e vai direto para o catálogo
        if (Sessao.getUsuarioId(this) != 0) {
            abrirCatalogo()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = IFilmesDatabase.getInstance(this)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnEntrar.setOnClickListener { entrar() }
        binding.btnCriarConta.setOnClickListener {
            val intent = Intent(this, CadastroUsuarioActivity::class.java)
            startActivity(intent)
        }
    }

    private fun entrar() {
        val email = binding.etEmail.text.toString().trim()
        val senha = binding.etSenha.text.toString()

        if (email.isBlank() || senha.isBlank()) {
            Toast.makeText(this, R.string.erro_campos, Toast.LENGTH_SHORT).show()
            return
        }

        // Gera o hash da senha digitada para comparar com o hash salvo no banco
        val senhaHash = Usuario.criptografarSenha(email, senha)

        lifecycleScope.launch(Dispatchers.IO) {
            val usuario = db.usuarioDao().buscarPorEmail(email)
            withContext(Dispatchers.Main) {
                if (usuario != null && usuario.senha == senhaHash) {
                    Sessao.criar(this@LoginActivity, usuario.id)
                    abrirCatalogo()
                } else {
                    Toast.makeText(this@LoginActivity, R.string.erro_login, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun abrirCatalogo() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
