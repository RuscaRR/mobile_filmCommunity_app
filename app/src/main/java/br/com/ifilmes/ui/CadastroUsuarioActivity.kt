package br.com.ifilmes.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.ifilmes.R
import br.com.ifilmes.data.IFilmesDatabase
import br.com.ifilmes.databinding.ActivityCadastroUsuarioBinding
import br.com.ifilmes.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastroUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroUsuarioBinding
    private lateinit var launcherGaleria: ActivityResultLauncher<Array<String>>
    private lateinit var db: IFilmesDatabase
    private var uriSelecionada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = IFilmesDatabase.getInstance(this)
        setupLauncher()
        setupListeners()
    }

    private fun setupLauncher() {
        launcherGaleria = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                uriSelecionada = uri.toString()
                binding.imgFoto.setImageURI(uri)
            }
        }
    }

    private fun setupListeners() {
        binding.imgFoto.setOnClickListener {
            launcherGaleria.launch(arrayOf("image/*"))
        }
        binding.btnSalvar.setOnClickListener { salvar() }
        binding.btnVoltar.setOnClickListener { finish() }
    }

    private fun salvar() {
        val nome = binding.etNome.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val senha = binding.etSenha.text.toString()

        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            Toast.makeText(this, R.string.erro_campos, Toast.LENGTH_SHORT).show()
            return
        }

        // A senha é salva como hash SHA-256, nunca em texto puro
        val senhaHash = Usuario.criptografarSenha(email, senha)
        val usuario = Usuario(nome = nome, email = email, senha = senhaHash, foto = uriSelecionada)

        lifecycleScope.launch(Dispatchers.IO) {
            val existente = db.usuarioDao().buscarPorEmail(email)
            if (existente == null) {
                db.usuarioDao().inserir(usuario)
            }
            withContext(Dispatchers.Main) {
                if (existente != null) {
                    Toast.makeText(this@CadastroUsuarioActivity, R.string.erro_email_existente, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CadastroUsuarioActivity, R.string.msg_conta_criada, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
