package br.com.ifilmes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.ifilmes.util.Hash
import java.io.Serializable

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val email: String,
    val senha: String, // guarda o hash SHA-256, nunca a senha digitada
    val foto: String = ""
) : Serializable {

    companion object {
        // Igual ao setSenha() do Usuario.java: junta um dado do usuário com a senha
        // antes de gerar o hash. No Java era o id; aqui é o e-mail, porque o id
        // só é criado pelo Room depois que o usuário é salvo.
        fun criptografarSenha(email: String, senha: String): String {
            return Hash.stringToHash(email + senha, "SHA-256")
        }
    }
}
