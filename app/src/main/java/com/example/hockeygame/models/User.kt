package com.example.hockeygame.models

import io.github.agrevster.pocketbaseKotlin.models.utils.BaseModel

data class User(
    override val id: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val verified: Boolean = false,
    val dollars: Int = 0,
    val gems: Int = 0,
    val hockeyPass: Boolean = false,
    val created: String = "",
    val updated: String = ""
) : BaseModel()
