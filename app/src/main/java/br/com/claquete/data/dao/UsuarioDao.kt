package br.com.claquete.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.claquete.model.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun inserir(usuario: Usuario): Long

    @Update
    suspend fun atualizar(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE id = :id")
    suspend fun buscarPorId(id: Int): Usuario?

    @Query("SELECT * FROM usuario WHERE username = :login OR email = :login LIMIT 1")
    suspend fun buscarPorLogin(login: String): Usuario?

    @Query("SELECT COUNT(*) FROM usuario WHERE username = :username AND id != :ignorarId")
    suspend fun contarPorUsername(username: String, ignorarId: Int): Int

    @Query("SELECT COUNT(*) FROM usuario WHERE email = :email AND id != :ignorarId")
    suspend fun contarPorEmail(email: String, ignorarId: Int): Int

    @Query("SELECT COUNT(*) FROM usuario")
    suspend fun contar(): Int

    @Query("SELECT * FROM usuario WHERE id != :usuarioId ORDER BY nome COLLATE NOCASE ASC")
    suspend fun listarOutros(usuarioId: Int): List<Usuario>

    @Query(
        """
        SELECT usuario.* FROM usuario
        INNER JOIN seguidor ON seguidor.seguidorId = usuario.id
        WHERE seguidor.seguidoId = :usuarioId
        ORDER BY usuario.nome COLLATE NOCASE ASC
        """
    )
    suspend fun listarSeguidores(usuarioId: Int): List<Usuario>

    @Query(
        """
        SELECT usuario.* FROM usuario
        INNER JOIN seguidor ON seguidor.seguidoId = usuario.id
        WHERE seguidor.seguidorId = :usuarioId
        ORDER BY usuario.nome COLLATE NOCASE ASC
        """
    )
    suspend fun listarSeguindo(usuarioId: Int): List<Usuario>
}
