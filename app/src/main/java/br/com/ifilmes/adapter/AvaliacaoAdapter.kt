package br.com.ifilmes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ifilmes.R
import br.com.ifilmes.databinding.ItemAvaliacaoBinding
import br.com.ifilmes.model.Avaliacao

// O título de cada item muda conforme a tela:
// mostrarUsuario e mostrarFilme = true: "Ana avaliou Interestelar" (feed da comunidade)
// só mostrarFilme = true: título do filme (tela de perfil)
// só mostrarUsuario = true: nome de quem avaliou (tela de detalhes)
class AvaliacaoAdapter(
    private var avaliacoes: List<Avaliacao>,
    private val mostrarUsuario: Boolean,
    private val mostrarFilme: Boolean,
    private val onClick: (Avaliacao) -> Unit
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
            val contexto = binding.root.context

            binding.tvNome.text = if (mostrarUsuario && mostrarFilme) {
                contexto.getString(R.string.feed_item, avaliacao.nomeUsuario, avaliacao.tituloFilme)
            } else if (mostrarFilme) {
                avaliacao.tituloFilme
            } else {
                avaliacao.nomeUsuario
            }
            binding.tvNota.text = contexto.getString(R.string.nota, avaliacao.nota)
            binding.tvComentario.text = avaliacao.comentario
            binding.root.setOnClickListener { onClick(avaliacao) }
        }
    }
}
