package br.com.ifilmes.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ifilmes.R
import br.com.ifilmes.adapter.AvaliacaoAdapter
import br.com.ifilmes.data.IFilmesDatabase
import br.com.ifilmes.databinding.ActivityPerfilBinding
import br.com.ifilmes.util.FiltroApp
import br.com.ifilmes.util.Sessao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding
    private lateinit var adapter: AvaliacaoAdapter
    private lateinit var db: IFilmesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!FiltroApp.doFilter(this)) return
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = IFilmesDatabase.getInstance(this)
        setupRecyclerView()
        setupListeners()
        carregarPerfil()
    }

    private fun setupRecyclerView() {
        adapter = AvaliacaoAdapter(listOf(), true)
        binding.rvAvaliacoes.apply {
            layoutManager = LinearLayoutManager(this@PerfilActivity)
            adapter = this@PerfilActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.btnVoltar.setOnClickListener { finish() }
        binding.btnSair.setOnClickListener {
            Sessao.invalidar(this) // logout: apaga a sessão, como o doGetLogout do Java
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity() // fecha todas as telas abertas do app
        }
    }

    private fun carregarPerfil() {
        // perfilId = dono do perfil exibido; usuarioId = quem está logado (vem da sessão)
        val perfilId = intent.getIntExtra("perfilId", 0)
        val usuarioId = Sessao.getUsuarioId(this)

        // O botão Sair só aparece no perfil do próprio usuário
        if (perfilId == usuarioId) {
            binding.btnSair.visibility = View.VISIBLE
        } else {
            binding.btnSair.visibility = View.GONE
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val usuario = db.usuarioDao().buscarPorId(perfilId)
            val avaliacoes = db.avaliacaoDao().listarPorUsuario(perfilId)
            withContext(Dispatchers.Main) {
                if (usuario != null) {
                    binding.tvNome.text = usuario.nome
                    binding.tvEmail.text = usuario.email
                    if (usuario.foto.isNotEmpty()) {
                        binding.imgFoto.setImageURI(usuario.foto.toUri())
                    }
                }
                binding.tvTotal.text = getString(R.string.total_avaliacoes, avaliacoes.size)
                adapter.updateLista(avaliacoes)
            }
        }
    }
}
