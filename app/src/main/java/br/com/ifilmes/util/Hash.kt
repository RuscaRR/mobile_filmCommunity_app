package br.com.ifilmes.util

import java.math.BigInteger
import java.security.MessageDigest

// Mesma lógica do framework.util.Hash do projeto Java:
// transforma um texto em hash usando o algoritmo escolhido (SHA-256, SHA-1, MD5...)
object Hash {
    fun stringToHash(string: String, algorithm: String): String {
        val md = MessageDigest.getInstance(algorithm)
        return BigInteger(1, md.digest(string.toByteArray(Charsets.UTF_8))).toString(16)
    }
}
