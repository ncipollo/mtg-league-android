package org.mtg.model

import java.util.*

data class MatchResult(
    val id: Long = 0,
    val winnerId: Long = 0,
    val loserId: Long = 0,
    val gamesCount: Long = 0,
    val leagueId: Long = 0,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val userId: Long? = null
)
