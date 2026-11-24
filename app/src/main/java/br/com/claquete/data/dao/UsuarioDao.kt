package br.com.claquete.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.claquete.model.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun inserir(usuario: Usuario)

    @Query("SELECT * FROM usuario ORDER BY nome ASC")
    suspend fun listarTodos(): List<Usuario>

    @Query("SELECT * FROM usuario WHERE id = :id")
    suspend fun buscarPorId(id: Int): Usuario?

    @Query("SELECT * FROM usuario WHERE email = :email")
    suspend fun buscarPorEmail(email: String): Usuario?
}
