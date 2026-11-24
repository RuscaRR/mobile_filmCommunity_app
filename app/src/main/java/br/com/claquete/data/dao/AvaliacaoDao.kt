package br.com.claquete.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.claquete.model.Avaliacao

@Dao
interface AvaliacaoDao {
    @Insert
    suspend fun inserir(avaliacao: Avaliacao)

    @Query("SELECT * FROM avaliacao WHERE filmeId = :filmeId ORDER BY id DESC")
    suspend fun listarPorFilme(filmeId: Int): List<Avaliacao>

    @Query("SELECT * FROM avaliacao WHERE usuarioId = :usuarioId ORDER BY id DESC")
    suspend fun listarPorUsuario(usuarioId: Int): List<Avaliacao>

    @Query("DELETE FROM avaliacao WHERE filmeId = :filmeId")
    suspend fun deletarPorFilme(filmeId: Int)
}
