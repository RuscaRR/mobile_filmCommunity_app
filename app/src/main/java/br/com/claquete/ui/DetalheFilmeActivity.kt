package br.com.claquete.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.claquete.R
import br.com.claquete.adapter.AvaliacaoAdapter
import br.com.claquete.data.ClaqueteDatabase
import br.com.claquete.databinding.ActivityDetalheFilmeBinding
import br.com.claquete.model.Avaliacao
import br.com.claquete.model.Filme
import br.com.claquete.model.Usuario
import coil.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalheFilmeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalheFilmeBinding
    private lateinit var adapter: AvaliacaoAdapter
    private lateinit var db: ClaqueteDatabase
    private var filme: Filme? = null
    private var usuario: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalheFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = ClaqueteDatabase.getInstance(this)
        setupRecyclerView()
        setupListeners()
        carregarDados()
    }

    private fun setupRecyclerView() {
        adapter = AvaliacaoAdapter(listOf(), false)
        binding.rvAvaliacoes.apply {
            layoutManager = LinearLayoutManager(this@DetalheFilmeActivity)
            adapter = this@DetalheFilmeActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.btnVerTmdb.setOnClickListener { abrirNoTmdb() }
        binding.btnAvaliar.setOnClickListener { avaliar() }
        binding.btnRemover.setOnClickListener { remover() }
        binding.btnVoltar.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    // Busca no banco o filme e o usuário logado, usando os ids recebidos pela Intent
    private fun carregarDados() {
        val filmeId = intent.getIntExtra("filmeId", 0)
        val usuarioId = intent.getIntExtra("usuarioId", 0)

        lifecycleScope.launch(Dispatchers.IO) {
            filme = db.filmeDao().buscarPorId(filmeId)
            usuario = db.usuarioDao().buscarPorId(usuarioId)
            withContext(Dispatchers.Main) {
                mostrarFilme()
            }
            carregarAvaliacoes()
        }
    }

    private fun mostrarFilme() {
        val filme = filme ?: return
        val tipo = if (filme.tipo == "movie") getString(R.string.tipo_filme) else getString(R.string.tipo_serie)

        binding.tvTitulo.text = filme.titulo
        binding.tvInfo.text = getString(R.string.info_filme, tipo, filme.ano)
        binding.tvSinopse.text = filme.sinopse.ifBlank { getString(R.string.sem_sinopse) }
        binding.imgPoster.load(filme.poster) {
            placeholder(R.drawable.ic_filme)
            error(R.drawable.ic_filme)
        }

        // Só quem adicionou o filme pode removê-lo do catálogo
        if (filme.usuarioId == usuario?.id) {
            binding.btnRemover.visibility = View.VISIBLE
        } else {
            binding.btnRemover.visibility = View.GONE
        }
    }

    private suspend fun carregarAvaliacoes() {
        val filme = filme ?: return
        val avaliacoes = db.avaliacaoDao().listarPorFilme(filme.id)
        withContext(Dispatchers.Main) {
            adapter.updateLista(avaliacoes)
            if (avaliacoes.isEmpty()) {
                binding.tvMedia.text = getString(R.string.sem_avaliacoes)
            } else {
                val media = avaliacoes.map { it.nota }.average()
                binding.tvMedia.text = getString(R.string.media_notas, media, avaliacoes.size)
            }
        }
    }

    // Intent implícita: abre a página do filme no navegador
    private fun abrirNoTmdb() {
        val filme = filme ?: return
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = "https://www.themoviedb.org/${filme.tipo}/${filme.tmdbId}".toUri()
        startActivity(intent)
    }

    private fun avaliar() {
        val filme = filme ?: return
        val usuario = usuario ?: return
        val nota = binding.rbNota.rating
        val comentario = binding.etComentario.text.toString().trim()

        if (nota == 0f) {
            Toast.makeText(this, R.string.erro_nota, Toast.LENGTH_SHORT).show()
            return
        }

        val avaliacao = Avaliacao(
            filmeId = filme.id,
            usuarioId = usuario.id,
            nomeUsuario = usuario.nome,
            tituloFilme = filme.titulo,
            nota = nota,
            comentario = comentario
        )

        lifecycleScope.launch(Dispatchers.IO) {
            db.avaliacaoDao().inserir(avaliacao)
            withContext(Dispatchers.Main) {
                binding.rbNota.rating = 0f
                binding.etComentario.text.clear()
                Toast.makeText(this@DetalheFilmeActivity, R.string.msg_avaliacao_salva, Toast.LENGTH_SHORT).show()
            }
            carregarAvaliacoes()
        }
    }

    private fun remover() {
        val filme = filme ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            db.avaliacaoDao().deletarPorFilme(filme.id)
            db.filmeDao().deletar(filme)
            withContext(Dispatchers.Main) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}
