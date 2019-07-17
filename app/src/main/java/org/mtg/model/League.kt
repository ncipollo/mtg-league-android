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

/*
{
        "id": 1,
        "name": "Ixalan",
        "set_abbreviation": "XLN",
        "created_at": "2019-07-12T13:03:58.896Z",
        "updated_at": "2019-07-12T13:03:58.896Z",
        "active": false,
        "entry_price_amount": null,
        "starting_packs_count": null
}

 */
