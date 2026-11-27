package br.com.ifilmes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ifilmes.R
import br.com.ifilmes.databinding.ItemAvaliacaoBinding
import br.com.ifilmes.model.Avaliacao

// mostrarFilme = true: mostra o título do filme (tela de perfil)
// mostrarFilme = false: mostra o nome de quem avaliou (tela de detalhes)
class AvaliacaoAdapter(
    private var avaliacoes: List<Avaliacao>,
    private val mostrarFilme: Boolean
) : RecyclerView.Adapter<AvaliacaoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAvaliacaoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(avaliacoes[position])
    }

    override fun getItemCount(): Int = avaliacoes.size

    fun updateLista(novasAvaliacoes: List<Avaliacao>) {
        this.avaliacoes = novasAvaliacoes
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemAvaliacaoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(avaliacao: Avaliacao) {
            binding.tvNome.text = if (mostrarFilme) avaliacao.tituloFilme else avaliacao.nomeUsuario
            binding.tvNota.text = binding.root.context.getString(R.string.nota, avaliacao.nota)
            binding.tvComentario.text = avaliacao.comentario
        }
    }
}
