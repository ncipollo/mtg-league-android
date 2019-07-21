package org.mtg.model

data class Settings(
    val darkMode: Boolean = false,
    val selectedLeagueId: Long = 0,
    val selectedLeagueName: String = ""
)
