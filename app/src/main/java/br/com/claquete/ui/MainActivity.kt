package br.com.claquete.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.claquete.adapter.FilmeAdapter
import br.com.claquete.data.ClaqueteDatabase
import br.com.claquete.databinding.ActivityMainBinding
import br.com.claquete.model.Filme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FilmeAdapter
    private lateinit var launcherAtualizar: ActivityResultLauncher<Intent>
    private lateinit var db: ClaqueteDatabase
    private var filmes: List<Filme> = listOf()
    private var usuarioId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = ClaqueteDatabase.getInstance(this)
        usuarioId = intent.getIntExtra("usuarioId", 0)
        setupRecyclerView()
        setupLauncher()
        setupListeners()
        carregarFilmes()
    }

    private fun setupRecyclerView() {
        adapter = FilmeAdapter(filmes) { filme ->
            val intent = Intent(this, DetalheFilmeActivity::class.java)
            intent.putExtra("filmeId", filme.id)
            intent.putExtra("usuarioId", usuarioId)
            launcherAtualizar.launch(intent)
        }
        binding.rvFilmes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    // Quando a busca adiciona um filme ou o detalhe remove um, a lista é recarregada
    private fun setupLauncher() {
        launcherAtualizar = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                carregarFilmes()
            }
        }
    }

    private fun setupListeners() {
        binding.btnAdicionar.setOnClickListener {
            val intent = Intent(this, BuscaFilmeActivity::class.java)
            intent.putExtra("usuarioId", usuarioId)
            launcherAtualizar.launch(intent)
        }
        binding.btnComunidade.setOnClickListener {
            val intent = Intent(this, ComunidadeActivity::class.java)
            intent.putExtra("usuarioId", usuarioId)
            startActivity(intent)
        }
        binding.btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("perfilId", usuarioId)
            intent.putExtra("usuarioId", usuarioId)
            startActivity(intent)
        }
        binding.btnTema.setOnClickListener { mudarTema() }
        binding.btnIdioma.setOnClickListener { alternarIdioma() }

        binding.etFiltro.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrar()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun carregarFilmes() {
        lifecycleScope.launch(Dispatchers.IO) {
            filmes = db.filmeDao().listarTodos()
            withContext(Dispatchers.Main) {
                filtrar()
            }
        }
    }

    private fun filtrar() {
        val filtro = binding.etFiltro.text.toString().lowercase()
        val filtrados = filmes.filter {
            it.titulo.lowercase().contains(filtro)
        }
        adapter.updateLista(filtrados)
    }

    private fun mudarTema() {
        val isNight = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        val mode = if (isNight) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun alternarIdioma() {
        val idiomaAtual = AppCompatDelegate.getApplicationLocales()
        val usandoIngles = !idiomaAtual.isEmpty && idiomaAtual[0]?.language == "en"
        val proximoIdioma = if (usandoIngles) "pt" else "en"
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(proximoIdioma))
    }
}
