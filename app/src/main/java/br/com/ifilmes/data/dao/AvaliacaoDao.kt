package br.com.ifilmes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.ifilmes.model.Avaliacao

@Dao
interface AvaliacaoDao {
    @Insert
    suspend fun inserir(avaliacao: Avaliacao)

    @Query("SELECT * FROM avaliacao WHERE filmeId = :filmeId ORDER BY id DESC")
    suspend fun listarPorFilme(filmeId: Int): List<Avaliacao>

    @Query("SELECT * FROM avaliacao WHERE usuarioId = :usuarioId ORDER BY id DESC")
    suspend fun listarPorUsuario(usuarioId: Int): List<Avaliacao>

    // Feed da comunidade: as avaliações mais novas de todos os usuários
    @Query("SELECT * FROM avaliacao ORDER BY id DESC LIMIT 50")
    suspend fun listarRecentes(): List<Avaliacao>

    @Query("DELETE FROM avaliacao WHERE filmeId = :filmeId")
    suspend fun deletarPorFilme(filmeId: Int)
}
