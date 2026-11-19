package br.com.claquete.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.claquete.data.dao.AvaliacaoDao
import br.com.claquete.data.dao.FilmeDao
import br.com.claquete.data.dao.SeguidorDao
import br.com.claquete.data.dao.UsuarioDao
import br.com.claquete.model.Avaliacao
import br.com.claquete.model.Filme
import br.com.claquete.model.Seguidor
import br.com.claquete.model.Usuario

@Database(
    entities = [Usuario::class, Filme::class, Avaliacao::class, Seguidor::class],
    version = 1,
    exportSchema = false
)
abstract class ClaqueteDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun filmeDao(): FilmeDao
    abstract fun avaliacaoDao(): AvaliacaoDao
    abstract fun seguidorDao(): SeguidorDao

    companion object {
        private const val NOME_BANCO = "claquete_database"

        @Volatile
        private var INSTANCE: ClaqueteDatabase? = null

        fun getInstance(context: Context): ClaqueteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ClaqueteDatabase::class.java,
                    NOME_BANCO
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
