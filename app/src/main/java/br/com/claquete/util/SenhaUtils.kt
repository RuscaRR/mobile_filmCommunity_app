package br.com.claquete.util

import java.security.MessageDigest

object SenhaUtils {
    private const val ALGORITMO = "SHA-256"

    fun gerarHash(senha: String): String {
        val bytes = MessageDigest.getInstance(ALGORITMO).digest(senha.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
