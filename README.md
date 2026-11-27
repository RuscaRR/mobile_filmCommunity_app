# IFilmes

An Android app for cataloging movies and series, built in Kotlin for the Mobile Development course (IFSP Araraquara).

Each user creates an account, adds movies and series to the catalog by searching for them by name, and writes reviews with a rating. The Community screen lists the other users and the reviews each one has written.

Titles, posters and synopses come from the [The Movie Database (TMDB)](https://www.themoviedb.org/) API.

## Features

- Sign up and sign in, with passwords stored as a salted SHA-256 hash
- Login session that keeps the user signed in after the app is closed
- Home screen with two sections: **2026 Releases** (10 popular movies included on first launch) and **My List** (titles added through the search)
- Search on TMDB and add movies or series to the catalog
- Movie details with synopsis, rating average, reviews and a link to the TMDB page
- User profile with photo from the gallery and the list of reviews
- Community screen with all users
- Settings gear to switch between light/dark theme and Portuguese/English

## Built with

- Activities and Intents (explicit and implicit)
- View Binding
- ConstraintLayout
- RecyclerView with Adapter and text filter
- ActivityResult (screen results and picking a photo from the gallery)
- Room to store users, movies and reviews, with an initial catalog created through `RoomDatabase.Callback`
- Coroutines (`lifecycleScope` with `Dispatchers.IO`)
- SharedPreferences for the login session
- Light/dark theme and Portuguese/English languages
- TMDB API (`HttpURLConnection` + `JSONObject`) and Coil to load the posters

## Requirements

- Android Studio (Ladybug or newer, with AGP 8.7)
- JDK 11 or newer
- Android 8.0 (API 26) or newer on the device or emulator
- Internet connection for the TMDB search and the posters

## Project structure

```
app/src/main/java/br/com/ifilmes/
├── adapter/   RecyclerView adapters (movies, reviews, users)
├── data/      Room database, DAOs and the TMDB API client
├── model/     Room entities: Usuario, Filme, Avaliacao
├── ui/        Activities (login, sign up, home, search, details, community, profile)
└── util/      Password hash, login session and the screen filter
```

## How to run

1. Create a free account at https://www.themoviedb.org/ and generate an API key under **Settings > API**.
2. Open the `local.properties` file in the project root (Android Studio creates it automatically) and add the line:

   ```
   TMDB_API_KEY=your_key_here
   ```

   This file is not pushed to GitHub, so each developer adds their own key.
3. Sync Gradle and run the app on the emulator.

## Notes

- The 10 movies in **2026 Releases** are inserted only when the database is created. To see them again after testing, uninstall the app or clear its data in the emulator settings.
- Movies in **2026 Releases** belong to the app, so no user can remove them. A movie added through the search can be removed only by the user who added it.
- The login session expires after 7 days without opening the app. **Sign out** on the profile screen ends it right away.
- Passwords are never stored as typed: the app saves a SHA-256 hash of the email combined with the password.
- All data is stored locally on the device (Room/SQLite); there is no server.

## Credits

Developed by **Raul Rusca** and **Victor Schiabel**.

---

This product uses the TMDB API but is not endorsed or certified by TMDB.
