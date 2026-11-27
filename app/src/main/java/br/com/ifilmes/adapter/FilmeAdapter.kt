package br.com.ifilmes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ifilmes.R
import br.com.ifilmes.databinding.ItemFilmeBinding
import br.com.ifilmes.model.Filme
import coil.load

class FilmeAdapter(
    private var filmes: List<Filme>,
    private val onClick: (Filme) -> Unit
) : RecyclerView.Adapter<FilmeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilmeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filmes[position])
    }

    override fun getItemCount(): Int = filmes.size

    fun updateLista(novosFilmes: List<Filme>) {
        this.filmes = novosFilmes
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemFilmeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(filme: Filme) {
            val contexto = binding.root.context
            val tipo = if (filme.tipo == "movie") {
                contexto.getString(R.string.tipo_filme)
            } else {
                contexto.getString(R.string.tipo_serie)
            }

            binding.tvTitulo.text = filme.titulo
            binding.tvInfo.text = contexto.getString(R.string.info_filme, tipo, filme.ano)
            binding.tvSinopse.text = filme.sinopse
            // Coil baixa a imagem do pôster pela URL do TMDB
            binding.imgPoster.load(filme.poster) {
                placeholder(R.drawable.ic_filme)
                error(R.drawable.ic_filme)
            }
            binding.root.setOnClickListener { onClick(filme) }
        }
    }
}
