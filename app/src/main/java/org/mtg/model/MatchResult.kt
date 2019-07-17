package org.mtg.model

import java.util.*

data class MatchResult(
    val id: Long = 0,
    val winnerId: Long = 0,
    val loserId: Long = 0,
    val gamesCount: Long = 0,
    val leagueId: Long = 0,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

/*
{
        "id": 1,
        "winner_id": 14,
        "loser_id": 1,
        "games_count": 3,
        "league_id": 7,
        "created_at": "2019-07-12T13:03:59.214Z",
        "updated_at": "2019-07-12T13:03:59.214Z",
        "user_id": 14
},
 */
