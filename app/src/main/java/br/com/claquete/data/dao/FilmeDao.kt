package br.com.claquete.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.claquete.model.Filme

@Dao
interface FilmeDao {
    @Insert
    suspend fun inserir(filme: Filme)

    @Query("SELECT * FROM filme ORDER BY titulo ASC")
    suspend fun listarTodos(): List<Filme>

    @Query("SELECT * FROM filme WHERE id = :id")
    suspend fun buscarPorId(id: Int): Filme?

    @Query("SELECT * FROM filme WHERE tmdbId = :tmdbId AND tipo = :tipo")
    suspend fun buscarPorTmdbId(tmdbId: Int, tipo: String): Filme?

    @Delete
    suspend fun deletar(filme: Filme)
}
