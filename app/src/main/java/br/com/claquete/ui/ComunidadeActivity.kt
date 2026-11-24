package br.com.claquete.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.claquete.adapter.UsuarioAdapter
import br.com.claquete.data.ClaqueteDatabase
import br.com.claquete.databinding.ActivityComunidadeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComunidadeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComunidadeBinding
    private lateinit var adapter: UsuarioAdapter
    private lateinit var db: ClaqueteDatabase
    private var usuarioId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComunidadeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = ClaqueteDatabase.getInstance(this)
        usuarioId = intent.getIntExtra("usuarioId", 0)
        setupRecyclerView()
        binding.btnVoltar.setOnClickListener { finish() }
        carregarUsuarios()
    }

    private fun setupRecyclerView() {
        adapter = UsuarioAdapter(listOf()) { usuario ->
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("perfilId", usuario.id)
            intent.putExtra("usuarioId", usuarioId)
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
