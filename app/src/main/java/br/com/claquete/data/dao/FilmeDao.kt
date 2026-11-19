package br.com.claquete.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.claquete.model.Filme
import br.com.claquete.model.FilmeComNota

@Dao
interface FilmeDao {
    @Insert
    suspend fun inserir(filme: Filme): Long

    @Update
    suspend fun atualizar(filme: Filme)

    @Delete
    suspend fun deletar(filme: Filme)

    @Query("SELECT * FROM filme WHERE id = :id")
    suspend fun buscarPorId(id: Int): Filme?

    @Query("SELECT COUNT(*) FROM filme")
    suspend fun contar(): Int

    @Query(
        """
        SELECT filme.*,
               IFNULL(AVG(CASE WHEN avaliacao.nota > 0 THEN avaliacao.nota END), 0) AS mediaNotas,
               COUNT(CASE WHEN avaliacao.nota > 0 THEN 1 END) AS totalAvaliacoes
        FROM filme
        LEFT JOIN avaliacao ON avaliacao.filmeId = filme.id
        GROUP BY filme.id
        ORDER BY filme.titulo COLLATE NOCASE ASC
        """
    )
    suspend fun listarComNotas(): List<FilmeComNota>

    @Query(
        """
        SELECT filme.*,
               IFNULL(AVG(CASE WHEN avaliacao.nota > 0 THEN avaliacao.nota END), 0) AS mediaNotas,
               COUNT(CASE WHEN avaliacao.nota > 0 THEN 1 END) AS totalAvaliacoes
        FROM filme
        LEFT JOIN avaliacao ON avaliacao.filmeId = filme.id
        WHERE filme.id = :id
        GROUP BY filme.id
        """
    )
    suspend fun buscarComNota(id: Int): FilmeComNota?
}
