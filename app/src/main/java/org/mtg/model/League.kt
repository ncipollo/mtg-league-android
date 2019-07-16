package org.mtg.model

import java.util.*

data class League(
    val id: Long = 0,
    val name: String = "",
    val setAbbreviation: String = "",
    val createdAt: Date = Date(0),
    val updatedAt: Date = Date(0),
    val active: Boolean = false,
    val entryPriceAmount: Long? = null,
    val staringPacksCount: Long? = null
)
