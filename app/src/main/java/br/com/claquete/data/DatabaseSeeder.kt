package br.com.claquete.data

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import br.com.claquete.R
import br.com.claquete.model.Avaliacao
import br.com.claquete.model.Filme
import br.com.claquete.model.Genero
import br.com.claquete.model.Seguidor
import br.com.claquete.model.Usuario
import br.com.claquete.util.SenhaUtils

object DatabaseSeeder {
    private const val TAG = "DatabaseSeeder"
    private const val SENHA_DEMO = "123456"
    private const val UM_DIA = 24L * 60 * 60 * 1000

    suspend fun popularSeVazio(context: Context, db: ClaqueteDatabase) {
        if (db.usuarioDao().contar() > 0) return

        Log.d(TAG, "Banco vazio: inserindo dados iniciais")

        db.withTransaction {
            inserirDados(context, db)
        }
    }

    private suspend fun inserirDados(context: Context, db: ClaqueteDatabase) {
        val agora = System.currentTimeMillis()
        val senha = SenhaUtils.gerarHash(SENHA_DEMO)
        val usuarioDao = db.usuarioDao()
        val filmeDao = db.filmeDao()

        val ana = usuarioDao.inserir(
            Usuario(
                nome = "Ana Souza", username = "ana", email = "ana@claquete.app",
                senha = senha, bio = context.getString(R.string.seed_bio_ana)
            )
        ).toInt()
        val bruno = usuarioDao.inserir(
            Usuario(
                nome = "Bruno Lima", username = "bruno", email = "bruno@claquete.app",
                senha = senha, bio = context.getString(R.string.seed_bio_bruno)
            )
        ).toInt()
        val carla = usuarioDao.inserir(
            Usuario(
                nome = "Carla Mendes", username = "carla", email = "carla@claquete.app",
                senha = senha, bio = context.getString(R.string.seed_bio_carla)
            )
        ).toInt()
        val diego = usuarioDao.inserir(
            Usuario(
                nome = "Diego Rocha", username = "diego", email = "diego@claquete.app",
                senha = senha, bio = context.getString(R.string.seed_bio_diego)
            )
        ).toInt()

        fun poster(nome: String) = "android.resource://${context.packageName}/drawable/$nome"
        fun cadastradoHa(dias: Int) = agora - dias * UM_DIA

        val cidadeDeDeus = filmeDao.inserir(
            Filme(
                titulo = "Cidade de Deus", diretor = "Fernando Meirelles", ano = 2002,
                genero = Genero.CRIME.name, duracao = 130,
                sinopse = context.getString(R.string.seed_sinopse_cidade_de_deus),
                poster = poster("poster_cidade_de_deus"), usuarioId = ana,
                dataCadastro = cadastradoHa(30)
            )
        ).toInt()
        val centralDoBrasil = filmeDao.inserir(
            Filme(
                titulo = "Central do Brasil", diretor = "Walter Salles", ano = 1998,
                genero = Genero.DRAMA.name, duracao = 113,
                sinopse = context.getString(R.string.seed_sinopse_central_do_brasil),
                poster = poster("poster_central_do_brasil"), usuarioId = ana,
                dataCadastro = cadastradoHa(29)
            )
        ).toInt()
        val autoDaCompadecida = filmeDao.inserir(
            Filme(
                titulo = "O Auto da Compadecida", diretor = "Guel Arraes", ano = 2000,
                genero = Genero.COMEDIA.name, duracao = 104,
                sinopse = context.getString(R.string.seed_sinopse_auto_da_compadecida),
                poster = poster("poster_auto_da_compadecida"), usuarioId = carla,
                dataCadastro = cadastradoHa(27)
            )
        ).toInt()
        val interestelar = filmeDao.inserir(
            Filme(
                titulo = "Interestelar", diretor = "Christopher Nolan", ano = 2014,
                genero = Genero.FICCAO_CIENTIFICA.name, duracao = 169,
                sinopse = context.getString(R.string.seed_sinopse_interestelar),
                poster = poster("poster_interestelar"), usuarioId = bruno,
                dataCadastro = cadastradoHa(25)
            )
        ).toInt()
        val parasita = filmeDao.inserir(
            Filme(
                titulo = "Parasita", diretor = "Bong Joon-ho", ano = 2019,
                genero = Genero.SUSPENSE.name, duracao = 132,
                sinopse = context.getString(R.string.seed_sinopse_parasita),
                poster = poster("poster_parasita"), usuarioId = carla,
                dataCadastro = cadastradoHa(22)
            )
        ).toInt()
        val viagemDeChihiro = filmeDao.inserir(
            Filme(
                titulo = "A Viagem de Chihiro", diretor = "Hayao Miyazaki", ano = 2001,
                genero = Genero.ANIMACAO.name, duracao = 125,
                sinopse = context.getString(R.string.seed_sinopse_viagem_de_chihiro),
                poster = poster("poster_viagem_de_chihiro"), usuarioId = ana,
                dataCadastro = cadastradoHa(20)
            )
        ).toInt()
        val poderosoChefao = filmeDao.inserir(
            Filme(
                titulo = "O Poderoso Chefão", diretor = "Francis Ford Coppola", ano = 1972,
                genero = Genero.CRIME.name, duracao = 175,
                sinopse = context.getString(R.string.seed_sinopse_poderoso_chefao),
                poster = poster("poster_poderoso_chefao"), usuarioId = bruno,
                dataCadastro = cadastradoHa(18)
            )
        ).toInt()
        val aindaEstouAqui = filmeDao.inserir(
            Filme(
                titulo = "Ainda Estou Aqui", diretor = "Walter Salles", ano = 2024,
                genero = Genero.DRAMA.name, duracao = 137,
                sinopse = context.getString(R.string.seed_sinopse_ainda_estou_aqui),
                poster = poster("poster_ainda_estou_aqui"), usuarioId = ana,
                dataCadastro = cadastradoHa(15)
            )
        ).toInt()
        val bacurau = filmeDao.inserir(
            Filme(
                titulo = "Bacurau", diretor = "Kleber Mendonça Filho, Juliano Dornelles", ano = 2019,
                genero = Genero.SUSPENSE.name, duracao = 131,
                sinopse = context.getString(R.string.seed_sinopse_bacurau),
                poster = poster("poster_bacurau"), usuarioId = carla,
                dataCadastro = cadastradoHa(12)
            )
        ).toInt()
        val toyStory = filmeDao.inserir(
            Filme(
                titulo = "Toy Story", diretor = "John Lasseter", ano = 1995,
                genero = Genero.ANIMACAO.name, duracao = 81,
                sinopse = context.getString(R.string.seed_sinopse_toy_story),
                poster = poster("poster_toy_story"), usuarioId = diego,
                dataCadastro = cadastradoHa(9)
            )
        ).toInt()
        val matrix = filmeDao.inserir(
            Filme(
                titulo = "Matrix", diretor = "Lana Wachowski, Lilly Wachowski", ano = 1999,
                genero = Genero.FICCAO_CIENTIFICA.name, duracao = 136,
                sinopse = context.getString(R.string.seed_sinopse_matrix),
                poster = poster("poster_matrix"), usuarioId = bruno,
                dataCadastro = cadastradoHa(5)
            )
        ).toInt()
        val laLaLand = filmeDao.inserir(
            Filme(
                titulo = "La La Land", diretor = "Damien Chazelle", ano = 2016,
                genero = Genero.MUSICAL.name, duracao = 128,
                sinopse = context.getString(R.string.seed_sinopse_la_la_land),
                poster = poster("poster_la_la_land"), usuarioId = diego,
                dataCadastro = cadastradoHa(2)
            )
        ).toInt()

        fun avaliacao(usuario: Int, filme: Int, nota: Float, favorito: Boolean, resenha: Int?, diasAtras: Int) =
            Avaliacao(
                usuarioId = usuario,
                filmeId = filme,
                nota = nota,
                favorito = favorito,
                resenha = resenha?.let { context.getString(it) } ?: "",
                data = agora - diasAtras * UM_DIA
            )

        db.avaliacaoDao().salvarTodas(
            listOf(
                avaliacao(ana, cidadeDeDeus, 5f, true, R.string.seed_resenha_ana_cidade, 14),
                avaliacao(ana, centralDoBrasil, 4.5f, true, R.string.seed_resenha_ana_central, 11),
                avaliacao(ana, viagemDeChihiro, 5f, true, R.string.seed_resenha_ana_chihiro, 8),
                avaliacao(ana, laLaLand, 4f, false, null, 2),
                avaliacao(ana, aindaEstouAqui, 5f, true, R.string.seed_resenha_ana_ainda, 1),

                avaliacao(bruno, interestelar, 5f, true, R.string.seed_resenha_bruno_interestelar, 13),
                avaliacao(bruno, matrix, 4.5f, true, R.string.seed_resenha_bruno_matrix, 4),
                avaliacao(bruno, poderosoChefao, 5f, false, R.string.seed_resenha_bruno_chefao, 9),
                avaliacao(bruno, parasita, 4.5f, false, null, 6),
                avaliacao(bruno, cidadeDeDeus, 4.5f, false, null, 3),

                avaliacao(carla, parasita, 5f, true, R.string.seed_resenha_carla_parasita, 12),
                avaliacao(carla, bacurau, 4.5f, true, R.string.seed_resenha_carla_bacurau, 7),
                avaliacao(carla, autoDaCompadecida, 5f, true, R.string.seed_resenha_carla_auto, 5),
                avaliacao(carla, aindaEstouAqui, 4.5f, false, R.string.seed_resenha_carla_ainda, 2),
                avaliacao(carla, viagemDeChihiro, 4f, false, null, 10),

                avaliacao(diego, toyStory, 4.5f, true, R.string.seed_resenha_diego_toy, 6),
                avaliacao(diego, autoDaCompadecida, 4.5f, false, R.string.seed_resenha_diego_auto, 4),
                avaliacao(diego, interestelar, 3.5f, false, R.string.seed_resenha_diego_interestelar, 3),
                avaliacao(diego, laLaLand, 3f, false, R.string.seed_resenha_diego_lalaland, 1),
                avaliacao(diego, matrix, 5f, true, null, 2)
            )
        )

        db.seguidorDao().seguirTodos(
            listOf(
                Seguidor(seguidorId = ana, seguidoId = bruno),
                Seguidor(seguidorId = ana, seguidoId = carla),
                Seguidor(seguidorId = bruno, seguidoId = ana),
                Seguidor(seguidorId = bruno, seguidoId = diego),
                Seguidor(seguidorId = carla, seguidoId = ana),
                Seguidor(seguidorId = carla, seguidoId = diego),
                Seguidor(seguidorId = diego, seguidoId = carla)
            )
        )

        Log.d(TAG, "Dados iniciais inseridos com sucesso")
    }
}
