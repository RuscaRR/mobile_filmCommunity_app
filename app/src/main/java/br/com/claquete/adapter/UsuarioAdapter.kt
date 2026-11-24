package br.com.claquete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import br.com.claquete.R
import br.com.claquete.databinding.ItemUsuarioBinding
import br.com.claquete.model.Usuario

class UsuarioAdapter(
    private var usuarios: List<Usuario>,
    private val onClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsuarioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(usuarios[position])
    }

    override fun getItemCount(): Int = usuarios.size

    fun updateLista(novosUsuarios: List<Usuario>) {
        this.usuarios = novosUsuarios
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemUsuarioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(usuario: Usuario) {
            binding.tvNome.text = usuario.nome
            binding.tvEmail.text = usuario.email
            if (usuario.foto.isEmpty()) {
                binding.imgFoto.setImageResource(R.drawable.ic_usuario)
            } else {
                binding.imgFoto.setImageURI(usuario.foto.toUri())
            }
            binding.root.setOnClickListener { onClick(usuario) }
        }
    }
}
