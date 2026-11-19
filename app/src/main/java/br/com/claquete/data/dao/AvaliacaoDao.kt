package br.com.claquete.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import br.com.claquete.model.Avaliacao
import br.com.claquete.model.AvaliacaoDetalhada

@Dao
interface AvaliacaoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(avaliacao: Avaliacao)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarTodas(avaliacoes: List<Avaliacao>)

    @Delete
    suspend fun deletar(avaliacao: Avaliacao)

    @Query("SELECT * FROM avaliacao WHERE usuarioId = :usuarioId AND filmeId = :filmeId")
    suspend fun buscar(usuarioId: Int, filmeId: Int): Avaliacao?

    @Transaction
    @Query(
        """
        SELECT * FROM avaliacao
        WHERE filmeId = :filmeId AND (nota > 0 OR resenha != '')
        ORDER BY data DESC
        """
    )
    suspend fun listarPorFilme(filmeId: Int): List<AvaliacaoDetalhada>

    @Transaction
    @Query(
        """
        SELECT * FROM avaliacao
        WHERE usuarioId = :usuarioId AND (nota > 0 OR resenha != '')
        ORDER BY data DESC
        """
    )
    suspend fun listarPorUsuario(usuarioId: Int): List<AvaliacaoDetalhada>

    @Transaction
    @Query(
        """
        SELECT * FROM avaliacao
        WHERE usuarioId IN (SELECT seguidoId FROM seguidor WHERE seguidorId = :usuarioId)
          AND (nota > 0 OR resenha != '')
        ORDER BY data DESC
        LIMIT 20
        """
    )
    suspend fun listarFeed(usuarioId: Int): List<AvaliacaoDetalhada>

    @Query("SELECT filmeId FROM avaliacao WHERE usuarioId = :usuarioId AND favorito = 1 ORDER BY data DESC")
    suspend fun listarIdsFavoritos(usuarioId: Int): List<Int>

    @Query("SELECT COUNT(*) FROM avaliacao WHERE usuarioId = :usuarioId AND (nota > 0 OR resenha != '')")
    suspend fun contarPorUsuario(usuarioId: Int): Int
}
