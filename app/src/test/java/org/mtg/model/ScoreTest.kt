package org.mtg.model

import org.junit.Test
import kotlin.test.assertEquals

class ScoreTest {

    @Test
    fun plus() {
        val score = Score() + 1
        assertEquals(expected = Score(life = 21, lifeChange = 1), actual = score)
    }

    @Test
    fun minus() {
        val score = Score() - 1
        assertEquals(expected = Score(life = 19, lifeChange = -1), actual = score)
    }
}
