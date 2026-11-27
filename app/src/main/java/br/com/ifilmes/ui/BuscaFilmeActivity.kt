package br.com.ifilmes.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ifilmes.R
import br.com.ifilmes.adapter.FilmeAdapter
import br.com.ifilmes.data.IFilmesDatabase
import br.com.ifilmes.data.TmdbApi
import br.com.ifilmes.databinding.ActivityBuscaFilmeBinding
import br.com.ifilmes.model.Filme
import br.com.ifilmes.util.FiltroApp
import br.com.ifilmes.util.Sessao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuscaFilmeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuscaFilmeBinding
    private lateinit var adapter: FilmeAdapter
    private lateinit var db: IFilmesDatabase
    private var usuarioId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!FiltroApp.doFilter(this)) return
        binding = ActivityBuscaFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = IFilmesDatabase.getInstance(this)
        usuarioId = Sessao.getUsuarioId(this)
        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = FilmeAdapter(listOf()) { filme ->
            adicionarAoCatalogo(filme)
        }
        binding.rvResultados.apply {
            layoutManager = LinearLayoutManager(this@BuscaFilmeActivity)
            adapter = this@BuscaFilmeActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.btnBuscar.setOnClickListener { buscar() }
        binding.btnVoltar.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun buscar() {
        val texto = binding.etBusca.text.toString().trim()
        if (texto.isBlank()) {
            Toast.makeText(this, R.string.erro_campos, Toast.LENGTH_SHORT).show()
            return
        }

        val idioma = getString(R.string.idioma_api)
        binding.pbCarregando.visibility = View.VISIBLE

        // A busca acessa a internet, então roda fora da thread principal
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val resultados = TmdbApi.buscar(texto, idioma)
                withContext(Dispatchers.Main) {
                    binding.pbCarregando.visibility = View.GONE
                    adapter.updateLista(resultados)
                    if (resultados.isEmpty()) {
                        Toast.makeText(this@BuscaFilmeActivity, R.string.msg_nenhum_resultado, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.pbCarregando.visibility = View.GONE
                    Toast.makeText(this@BuscaFilmeActivity, R.string.erro_busca, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun adicionarAoCatalogo(filme: Filme) {
        lifecycleScope.launch(Dispatchers.IO) {
            val existente = db.filmeDao().buscarPorTmdbId(filme.tmdbId, filme.tipo)
            if (existente == null) {
                db.filmeDao().inserir(filme.copy(usuarioId = usuarioId))
            }
            withContext(Dispatchers.Main) {
                if (existente != null) {
                    Toast.makeText(this@BuscaFilmeActivity, R.string.msg_ja_no_catalogo, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@BuscaFilmeActivity, R.string.msg_adicionado, Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }
}
