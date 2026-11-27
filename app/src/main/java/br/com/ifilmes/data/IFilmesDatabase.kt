package br.com.ifilmes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.ifilmes.R
import br.com.ifilmes.data.dao.AvaliacaoDao
import br.com.ifilmes.data.dao.FilmeDao
import br.com.ifilmes.data.dao.UsuarioDao
import br.com.ifilmes.model.Avaliacao
import br.com.ifilmes.model.Filme
import br.com.ifilmes.model.Usuario

@Database(entities = [Usuario::class, Filme::class, Avaliacao::class], version = 1)
abstract class IFilmesDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun filmeDao(): FilmeDao
    abstract fun avaliacaoDao(): AvaliacaoDao

    companion object {
        @Volatile
        private var INSTANCE: IFilmesDatabase? = null

        fun getInstance(context: Context): IFilmesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IFilmesDatabase::class.java,
                    "ifilmes_database"
                ).addCallback(filmesIniciais(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Roda só uma vez, quando o banco é criado na primeira abertura do app.
        // Insere 10 filmes populares de 2026 para o catálogo não começar vazio.
        // O INSERT usa "?" nos valores, como o PreparedStatement dos DAOs do projeto Java.
        private fun filmesIniciais(context: Context) = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val filmes = listOf(
                    filmeInicial(context, 969681, "x0nvYzQpyJc5pdT9lMnkMuYAg0O.jpg", R.string.filme_homem_aranha, R.string.sinopse_homem_aranha),
                    filmeInicial(context, 1368337, "m0ehGErq8GTLK4WZxaq9QLGAR3u.jpg", R.string.filme_odisseia, R.string.sinopse_odisseia),
                    filmeInicial(context, 1084244, "jTN1hdfP88Km63waezqIN1uUqT3.jpg", R.string.filme_toy_story, R.string.sinopse_toy_story),
                    filmeInicial(context, 936075, "gKWVxVJ5Q0JcQKj2Z8Hu5Qwo7s2.jpg", R.string.filme_michael, R.string.sinopse_michael),
                    filmeInicial(context, 1226863, "b3WeTp42eJSRuE4UZfyPCOJW4c.jpg", R.string.filme_mario, R.string.sinopse_mario),
                    filmeInicial(context, 1314481, "50yWyY981TyUHhoxxSEKwO70FmQ.jpg", R.string.filme_diabo_prada, R.string.sinopse_diabo_prada),
                    filmeInicial(context, 687163, "2i8uru7rlbHKaoIbC2V4FZLT7uW.jpg", R.string.filme_devoradores, R.string.sinopse_devoradores),
                    filmeInicial(context, 1315772, "hTowtXrkCY7FJyoj4p91JckrJSE.jpg", R.string.filme_minions, R.string.sinopse_minions),
                    filmeInicial(context, 1228710, "dNwaS0tnwgQRaQFPY5MbGxdmYXr.jpg", R.string.filme_mandaloriano, R.string.sinopse_mandaloriano),
                    filmeInicial(context, 1327819, "ftPFJBGbldoWiiPrmesW96zBzdH.jpg", R.string.filme_hoppers, R.string.sinopse_hoppers)
                )

                filmes.forEach { filme ->
                    db.execSQL(
                        "INSERT INTO filme (tmdbId, tipo, titulo, ano, sinopse, poster, usuarioId) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        arrayOf(filme.tmdbId, filme.tipo, filme.titulo, filme.ano, filme.sinopse, filme.poster, filme.usuarioId)
                    )
                }
            }
        }

        // Monta o Filme com os textos do strings.xml (no idioma do celular) e o pôster do TMDB.
        // usuarioId = 0: filme do sistema, por isso nenhum usuário vê o botão Remover.
        private fun filmeInicial(context: Context, tmdbId: Int, poster: String, titulo: Int, sinopse: Int) = Filme(
            tmdbId = tmdbId,
            tipo = "movie",
            titulo = context.getString(titulo),
            ano = "2026",
            sinopse = context.getString(sinopse),
            poster = "https://image.tmdb.org/t/p/w500/$poster",
            usuarioId = 0
        )
    }
}
