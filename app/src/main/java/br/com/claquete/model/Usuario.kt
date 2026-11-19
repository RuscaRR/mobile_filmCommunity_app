package br.com.claquete.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "usuario",
    indices = [
        Index(value = ["username"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String,
    val username: String,
    val email: String,
    val senha: String,
    val foto: String = "",
    val bio: String = "",
    val dataCadastro: Long = System.currentTimeMillis()
) : Serializable
