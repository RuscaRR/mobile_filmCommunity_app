package br.com.ifilmes.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ifilmes.R
import br.com.ifilmes.adapter.FilmeAdapter
import br.com.ifilmes.data.IFilmesDatabase
import br.com.ifilmes.databinding.ActivityMainBinding
import br.com.ifilmes.model.Filme
import br.com.ifilmes.util.FiltroApp
import br.com.ifilmes.util.Sessao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterLancamentos: FilmeAdapter
    private lateinit var adapterAdicionados: FilmeAdapter
    private lateinit var launcherAtualizar: ActivityResultLauncher<Intent>
    private lateinit var db: IFilmesDatabase
    private var filmes: List<Filme> = listOf()
    private var usuarioId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!FiltroApp.doFilter(this)) return
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = IFilmesDatabase.getInstance(this)
        usuarioId = Sessao.getUsuarioId(this)
        setupRecyclerView()
        setupLauncher()
        setupListeners()
        carregarFilmes()
    }

    // Duas listas na tela: os lançamentos que já vêm no app e os filmes adicionados pela busca
    private fun setupRecyclerView() {
        adapterLancamentos = FilmeAdapter(listOf()) { filme -> abrirDetalhe(filme) }
        binding.rvLancamentos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterLancamentos
        }

        adapterAdicionados = FilmeAdapter(listOf()) { filme -> abrirDetalhe(filme) }
        binding.rvAdicionados.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterAdicionados
        }
    }

    private fun abrirDetalhe(filme: Filme) {
        val intent = Intent(this, DetalheFilmeActivity::class.java)
        intent.putExtra("filmeId", filme.id)
        launcherAtualizar.launch(intent)
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
            launcherAtualizar.launch(intent)
        }
        binding.btnComunidade.setOnClickListener {
            val intent = Intent(this, ComunidadeActivity::class.java)
            startActivity(intent)
        }
        binding.btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("perfilId", usuarioId)
            startActivity(intent)
        }
        binding.btnConfig.setOnClickListener { abrirConfiguracoes() }

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

        // usuarioId = 0 são os filmes do sistema (Lançamentos 2026);
        // os outros foram adicionados pelos usuários na busca (Adicionados pela comunidade)
        val lancamentos = filtrados.filter { it.usuarioId == 0 }
        val adicionados = filtrados.filter { it.usuarioId != 0 }

        adapterLancamentos.updateLista(lancamentos)
        adapterAdicionados.updateLista(adicionados)

        binding.tvLancamentos.visibility = if (lancamentos.isEmpty()) View.GONE else View.VISIBLE
        binding.tvListaVazia.visibility = if (adicionados.isEmpty()) View.VISIBLE else View.GONE
    }

    // A engrenagem abre um menu com as duas opções: tema e idioma
    private fun abrirConfiguracoes() {
        val menu = PopupMenu(this, binding.btnConfig)
        menu.menuInflater.inflate(R.menu.menu_config, menu.menu)
        menu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuTema -> mudarTema()
                R.id.menuIdioma -> alternarIdioma()
            }
            true
        }
        menu.show()
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
