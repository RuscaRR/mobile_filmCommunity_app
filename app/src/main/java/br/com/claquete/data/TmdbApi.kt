package br.com.claquete.data

import br.com.claquete.BuildConfig
import br.com.claquete.model.Filme
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

// Busca filmes e séries na API do The Movie Database (TMDB).
// Deve ser chamado dentro de uma corrotina com Dispatchers.IO (acessa a internet).
object TmdbApi {
    private const val URL_BASE = "https://api.themoviedb.org/3"
    private const val URL_IMAGEM = "https://image.tmdb.org/t/p/w500"

    fun buscar(texto: String, idioma: String): List<Filme> {
        val busca = URLEncoder.encode(texto, "UTF-8")
        val endereco = "$URL_BASE/search/multi?api_key=${BuildConfig.TMDB_API_KEY}" +
                "&language=$idioma&query=$busca"

        val conexao = URL(endereco).openConnection() as HttpURLConnection
        val resposta = conexao.inputStream.bufferedReader().readText()
        conexao.disconnect()

        val resultados = JSONObject(resposta).getJSONArray("results")
        val filmes = mutableListOf<Filme>()

        for (i in 0 until resultados.length()) {
            val item = resultados.getJSONObject(i)
            val tipo = item.optString("media_type")

            // A busca "multi" também traz pessoas (atores), então ignoramos
            if (tipo != "movie" && tipo != "tv") continue

            // Filmes usam "title" e "release_date"; séries usam "name" e "first_air_date"
            val titulo = if (tipo == "movie") item.optString("title") else item.optString("name")
            val data = if (tipo == "movie") item.optString("release_date") else item.optString("first_air_date")
            val poster = if (item.isNull("poster_path")) "" else URL_IMAGEM + item.getString("poster_path")

            filmes.add(
                Filme(
                    tmdbId = item.getInt("id"),
                    tipo = tipo,
                    titulo = titulo,
                    ano = data.take(4),
                    sinopse = item.optString("overview"),
                    poster = poster
                )
            )
        }
        return filmes
    }
}
