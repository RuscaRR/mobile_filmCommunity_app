# Claquete

Aplicativo Android de catálogo de filmes e séries, feito em Kotlin para a disciplina de Desenvolvimento Mobile (IFSP Araraquara).

Cada usuário cria uma conta, adiciona filmes e séries ao catálogo buscando pelo nome e escreve avaliações com nota. Na tela de Comunidade dá para ver os outros usuários e as avaliações de cada um.

Os nomes, pôsteres e sinopses vêm da API do [The Movie Database (TMDB)](https://www.themoviedb.org/).

## Integrantes

| Nome | Tarefas |
|------|---------|
| (Integrante 1) | Banco de dados (Room), login, cadastro de usuário, perfil e comunidade |
| (Integrante 2) | Temas, idiomas e ícones, catálogo, busca no TMDB e detalhes do filme |

## O que foi usado

- Activities e Intents (explícitas e implícitas)
- View Binding
- ConstraintLayout
- RecyclerView com Adapter e filtro por texto
- ActivityResult (retorno das telas e escolha de foto na galeria)
- Room para salvar usuários, filmes e avaliações
- Corrotinas (`lifecycleScope` com `Dispatchers.IO`)
- Tema claro/escuro e idiomas português/inglês
- API do TMDB (`HttpURLConnection` + `JSONObject`) e Coil para carregar os pôsteres

## Como rodar

1. Crie uma conta gratuita em https://www.themoviedb.org/ e gere uma chave em **Configurações > API**.
2. Abra o arquivo `local.properties` na raiz do projeto (o Android Studio cria ele sozinho) e adicione a linha:

   ```
   TMDB_API_KEY=sua_chave_aqui
   ```

   Esse arquivo não vai para o GitHub, então cada integrante coloca a própria chave.
3. Sincronize o Gradle e rode o app no emulador.

---

Este produto usa a API do TMDB, mas não é endossado nem certificado pelo TMDB.
