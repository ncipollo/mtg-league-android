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

/*
 {
        "user_id": 25,
        "first_name": "Nick",
        "last_name": "Cipollo",
        "office": "Boston 1 Federal",
        "overall_record": "1-0",
        "overall_points": 2,
        "week1_record": "1-0",
        "week2_record": "0-0",
        "week3_record": "0-0",
        "week4_record": "0-0",
        "ranking": "2nd"
}
 */
