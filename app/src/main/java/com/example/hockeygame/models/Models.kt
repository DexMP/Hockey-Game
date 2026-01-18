package com.example.hockeygame.models

// Игрок
data class Player(
    val `$id`: String = "",
    val teamId: String = "",
    val name: String = "",
    val number: Int = 0,
    val position: String = "", // вратарь, защитник, форвард
    val stats: PlayerStats = PlayerStats(),
    val rating: Float = 0f
)

data class PlayerStats(
    val goals: Int = 0,
    val assists: Int = 0,
    val gamesPlayed: Int = 0
)

// Матч
data class Match(
    val `$id`: String = "",
    val tournamentId: String = "",
    val team1Id: String = "",
    val team2Id: String = "",
    val date: String = "",
    val score: String? = null,
    val status: String = "planned" // planned, finished
)

// Турнир
data class Tournament(
    val `$id`: String = "",
    val name: String = "",
    val tournamentClass: String = "", // класс
    val teamStrength: String = "", // общие силы
    val status: String = "active",
    val teams: List<String> = emptyList() // ID команд
)

// Новость
data class News(
    val `$id`: String = "",
    val teamId: String = "",
    val title: String = "",
    val content: String = "",
    val author: String = "",
    val createdAt: String = ""
)

// Информация о клубе
data class ClubInfo(
    val `$id`: String = "",
    val teamId: String = "",
    val description: String = "",
    val founded: String = "",
    val contacts: String = ""
)

// Пользователь (расширенный)
data class UserProfile(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val theme: String = "light", // light/dark
    val avatar: String? = null
)
