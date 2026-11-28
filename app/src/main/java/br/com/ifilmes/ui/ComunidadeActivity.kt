package br.com.ifilmes.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ifilmes.adapter.AvaliacaoAdapter
import br.com.ifilmes.data.IFilmesDatabase
import br.com.ifilmes.databinding.ActivityComunidadeBinding
import br.com.ifilmes.util.FiltroApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Feed da comunidade: mostra as avaliações mais recentes de todos os usuários
class ComunidadeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComunidadeBinding
    private lateinit var adapter: AvaliacaoAdapter
    private lateinit var db: IFilmesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!FiltroApp.doFilter(this)) return
        binding = ActivityComunidadeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = IFilmesDatabase.getInstance(this)
        setupRecyclerView()
        binding.btnVoltar.setOnClickListener { finish() }
    }

    // Recarrega toda vez que a tela volta a aparecer, para mostrar avaliações novas
    override fun onResume() {
        super.onResume()
        carregarFeed()
    }

    private fun setupRecyclerView() {
        adapter = AvaliacaoAdapter(listOf(), true, true) { avaliacao ->
            val intent = Intent(this, DetalheFilmeActivity::class.java)
            intent.putExtra("filmeId", avaliacao.filmeId)
            startActivity(intent)
        }
        binding.rvFeed.apply {
            layoutManager = LinearLayoutManager(this@ComunidadeActivity)
            adapter = this@ComunidadeActivity.adapter
        }
    }

    private fun carregarFeed() {
        lifecycleScope.launch(Dispatchers.IO) {
            val avaliacoes = db.avaliacaoDao().listarRecentes()
            withContext(Dispatchers.Main) {
                adapter.updateLista(avaliacoes)
                binding.tvFeedVazio.visibility = if (avaliacoes.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}
