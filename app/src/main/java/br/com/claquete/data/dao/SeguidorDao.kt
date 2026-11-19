package br.com.claquete.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.claquete.model.Seguidor

@Dao
interface SeguidorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun seguir(seguidor: Seguidor)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun seguirTodos(seguidores: List<Seguidor>)

    @Query("DELETE FROM seguidor WHERE seguidorId = :seguidorId AND seguidoId = :seguidoId")
    suspend fun deixarDeSeguir(seguidorId: Int, seguidoId: Int)

    @Query("SELECT COUNT(*) FROM seguidor WHERE seguidorId = :seguidorId AND seguidoId = :seguidoId")
    suspend fun contarRelacao(seguidorId: Int, seguidoId: Int): Int

    @Query("SELECT COUNT(*) FROM seguidor WHERE seguidoId = :usuarioId")
    suspend fun contarSeguidores(usuarioId: Int): Int

    @Query("SELECT COUNT(*) FROM seguidor WHERE seguidorId = :usuarioId")
    suspend fun contarSeguindo(usuarioId: Int): Int

    @Query("SELECT seguidoId FROM seguidor WHERE seguidorId = :usuarioId")
    suspend fun listarIdsSeguindo(usuarioId: Int): List<Int>
}
