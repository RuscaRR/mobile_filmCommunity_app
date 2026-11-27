package br.com.ifilmes.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ifilmes.adapter.UsuarioAdapter
import br.com.ifilmes.data.IFilmesDatabase
import br.com.ifilmes.databinding.ActivityComunidadeBinding
import br.com.ifilmes.util.FiltroApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComunidadeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComunidadeBinding
    private lateinit var adapter: UsuarioAdapter
    private lateinit var db: IFilmesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!FiltroApp.doFilter(this)) return
        binding = ActivityComunidadeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = IFilmesDatabase.getInstance(this)
        setupRecyclerView()
        binding.btnVoltar.setOnClickListener { finish() }
        carregarUsuarios()
    }

    private fun setupRecyclerView() {
        adapter = UsuarioAdapter(listOf()) { usuario ->
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("perfilId", usuario.id)
            startActivity(intent)
        }
        binding.rvUsuarios.apply {
            layoutManager = LinearLayoutManager(this@ComunidadeActivity)
            adapter = this@ComunidadeActivity.adapter
        }
    }

    private fun carregarUsuarios() {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuarios = db.usuarioDao().listarTodos()
            withContext(Dispatchers.Main) {
                adapter.updateLista(usuarios)
            }
        }
    }
}
