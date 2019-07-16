package org.mtg.model

data class Standing(
    val userId: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val office: String = "",
    val overallRecord: String = "",
    val week1Record: String = "",
    val week2Record: String = "",
    val week3Record: String = "",
    val week4Record: String = "",
    val ranking: String = ""
)
