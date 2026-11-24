package br.com.claquete.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.claquete.R
import br.com.claquete.data.ClaqueteDatabase
import br.com.claquete.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: ClaqueteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = ClaqueteDatabase.getInstance(this)
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

        lifecycleScope.launch(Dispatchers.IO) {
            val usuario = db.usuarioDao().buscarPorEmail(email)
            withContext(Dispatchers.Main) {
                if (usuario != null && usuario.senha == senha) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("usuarioId", usuario.id)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, R.string.erro_login, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
